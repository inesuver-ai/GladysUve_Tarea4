package edu.ups.poo.audiovisual.modelo;

import java.util.Arrays;
import java.util.List;

public class ProgramaDeRadio extends ContenidoAudiovisual implements Transmisible {

    private String locutor;
    private String frecuencia;
    private boolean enVivo;

    public ProgramaDeRadio(String titulo, int duracionEnMinutos, String genero,
            String locutor, String frecuencia, boolean enVivo) {
        super(titulo, duracionEnMinutos, genero);
        this.locutor = Validaciones.textoNoVacio(locutor, "locutor");
        this.frecuencia = Validaciones.textoNoVacio(frecuencia, "frecuencia");
        this.enVivo = enVivo;
    }

    public String getLocutor() {
        return locutor;
    }

    public void setLocutor(String locutor) {
        this.locutor = Validaciones.textoNoVacio(locutor, "locutor");
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = Validaciones.textoNoVacio(frecuencia, "frecuencia");
    }

    public boolean isEnVivo() {
        return enVivo;
    }

    public void setEnVivo(boolean enVivo) {
        this.enVivo = enVivo;
    }

    @Override
    public String emitirEnVivo() {
        this.enVivo = true;
        return "El programa '" + getTitulo() + "' está en transmisión en vivo.";
    }

    @Override
    public String getNombreTipo() {
        return "Programa de Radio";
    }

    @Override
    protected List<String> detallesEspecificos() {
        return Arrays.asList(
                "Locutor: " + locutor,
                "Frecuencia: " + frecuencia,
                "¿En vivo?: " + (enVivo ? "Sí" : "No"));
    }
}
