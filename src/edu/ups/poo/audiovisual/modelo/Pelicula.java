package edu.ups.poo.audiovisual.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pelicula extends ContenidoAudiovisual {

    private String director;
    private final List<Actor> actores = new ArrayList<>();

    public Pelicula(String titulo, int duracionEnMinutos, String genero, String director) {
        super(titulo, duracionEnMinutos, genero);
        this.director = Validaciones.textoNoVacio(director, "director");
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = Validaciones.textoNoVacio(director, "director");
    }

    public void agregarActor(Actor actor) {
        actores.add(Validaciones.noNulo(actor, "actor"));
    }

    public List<Actor> getActores() {
        return Collections.unmodifiableList(actores);
    }

    @Override
    public String getNombreTipo() {
        return "Película";
    }

    @Override
    protected List<String> detallesEspecificos() {
        List<String> lineas = new ArrayList<>();
        lineas.add("Director: " + director);
        lineas.add("Actores:");
        for (Actor actor : actores) {
            lineas.add("  - " + actor.getNombre());
        }
        return lineas;
    }
}
