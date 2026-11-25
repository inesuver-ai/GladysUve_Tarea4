package edu.ups.poo.audiovisual.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Documental extends ContenidoAudiovisual {

    private String tema;
    private final List<Investigador> investigadores = new ArrayList<>();

    public Documental(String titulo, int duracionEnMinutos, String genero, String tema) {
        super(titulo, duracionEnMinutos, genero);
        this.tema = Validaciones.textoNoVacio(tema, "tema");
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = Validaciones.textoNoVacio(tema, "tema");
    }

    public void agregarInvestigador(Investigador investigador) {
        investigadores.add(Validaciones.noNulo(investigador, "investigador"));
    }

    public List<Investigador> getInvestigadores() {
        return Collections.unmodifiableList(investigadores);
    }

    @Override
    public String getNombreTipo() {
        return "Documental";
    }

    @Override
    protected List<String> detallesEspecificos() {
        List<String> lineas = new ArrayList<>();
        lineas.add("Tema: " + tema);
        lineas.add("Investigadores:");
        for (Investigador investigador : investigadores) {
            lineas.add("  - " + investigador.getNombre()
                    + " (" + investigador.getEspecialidad() + ", " + investigador.getInstitucion() + ")");
        }
        return lineas;
    }
}
