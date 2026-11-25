package edu.ups.poo.audiovisual.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class CatalogoContenidoTest {

    private CatalogoContenido catalogo;
    private Pelicula origen;
    private Podcast podcast;

    @Before
    public void preparar() {
        catalogo = new CatalogoContenido();
        origen = new Pelicula("El Origen", 148, "Ciencia Ficción", "Christopher Nolan");
        podcast = new Podcast("Mentes Curiosas", 35, "Educativo", "Universo", 47, "Spotify");
        catalogo.agregar(origen);
        catalogo.agregar(podcast);
    }

    @Test
    public void agregaYCuentaContenidos() {
        assertEquals(2, catalogo.cantidad());
        assertSame(origen, catalogo.listar().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaContenidoNulo() {
        catalogo.agregar(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listaDevueltaNoSePuedeModificar() {
        catalogo.listar().clear();
    }

    @Test
    public void eliminaPorIdExistente() {
        assertTrue(catalogo.eliminarPorId(origen.getId()));
        assertEquals(1, catalogo.cantidad());
    }

    @Test
    public void eliminarIdInexistenteDevuelveFalso() {
        assertFalse(catalogo.eliminarPorId(-1));
        assertEquals(2, catalogo.cantidad());
    }

    @Test
    public void buscaPorIdConOptional() {
        assertSame(podcast, catalogo.buscarPorId(podcast.getId()).get());
        assertFalse(catalogo.buscarPorId(-1).isPresent());
    }

    @Test
    public void buscaPorTituloSinImportarMayusculasNiTextoParcial() {
        List<ContenidoAudiovisual> encontrados = catalogo.buscarPorTitulo("ORIGEN");

        assertEquals(1, encontrados.size());
        assertSame(origen, encontrados.get(0));
    }

    @Test
    public void busquedaSinCoincidenciasDevuelveListaVacia() {
        assertTrue(catalogo.buscarPorTitulo("no existe").isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void busquedaConTextoVacioEsRechazada() {
        catalogo.buscarPorTitulo("  ");
    }

    @Test
    public void reemplazarContenidosSustituyeTodoElCatalogo() {
        Pelicula nueva = new Pelicula("Avatar", 125, "Acción", "James Cameron");

        catalogo.reemplazarContenidos(Arrays.asList(nueva));

        assertEquals(1, catalogo.cantidad());
        assertSame(nueva, catalogo.listar().get(0));
    }

    @Test
    public void reemplazarConListaVaciaVaciaElCatalogo() {
        catalogo.reemplazarContenidos(Arrays.<ContenidoAudiovisual>asList());

        assertEquals(0, catalogo.cantidad());
    }
}
