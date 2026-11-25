package edu.ups.poo.audiovisual.persistencia;

import java.util.ArrayList;
import java.util.List;

final class FormatoCsv {

    static final char SEPARADOR_CAMPO = ';';
    static final char SEPARADOR_ELEMENTO = '|';
    static final char SEPARADOR_DATO = ':';
    private static final char ESCAPE = '\\';

    private FormatoCsv() {
    }

    static String escapar(String texto) {
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            if (c == '\n') {
                resultado.append(ESCAPE).append('n');
            } else if (c == '\r') {
                continue;
            } else {
                if (esCaracterEspecial(c)) {
                    resultado.append(ESCAPE);
                }
                resultado.append(c);
            }
        }
        return resultado.toString();
    }

    static String desescapar(String texto) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == ESCAPE && i + 1 < texto.length()) {
                char siguiente = texto.charAt(++i);
                resultado.append(siguiente == 'n' ? '\n' : siguiente);
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }

    static List<String> dividir(String texto, char separador) {
        List<String> partes = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == ESCAPE && i + 1 < texto.length()) {
                actual.append(c).append(texto.charAt(++i));
            } else if (c == separador) {
                partes.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        partes.add(actual.toString());
        return partes;
    }

    static List<String> dividirLista(String texto) {
        if (texto.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return dividir(texto, SEPARADOR_ELEMENTO);
    }

    static String unir(List<String> partes, char separador) {
        return String.join(String.valueOf(separador), partes);
    }

    static int aEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El campo '" + campo + "' debe ser un número entero y se recibió '" + texto + "'.", e);
        }
    }

    static boolean aBooleano(String texto, String campo) {
        String limpio = texto.trim();
        if ("true".equalsIgnoreCase(limpio)) {
            return true;
        }
        if ("false".equalsIgnoreCase(limpio)) {
            return false;
        }
        throw new IllegalArgumentException(
                "El campo '" + campo + "' debe ser true o false y se recibió '" + texto + "'.");
    }

    private static boolean esCaracterEspecial(char c) {
        return c == ESCAPE || c == SEPARADOR_CAMPO || c == SEPARADOR_ELEMENTO || c == SEPARADOR_DATO;
    }
}
