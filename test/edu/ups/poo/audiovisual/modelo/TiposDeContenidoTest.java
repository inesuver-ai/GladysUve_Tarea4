package edu.ups.poo.audiovisual.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;

public class TiposDeContenidoTest {

    // ----- Película y actores -----

    @Test
    public void peliculaGuardaActoresYLosMuestra() {
        Pelicula pelicula = new Pelicula("El Origen", 148, "Ciencia Ficción", "Christopher Nolan");
        pelicula.agregarActor(new Actor("Leonardo DiCaprio", "Estadounidense", 1974));
        pelicula.agregarActor(new Actor("Joseph Gordon-Levitt", "Estadounidense", 1981));

        assertEquals(2, pelicula.getActores().size());
        String detalles = pelicula.obtenerDetalles();
        assertTrue(detalles.contains("Director: Christopher Nolan"));
        assertTrue(detalles.contains("  - Leonardo DiCaprio"));
        assertTrue(detalles.contains("  - Joseph Gordon-Levitt"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listaDeActoresNoSePuedeModificarDesdeAfuera() {
        Pelicula pelicula = new Pelicula("X", 90, "Drama", "Y");
        pelicula.getActores().add(new Actor("A", "B", 1990));
    }

    @Test(expected = IllegalArgumentException.class)
    public void peliculaRechazaActorNulo() {
        new Pelicula("X", 90, "Drama", "Y").agregarActor(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void peliculaRechazaDirectorVacio() {
        new Pelicula("X", 90, "Drama", " ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void actorRechazaAnioInvalido() {
        new Actor("Alguien", "Ecuatoriana", 0);
    }

    @Test
    public void actorExponeSusDatos() {
        Actor actor = new Actor("Zoe Saldaña", "Estadounidense", 1978);

        assertEquals("Zoe Saldaña", actor.getNombre());
        assertEquals("Estadounidense", actor.getNacionalidad());
        assertEquals(1978, actor.getAnioNacimiento());
    }

    // ----- Serie de TV y temporadas -----

    @Test
    public void serieCuentaTemporadasYEpisodios() {
        SerieDeTV serie = new SerieDeTV("Stranger Things", 50, "Ciencia Ficción", "Duffer Bros");
        serie.agregarTemporada(1, 8);
        serie.agregarTemporada(2, 9);
        serie.agregarTemporada(3, 8);

        assertEquals(3, serie.getTemporadas().size());
        assertEquals(25, serie.getTotalEpisodios());
    }

    @Test
    public void serieSinTemporadasTieneCeroEpisodios() {
        assertEquals(0, new SerieDeTV("Piloto", 30, "Comedia", "Alguien").getTotalEpisodios());
    }

    @Test
    public void serieMuestraDuracionPorEpisodio() {
        String detalles = new SerieDeTV("Stranger Things", 50, "Ciencia Ficción", "Duffer Bros").obtenerDetalles();

        assertTrue(detalles.contains("Duración por episodio: 50 min"));
        assertTrue(detalles.contains("Productor: Duffer Bros"));
        assertTrue(detalles.contains("Temporadas: 0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void temporadaRechazaCeroEpisodios() {
        new SerieDeTV("X", 30, "Drama", "Y").agregarTemporada(1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void temporadaRechazaNumeroNegativo() {
        new Temporada(-1, 5);
    }

    @Test
    public void temporadaExponeSusDatos() {
        Temporada temporada = new Temporada(2, 9);

        assertEquals(2, temporada.getNumero());
        assertEquals(9, temporada.getEpisodios());
    }

    // ----- Documental e investigadores -----

    @Test
    public void documentalGuardaInvestigadoresYLosMuestra() {
        Documental documental = new Documental("El Cambio Climático", 60, "Educativo", "Medio Ambiente");
        documental.agregarInvestigador(new Investigador("María López", "Climatología", "Universidad Nacional"));

        List<Investigador> investigadores = documental.getInvestigadores();
        assertEquals(1, investigadores.size());
        assertEquals("Universidad Nacional", investigadores.get(0).getInstitucion());
        assertTrue(documental.obtenerDetalles()
                .contains("  - María López (Climatología, Universidad Nacional)"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void documentalRechazaInvestigadorNulo() {
        new Documental("X", 60, "Educativo", "Tema").agregarInvestigador(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void investigadorRechazaInstitucionVacia() {
        new Investigador("Ana", "Física", "");
    }

    // ----- Podcast -----

    @Test
    public void podcastPuedeCompartirseEnSuPlataforma() {
        Podcast podcast = new Podcast("Mentes Curiosas", 35, "Educativo", "Universo", 47, "Spotify");

        assertEquals("Compartiendo el episodio 47 en Spotify... ¡Listo!", podcast.compartir());
    }

    @Test
    public void podcastMuestraSusDatosEspecificos() {
        String detalles = new Podcast("Mentes Curiosas", 35, "Educativo", "Universo", 47, "Spotify")
                .obtenerDetalles();

        assertTrue(detalles.contains("=== Podcast ==="));
        assertTrue(detalles.contains("Número de episodio: 47"));
        assertTrue(detalles.contains("Plataforma: Spotify"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void podcastRechazaEpisodioCero() {
        new Podcast("X", 30, "Educativo", "Tema", 0, "Spotify");
    }

    // ----- Programa de radio -----

    @Test
    public void radioEmitirEnVivoCambiaElEstado() {
        ProgramaDeRadio radio = new ProgramaDeRadio("La Hora Cultural", 60, "Cultura", "Ana Vásquez", "92.5 FM", false);
        assertFalse(radio.isEnVivo());

        String mensaje = radio.emitirEnVivo();

        assertTrue(radio.isEnVivo());
        assertEquals("El programa 'La Hora Cultural' está en transmisión en vivo.", mensaje);
    }

    @Test
    public void radioMuestraSiEstaEnVivo() {
        ProgramaDeRadio radio = new ProgramaDeRadio("Noticias", 20, "Informativo", "Carlos", "98.5 FM", true);

        assertTrue(radio.obtenerDetalles().contains("¿En vivo?: Sí"));
        radio.setEnVivo(false);
        assertTrue(radio.obtenerDetalles().contains("¿En vivo?: No"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void radioRechazaFrecuenciaVacia() {
        new ProgramaDeRadio("X", 20, "Informativo", "Carlos", " ", true);
    }

    // ----- Polimorfismo (LSP) -----

    @Test
    public void todasLasSubclasesSePuedenUsarComoContenidoBase() {
        ContenidoAudiovisual[] contenidos = {
            new Pelicula("P", 100, "Drama", "D"),
            new SerieDeTV("S", 40, "Drama", "P"),
            new Documental("D", 50, "Educativo", "T"),
            new Podcast("Po", 30, "Educativo", "T", 1, "Spotify"),
            new ProgramaDeRadio("R", 20, "Cultura", "L", "90 FM", false)
        };

        for (ContenidoAudiovisual contenido : contenidos) {
            assertTrue(contenido.obtenerDetalles().contains("Título: " + contenido.getTitulo()));
            assertTrue(contenido.getId() > 0);
        }
    }
}
