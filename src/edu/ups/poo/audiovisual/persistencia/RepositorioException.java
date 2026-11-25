package edu.ups.poo.audiovisual.persistencia;

public class RepositorioException extends Exception {

    private static final long serialVersionUID = 1L;

    public RepositorioException(String mensaje) {
        super(mensaje);
    }

    public RepositorioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
