package edu.ups.poo.audiovisual.persistencia;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import java.util.List;

abstract class ConversorBase<T extends ContenidoAudiovisual> implements ConversorContenido {

    private final String tipo;
    private final Class<T> clase;
    private final int cantidadCampos;

    ConversorBase(String tipo, Class<T> clase, int cantidadCampos) {
        this.tipo = tipo;
        this.clase = clase;
        this.cantidadCampos = cantidadCampos;
    }

    protected abstract List<String> campos(T contenido);

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public Class<T> getClaseSoportada() {
        return clase;
    }

    @Override
    public int getCantidadCamposEspecificos() {
        return cantidadCampos;
    }

    @Override
    public List<String> aCamposEspecificos(ContenidoAudiovisual contenido) {
        return campos(clase.cast(contenido));
    }

    protected static void exigirDatos(List<String> datos, int esperados, String elemento) {
        if (datos.size() != esperados) {
            throw new IllegalArgumentException("Cada " + elemento + " debe tener " + esperados
                    + " datos separados por ':' y se recibieron " + datos.size() + ".");
        }
    }
}
