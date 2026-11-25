package edu.ups.poo.audiovisual.vista;

public class EntradaFinalizadaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntradaFinalizadaException(String mensaje) {
        super(mensaje);
    }

    public EntradaFinalizadaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
