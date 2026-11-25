package edu.ups.poo.audiovisual.persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FormatoCsvTest {

    @Test
    public void escaparYDesescaparSonInversos() {
        String original = "a;b|c:d\\e\nf";

        assertEquals(original, FormatoCsv.desescapar(FormatoCsv.escapar(original)));
    }

    @Test
    public void escaparNoDejaSeparadoresSinProteger() {
        String escapado = FormatoCsv.escapar("uno;dos");

        assertEquals(1, FormatoCsv.dividir(escapado, ';').size());
    }

    @Test
    public void dividirConservaCamposVaciosAlFinal() {
        List<String> partes = FormatoCsv.dividir("a;b;", ';');

        assertEquals(Arrays.asList("a", "b", ""), partes);
    }

    @Test
    public void dividirIgnoraSeparadoresEscapados() {
        List<String> partes = FormatoCsv.dividir("a\\;b;c", ';');

        assertEquals(2, partes.size());
        assertEquals("a;b", FormatoCsv.desescapar(partes.get(0)));
    }

    @Test
    public void dividirListaVaciaDevuelveListaVacia() {
        assertTrue(FormatoCsv.dividirLista("").isEmpty());
        assertTrue(FormatoCsv.dividirLista("   ").isEmpty());
    }

    @Test
    public void dividirListaSeparaPorElementos() {
        assertEquals(2, FormatoCsv.dividirLista("a:1|b:2").size());
    }

    @Test
    public void unirUsaElSeparadorIndicado() {
        assertEquals("a|b|c", FormatoCsv.unir(Arrays.asList("a", "b", "c"), '|'));
    }

    @Test
    public void aEnteroConvierteTextoConEspacios() {
        assertEquals(42, FormatoCsv.aEntero(" 42 ", "campo"));
    }

    @Test
    public void aEnteroInformaElCampoCuandoFalla() {
        try {
            FormatoCsv.aEntero("abc", "duración");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("duración"));
            assertTrue(e.getMessage().contains("abc"));
            return;
        }
        throw new AssertionError("Debió lanzar IllegalArgumentException");
    }

    @Test
    public void aBooleanoAceptaTrueYFalseSinImportarMayusculas() {
        assertTrue(FormatoCsv.aBooleano("TRUE", "x"));
        assertFalse(FormatoCsv.aBooleano(" false ", "x"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void aBooleanoRechazaOtrosValores() {
        FormatoCsv.aBooleano("quizá", "x");
    }
}
