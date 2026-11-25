package edu.ups.poo.audiovisual.controlador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.ups.poo.audiovisual.modelo.CatalogoContenido;
import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.Documental;
import edu.ups.poo.audiovisual.modelo.Pelicula;
import edu.ups.poo.audiovisual.modelo.Podcast;
import edu.ups.poo.audiovisual.modelo.ProgramaDeRadio;
import edu.ups.poo.audiovisual.modelo.SerieDeTV;
import edu.ups.poo.audiovisual.persistencia.RepositorioContenido;
import edu.ups.poo.audiovisual.persistencia.RepositorioException;
import edu.ups.poo.audiovisual.vista.VistaConsola;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ControladorContenidoTest {

    private static class RepositorioEnMemoria implements RepositorioContenido {

        List<ContenidoAudiovisual> guardados = new ArrayList<>();
        boolean fallar = false;

        @Override
        public List<ContenidoAudiovisual> cargar() throws RepositorioException {
            if (fallar) {
                throw new RepositorioException("fallo de lectura simulado");
            }
            return new ArrayList<>(guardados);
        }

        @Override
        public void guardar(List<ContenidoAudiovisual> contenidos) throws RepositorioException {
            if (fallar) {
                throw new RepositorioException("fallo de escritura simulado");
            }
            guardados = new ArrayList<>(contenidos);
        }
    }

    private CatalogoContenido catalogo = new CatalogoContenido();
    private RepositorioEnMemoria repositorio = new RepositorioEnMemoria();
    private ByteArrayOutputStream bytesDeSalida = new ByteArrayOutputStream();

    private ControladorContenido crearControlador(String entrada) throws Exception {
        VistaConsola vista = new VistaConsola(
                new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(bytesDeSalida, true, "UTF-8"));
        return new ControladorContenido(catalogo, repositorio, vista, FabricasContenido.porDefecto());
    }

    private String salida() throws Exception {
        return bytesDeSalida.toString("UTF-8");
    }

    private String lineas(String... lineas) {
        return String.join("\n", lineas) + "\n";
    }

    // ----- Agregar -----

    @Test
    public void agregarPeliculaCreaElContenidoEnElCatalogo() throws Exception {
        ControladorContenido controlador = crearControlador(
                lineas("1", "El Origen", "148", "Ciencia Ficción", "Christopher Nolan"));

        controlador.agregarContenido();

        assertEquals(1, catalogo.cantidad());
        assertTrue(catalogo.listar().get(0) instanceof Pelicula);
        assertTrue(salida().contains("Película agregado con ID"));
    }

    @Test
    public void agregarCreaCadaUnoDeLosCincoTipos() throws Exception {
        ControladorContenido controlador = crearControlador(lineas(
                "1", "P", "100", "Drama", "Dir",
                "2", "S", "40", "Drama", "Prod",
                "3", "D", "50", "Educativo", "Tema",
                "4", "Po", "30", "Educativo", "Tema", "7", "Spotify",
                "5", "R", "20", "Cultura", "Locutor", "90 FM", "si"));

        for (int i = 0; i < 5; i++) {
            controlador.agregarContenido();
        }

        List<ContenidoAudiovisual> creados = catalogo.listar();
        assertEquals(5, creados.size());
        assertTrue(creados.get(0) instanceof Pelicula);
        assertTrue(creados.get(1) instanceof SerieDeTV);
        assertTrue(creados.get(2) instanceof Documental);
        assertTrue(creados.get(3) instanceof Podcast);
        assertTrue(((ProgramaDeRadio) creados.get(4)).isEnVivo());
    }

    @Test
    public void cancelarAlElegirTipoNoAgregaNada() throws Exception {
        crearControlador("0\n").agregarContenido();

        assertEquals(0, catalogo.cantidad());
    }

    @Test
    public void datosInvalidosMuestranErrorYNoAgregan() throws Exception {
        ControladorContenido controlador = crearControlador(lineas("1", "", "100", "Drama", "Dir"));

        controlador.ejecutar(AccionMenu.AGREGAR);

        assertEquals(0, catalogo.cantidad());
        assertTrue(salida().contains("[ERROR]"));
    }

    @Test
    public void episodioNoNumericoDelPodcastMuestraError() throws Exception {
        ControladorContenido controlador = crearControlador(
                lineas("4", "Po", "30", "Educativo", "Tema", "abc", "Spotify"));

        controlador.ejecutar(AccionMenu.AGREGAR);

        assertEquals(0, catalogo.cantidad());
        assertTrue(salida().contains("número de episodio"));
    }

    @Test
    public void respuestaInvalidaEnVivoMuestraError() throws Exception {
        ControladorContenido controlador = crearControlador(
                lineas("5", "R", "20", "Cultura", "Loc", "90 FM", "tal vez"));

        controlador.ejecutar(AccionMenu.AGREGAR);

        assertEquals(0, catalogo.cantidad());
        assertTrue(salida().contains("si' o 'no"));
    }

    // ----- Listar, buscar, eliminar -----

    @Test
    public void listarCatalogoVacioInformaQueNoHayContenidos() throws Exception {
        crearControlador("").listarContenidos();

        assertTrue(salida().contains("No hay contenidos para mostrar."));
    }

    @Test
    public void buscarMuestraSoloLasCoincidencias() throws Exception {
        catalogo.agregar(new Pelicula("El Origen", 148, "Ciencia Ficción", "Nolan"));
        catalogo.agregar(new Podcast("Mentes Curiosas", 35, "Educativo", "Tema", 1, "Spotify"));

        crearControlador("mentes\n").buscarPorTitulo();

        assertTrue(salida().contains("Mentes Curiosas"));
        assertFalse(salida().contains("El Origen"));
    }

    @Test
    public void eliminarQuitaElContenidoPorId() throws Exception {
        Pelicula pelicula = new Pelicula("El Origen", 148, "Ciencia Ficción", "Nolan");
        catalogo.agregar(pelicula);

        crearControlador(pelicula.getId() + "\n").eliminarContenido();

        assertEquals(0, catalogo.cantidad());
        assertTrue(salida().contains("eliminado"));
    }

    @Test
    public void eliminarIdInexistenteMuestraError() throws Exception {
        crearControlador("-1\n").eliminarContenido();

        assertTrue(salida().contains("No existe un contenido con ID -1"));
    }

    // ----- Datos asociados -----

    @Test
    public void agregarActorAUnaPelicula() throws Exception {
        Pelicula pelicula = new Pelicula("El Origen", 148, "Ciencia Ficción", "Nolan");
        catalogo.agregar(pelicula);

        crearControlador(lineas(String.valueOf(pelicula.getId()), "Leonardo DiCaprio", "Estadounidense", "1974"))
                .agregarActor();

        assertEquals(1, pelicula.getActores().size());
    }

    @Test
    public void agregarTemporadaAUnaSerie() throws Exception {
        SerieDeTV serie = new SerieDeTV("Stranger Things", 50, "Ciencia Ficción", "Duffer Bros");
        catalogo.agregar(serie);

        crearControlador(lineas(String.valueOf(serie.getId()), "1", "8")).agregarTemporada();

        assertEquals(8, serie.getTotalEpisodios());
    }

    @Test
    public void agregarInvestigadorAUnDocumental() throws Exception {
        Documental documental = new Documental("Cosmos", 45, "Divulgación", "Astronomía");
        catalogo.agregar(documental);

        crearControlador(lineas(String.valueOf(documental.getId()), "Carl Sagan", "Astronomía", "Cornell"))
                .agregarInvestigador();

        assertEquals(1, documental.getInvestigadores().size());
    }

    @Test
    public void agregarActorAUnContenidoQueNoEsPeliculaMuestraError() throws Exception {
        Podcast podcast = new Podcast("Mentes", 35, "Educativo", "Tema", 1, "Spotify");
        catalogo.agregar(podcast);
        ControladorContenido controlador = crearControlador(String.valueOf(podcast.getId()) + "\n");

        controlador.ejecutar(AccionMenu.AGREGAR_ACTOR);

        assertTrue(salida().contains("No existe una película con ID " + podcast.getId()));
    }

    // ----- Persistencia -----

    @Test
    public void guardarEnviaElCatalogoAlRepositorio() throws Exception {
        catalogo.agregar(new Podcast("Mentes", 35, "Educativo", "Tema", 1, "Spotify"));

        crearControlador("").guardarContenidos();

        assertEquals(1, repositorio.guardados.size());
        assertTrue(salida().contains("Se guardaron 1 contenidos."));
    }

    @Test
    public void cargarReemplazaElCatalogoConLoDelRepositorio() throws Exception {
        repositorio.guardados.add(new Podcast("Guardado", 35, "Educativo", "Tema", 1, "Spotify"));
        catalogo.agregar(new Pelicula("Temporal", 90, "Drama", "Dir"));

        crearControlador("").cargarContenidos();

        assertEquals(1, catalogo.cantidad());
        assertEquals("Guardado", catalogo.listar().get(0).getTitulo());
    }

    @Test
    public void errorDelRepositorioAlGuardarSeMuestraSinRomperElPrograma() throws Exception {
        repositorio.fallar = true;

        crearControlador("").guardarContenidos();

        assertTrue(salida().contains("[ERROR] fallo de escritura simulado"));
    }

    @Test
    public void errorDelRepositorioAlCargarConservaElCatalogoActual() throws Exception {
        catalogo.agregar(new Pelicula("Actual", 90, "Drama", "Dir"));
        repositorio.fallar = true;

        crearControlador("").cargarContenidos();

        assertEquals(1, catalogo.cantidad());
        assertTrue(salida().contains("[ERROR] fallo de lectura simulado"));
    }

    // ----- Ciclo completo del menú -----

    @Test
    public void iniciarCargaDatosMuestraElMenuYTerminaConCero() throws Exception {
        repositorio.guardados.add(new Podcast("Guardado", 35, "Educativo", "Tema", 1, "Spotify"));

        crearControlador("0\n").iniciar();

        assertTrue(salida().contains("Se cargaron 1 contenidos."));
        assertTrue(salida().contains("--- Menú principal ---"));
        assertTrue(salida().contains("Hasta luego."));
    }

    @Test
    public void iniciarTerminaConMensajeSiSeAcabaLaEntrada() throws Exception {
        crearControlador("").iniciar();

        assertTrue(salida().contains("Entrada finalizada."));
    }

    @Test
    public void flujoCompletoAgregarGuardarYSalir() throws Exception {
        crearControlador(lineas("2", "1", "El Origen", "148", "Ciencia Ficción", "Nolan", "8", "0")).iniciar();

        assertEquals(1, repositorio.guardados.size());
        assertEquals("El Origen", repositorio.guardados.get(0).getTitulo());
    }

    // ----- Menú -----

    @Test
    public void menuTieneNueveOpcionesNumeradasYSalirEsCero() {
        assertEquals(9, AccionMenu.textosDelMenu().size());
        assertEquals(AccionMenu.SALIR, AccionMenu.desdeOpcion(0));
        assertEquals(AccionMenu.LISTAR, AccionMenu.desdeOpcion(1));
        assertEquals(AccionMenu.CARGAR, AccionMenu.desdeOpcion(9));
    }

    @Test(expected = IllegalArgumentException.class)
    public void opcionDeMenuFueraDeRangoEsRechazada() {
        AccionMenu.desdeOpcion(10);
    }
}
