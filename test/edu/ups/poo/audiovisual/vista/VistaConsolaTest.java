package edu.ups.poo.audiovisual.vista;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.Podcast;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class VistaConsolaTest {

    private ByteArrayOutputStream bytesDeSalida;

    private VistaConsola crearVista(String entrada) throws Exception {
        bytesDeSalida = new ByteArrayOutputStream();
        return new VistaConsola(
                new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(bytesDeSalida, true, "UTF-8"));
    }

    private String salida() throws Exception {
        return bytesDeSalida.toString("UTF-8");
    }

    @Test
    public void elegirOpcionDevuelveElNumeroIngresado() throws Exception {
        VistaConsola vista = crearVista("2\n");

        assertEquals(2, vista.elegirOpcion("Menú", Arrays.asList("Uno", "Dos", "Tres")));
        assertTrue(salida().contains("1. Uno"));
        assertTrue(salida().contains("0. Salir / Cancelar"));
    }

    @Test
    public void elegirOpcionCeroSignificaSalir() throws Exception {
        assertEquals(0, crearVista("0\n").elegirOpcion("Menú", Arrays.asList("Uno")));
    }

    @Test
    public void elegirOpcionRepiteLaPreguntaSiElDatoEsInvalido() throws Exception {
        VistaConsola vista = crearVista("abc\n9\n-1\n1\n");

        assertEquals(1, vista.elegirOpcion("Menú", Arrays.asList("Uno", "Dos")));
        assertTrue(salida().contains("[ERROR] Elija un número entre 0 y 2."));
    }

    @Test
    public void leerTextoQuitaEspaciosDeLosExtremos() throws Exception {
        assertEquals("Hola", crearVista("  Hola  \n").leerTexto("Texto"));
    }

    @Test
    public void leerEnteroRepiteHastaRecibirUnNumero() throws Exception {
        VistaConsola vista = crearVista("x\n3.5\n 42 \n");

        assertEquals(42, vista.leerEntero("Número"));
        assertTrue(salida().contains("Ingrese un número entero válido."));
    }

    @Test(expected = EntradaFinalizadaException.class)
    public void sinMasEntradaSeLanzaEntradaFinalizada() throws Exception {
        crearVista("").leerTexto("Texto");
    }

    @Test(expected = EntradaFinalizadaException.class)
    public void leerEnteroSinEntradaNoQuedaEnBucleInfinito() throws Exception {
        crearVista("abc\n").leerEntero("Número");
    }

    @Test
    public void mostrarContenidosListaIdYDetalles() throws Exception {
        VistaConsola vista = crearVista("");
        Podcast podcast = new Podcast("Mentes Curiosas", 35, "Educativo", "Universo", 47, "Spotify");

        vista.mostrarContenidos(Arrays.<ContenidoAudiovisual>asList(podcast));

        assertTrue(salida().contains("[ID " + podcast.getId() + "]"));
        assertTrue(salida().contains("Título: Mentes Curiosas"));
    }

    @Test
    public void mostrarContenidosVacioInformaQueNoHayNada() throws Exception {
        VistaConsola vista = crearVista("");

        vista.mostrarContenidos(new ArrayList<ContenidoAudiovisual>());

        assertTrue(salida().contains("No hay contenidos para mostrar."));
    }

    @Test
    public void mostrarErrorAnteponeEtiqueta() throws Exception {
        VistaConsola vista = crearVista("");

        vista.mostrarError("algo falló");

        assertTrue(salida().contains("[ERROR] algo falló"));
    }
}
