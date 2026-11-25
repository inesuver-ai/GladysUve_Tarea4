package edu.ups.poo.audiovisual.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ContenidoAudiovisualTest {

    private Pelicula crearPelicula() {
        return new Pelicula("El Origen", 148, "Ciencia Ficción", "Christopher Nolan");
    }

    @Test
    public void constructorGuardaDatosComunesYLimpiaEspacios() {
        Pelicula pelicula = new Pelicula("  Avatar  ", 125, " Acción ", "James Cameron");

        assertEquals("Avatar", pelicula.getTitulo());
        assertEquals(125, pelicula.getDuracionEnMinutos());
        assertEquals("Acción", pelicula.getGenero());
    }

    @Test
    public void cadaContenidoRecibeUnIdMayorQueElAnterior() {
        Pelicula primera = crearPelicula();
        Pelicula segunda = crearPelicula();

        assertTrue(segunda.getId() > primera.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void tituloVacioEsRechazado() {
        new Pelicula("   ", 100, "Drama", "Alguien");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tituloNuloEsRechazado() {
        new Pelicula(null, 100, "Drama", "Alguien");
    }

    @Test(expected = IllegalArgumentException.class)
    public void duracionCeroEsRechazada() {
        new Pelicula("Corto", 0, "Drama", "Alguien");
    }

    @Test(expected = IllegalArgumentException.class)
    public void duracionNegativaEsRechazada() {
        new Pelicula("Corto", -5, "Drama", "Alguien");
    }

    @Test(expected = IllegalArgumentException.class)
    public void generoVacioEsRechazado() {
        new Pelicula("Corto", 10, "", "Alguien");
    }

    @Test
    public void duracionMinimaValidaEsUnMinuto() {
        assertEquals(1, new Pelicula("Mínima", 1, "Experimental", "Alguien").getDuracionEnMinutos());
    }

    @Test
    public void settersActualizanYValidan() {
        Pelicula pelicula = crearPelicula();

        pelicula.setTitulo("Interestelar");
        pelicula.setDuracionEnMinutos(169);
        pelicula.setGenero("Drama");

        assertEquals("Interestelar", pelicula.getTitulo());
        assertEquals(169, pelicula.getDuracionEnMinutos());
        assertEquals("Drama", pelicula.getGenero());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setterDeDuracionRechazaValorInvalido() {
        crearPelicula().setDuracionEnMinutos(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setterDeTituloRechazaValorVacio() {
        crearPelicula().setTitulo("");
    }

    @Test
    public void detallesIncluyenEncabezadoYDatosComunes() {
        String detalles = crearPelicula().obtenerDetalles();

        assertTrue(detalles.startsWith("=== Película ==="));
        assertTrue(detalles.contains("Título: El Origen"));
        assertTrue(detalles.contains("Duración: 148 min"));
        assertTrue(detalles.contains("Género: Ciencia Ficción"));
    }
}
