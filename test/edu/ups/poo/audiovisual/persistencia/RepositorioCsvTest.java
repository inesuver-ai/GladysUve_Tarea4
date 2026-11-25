package edu.ups.poo.audiovisual.persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import edu.ups.poo.audiovisual.modelo.Actor;
import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.Documental;
import edu.ups.poo.audiovisual.modelo.Investigador;
import edu.ups.poo.audiovisual.modelo.Pelicula;
import edu.ups.poo.audiovisual.modelo.Podcast;
import edu.ups.poo.audiovisual.modelo.ProgramaDeRadio;
import edu.ups.poo.audiovisual.modelo.SerieDeTV;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RepositorioCsvTest {

    @Rule
    public TemporaryFolder carpeta = new TemporaryFolder();

    private Path archivo;
    private RepositorioCsv repositorio;

    @Before
    public void preparar() {
        archivo = carpeta.getRoot().toPath().resolve("contenidos.csv");
        repositorio = new RepositorioCsv(archivo, RegistroConversores.porDefecto());
    }

    private void escribir(String... lineas) throws IOException {
        Files.write(archivo, Arrays.asList(lineas), StandardCharsets.UTF_8);
    }

    private void assertErrorContiene(String fragmento) {
        try {
            repositorio.cargar();
            fail("Debió lanzar RepositorioException");
        } catch (RepositorioException e) {
            assertTrue("Mensaje real: " + e.getMessage(), e.getMessage().contains(fragmento));
        }
    }

    private List<ContenidoAudiovisual> contenidoCompleto() {
        Pelicula pelicula = new Pelicula("El Origen", 148, "Ciencia Ficción", "Christopher Nolan");
        pelicula.agregarActor(new Actor("Leonardo DiCaprio", "Estadounidense", 1974));
        pelicula.agregarActor(new Actor("Joseph Gordon-Levitt", "Estadounidense", 1981));
        SerieDeTV serie = new SerieDeTV("Stranger Things", 50, "Ciencia Ficción", "Duffer Bros");
        serie.agregarTemporada(1, 8);
        serie.agregarTemporada(2, 9);
        Documental documental = new Documental("El Cambio Climático", 60, "Educativo", "Medio Ambiente");
        documental.agregarInvestigador(new Investigador("María López", "Climatología", "Universidad Nacional"));
        return new ArrayList<>(Arrays.asList(
                pelicula, serie, documental,
                new Podcast("Mentes Curiosas", 35, "Educativo", "Universo", 47, "Spotify"),
                new ProgramaDeRadio("La Hora Cultural", 60, "Cultura", "Ana Vásquez", "92.5 FM", true)));
    }

    // ----- Escritura y lectura de ida y vuelta -----

    @Test
    public void guardarYCargarConservaTodosLosTiposYSusDatos() throws RepositorioException {
        List<ContenidoAudiovisual> originales = contenidoCompleto();

        repositorio.guardar(originales);
        List<ContenidoAudiovisual> leidos = repositorio.cargar();

        assertEquals(originales.size(), leidos.size());
        for (int i = 0; i < originales.size(); i++) {
            assertEquals(originales.get(i).getClass(), leidos.get(i).getClass());
            assertEquals(originales.get(i).obtenerDetalles(), leidos.get(i).obtenerDetalles());
        }
    }

    @Test
    public void cargarRecuperaActoresTemporadasEInvestigadores() throws RepositorioException {
        repositorio.guardar(contenidoCompleto());

        List<ContenidoAudiovisual> leidos = repositorio.cargar();

        assertEquals(2, ((Pelicula) leidos.get(0)).getActores().size());
        assertEquals(1974, ((Pelicula) leidos.get(0)).getActores().get(0).getAnioNacimiento());
        assertEquals(17, ((SerieDeTV) leidos.get(1)).getTotalEpisodios());
        assertEquals("Universidad Nacional",
                ((Documental) leidos.get(2)).getInvestigadores().get(0).getInstitucion());
        assertTrue(((ProgramaDeRadio) leidos.get(4)).isEnVivo());
    }

    @Test
    public void guardarListaVaciaCreaArchivoQueSeCargaVacio() throws RepositorioException {
        repositorio.guardar(new ArrayList<ContenidoAudiovisual>());

        assertTrue(Files.exists(archivo));
        assertTrue(repositorio.cargar().isEmpty());
    }

    @Test
    public void caracteresEspecialesEnTextosSobrevivenAlArchivo() throws RepositorioException {
        Pelicula pelicula = new Pelicula("Dr. Strange; el origen | parte:1", 100, "Acción;Fantasía", "A\\B");
        pelicula.agregarActor(new Actor("Nombre; con | raros: símbolos", "Ecuatoriana", 1990));

        repositorio.guardar(new ArrayList<ContenidoAudiovisual>(Arrays.asList(pelicula)));
        Pelicula leida = (Pelicula) repositorio.cargar().get(0);

        assertEquals("Dr. Strange; el origen | parte:1", leida.getTitulo());
        assertEquals("Acción;Fantasía", leida.getGenero());
        assertEquals("A\\B", leida.getDirector());
        assertEquals("Nombre; con | raros: símbolos", leida.getActores().get(0).getNombre());
    }

    @Test
    public void peliculaSinActoresSeGuardaYSeCarga() throws RepositorioException {
        Pelicula sinActores = new Pelicula("Muda", 80, "Drama", "Director");

        repositorio.guardar(new ArrayList<ContenidoAudiovisual>(Arrays.asList(sinActores)));

        assertTrue(((Pelicula) repositorio.cargar().get(0)).getActores().isEmpty());
    }

    @Test
    public void guardarCreaLasCarpetasQueFaltan() throws RepositorioException {
        Path anidado = carpeta.getRoot().toPath().resolve("a").resolve("b").resolve("datos.csv");

        new RepositorioCsv(anidado, RegistroConversores.porDefecto()).guardar(contenidoCompleto());

        assertTrue(Files.exists(anidado));
    }

    @Test
    public void guardarNoDejaArchivoTemporal() throws RepositorioException {
        repositorio.guardar(contenidoCompleto());

        assertFalse(Files.exists(archivo.resolveSibling("contenidos.csv.tmp")));
    }

    @Test
    public void guardarSobrescribeElArchivoAnterior() throws RepositorioException {
        repositorio.guardar(contenidoCompleto());
        repositorio.guardar(new ArrayList<ContenidoAudiovisual>(Arrays.asList(
                new Podcast("Solo uno", 10, "Educativo", "Tema", 1, "Spotify"))));

        assertEquals(1, repositorio.cargar().size());
    }

    // ----- Lectura de archivos escritos a mano -----

    @Test
    public void cargarIgnoraComentariosYLineasEnBlanco() throws Exception {
        escribir("# encabezado", "", "   ", "PODCAST;Mentes;35;Educativo;Universo;47;Spotify", "# fin");

        assertEquals(1, repositorio.cargar().size());
    }

    @Test
    public void cargarAceptaTipoEnMinusculas() throws Exception {
        escribir("podcast;Mentes;35;Educativo;Universo;47;Spotify");

        assertTrue(repositorio.cargar().get(0) instanceof Podcast);
    }

    // ----- Errores -----

    @Test
    public void cargarArchivoInexistenteLanzaRepositorioException() {
        assertErrorContiene("No existe el archivo");
    }

    @Test
    public void tipoDesconocidoInformaLaLinea() throws Exception {
        escribir("# cabecera", "LIBRO;Don Quijote;300;Novela;Cervantes");

        assertErrorContiene("Línea 2");
    }

    @Test
    public void duracionNoNumericaEsRechazada() throws Exception {
        escribir("PODCAST;Mentes;abc;Educativo;Universo;47;Spotify");

        assertErrorContiene("duración");
    }

    @Test
    public void cantidadIncorrectaDeCamposEsRechazada() throws Exception {
        escribir("PODCAST;Mentes;35;Educativo;Universo");

        assertErrorContiene("Se esperaban 7 campos");
    }

    @Test
    public void lineaConMenosDeCuatroCamposEsRechazada() throws Exception {
        escribir("PODCAST;Mentes");

        assertErrorContiene("Faltan campos");
    }

    @Test
    public void reglasDelModeloTambienSeAplicanAlLeer() throws Exception {
        escribir("PODCAST;;35;Educativo;Universo;47;Spotify");

        assertErrorContiene("título");
    }

    @Test
    public void actorConDatosIncompletosEsRechazado() throws Exception {
        escribir("PELICULA;X;100;Drama;Director;Solo Nombre:Ecuatoriana");

        assertErrorContiene("actor");
    }

    @Test
    public void booleanoInvalidoEnRadioEsRechazado() throws Exception {
        escribir("RADIO;Programa;30;Cultura;Locutor;90 FM;quizas");

        assertErrorContiene("true o false");
    }

    @Test
    public void guardarEnRutaImposibleLanzaRepositorioException() throws Exception {
        Path archivoComoCarpeta = carpeta.newFile("ocupado.txt").toPath();
        RepositorioCsv imposible = new RepositorioCsv(
                archivoComoCarpeta.resolve("datos.csv"), RegistroConversores.porDefecto());

        try {
            imposible.guardar(contenidoCompleto());
            fail("Debió lanzar RepositorioException");
        } catch (RepositorioException e) {
            assertTrue(e.getMessage().contains("No se pudo guardar"));
        }
    }

    // ----- Registro de conversores (OCP) -----

    @Test(expected = IllegalArgumentException.class)
    public void registroRechazaTipoDesconocido() {
        RegistroConversores.porDefecto().paraTipo("LIBRO");
    }

    @Test
    public void registroPermiteAgregarUnConversorNuevoSinModificarElRepositorio() throws Exception {
        RegistroConversores registro = new RegistroConversores();
        registro.registrar(new ConversorPodcast());
        RepositorioCsv soloPodcasts = new RepositorioCsv(archivo, registro);
        escribir("PODCAST;Mentes;35;Educativo;Universo;47;Spotify");

        assertEquals(1, soloPodcasts.cargar().size());
    }
}
