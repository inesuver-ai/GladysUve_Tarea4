package edu.ups.poo.audiovisual.modelo;

final class Validaciones {

    private Validaciones() {
    }

    static String textoNoVacio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' no puede estar vacío.");
        }
        return valor.trim();
    }

    static int positivo(int valor, String campo) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser mayor que cero.");
        }
        return valor;
    }

    static <T> T noNulo(T valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException("El campo '" + campo + "' no puede ser nulo.");
        }
        return valor;
    }
}
