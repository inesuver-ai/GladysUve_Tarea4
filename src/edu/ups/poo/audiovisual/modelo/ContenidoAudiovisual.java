package edu.ups.poo.audiovisual.modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class ContenidoAudiovisual {

    private static int ultimoId = 0;

    private final int id;
    private String titulo;
    private int duracionEnMinutos;
    private String genero;

    protected ContenidoAudiovisual(String titulo, int duracionEnMinutos, String genero) {
        this.titulo = Validaciones.textoNoVacio(titulo, "título");
        this.duracionEnMinutos = Validaciones.positivo(duracionEnMinutos, "duración");
        this.genero = Validaciones.textoNoVacio(genero, "género");
        this.id = ++ultimoId;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = Validaciones.textoNoVacio(titulo, "título");
    }

    public int getDuracionEnMinutos() {
        return duracionEnMinutos;
    }

    public void setDuracionEnMinutos(int duracionEnMinutos) {
        this.duracionEnMinutos = Validaciones.positivo(duracionEnMinutos, "duración");
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = Validaciones.textoNoVacio(genero, "género");
    }

    public abstract String getNombreTipo();

    protected abstract List<String> detallesEspecificos();

    protected String getEtiquetaDuracion() {
        return "Duración";
    }

    public final String obtenerDetalles() {
        List<String> lineas = new ArrayList<>();
        lineas.add("=== " + getNombreTipo() + " ===");
        lineas.add("Título: " + titulo);
        lineas.add(getEtiquetaDuracion() + ": " + duracionEnMinutos + " min");
        lineas.add("Género: " + genero);
        lineas.addAll(detallesEspecificos());
        return String.join(System.lineSeparator(), lineas);
    }
}
