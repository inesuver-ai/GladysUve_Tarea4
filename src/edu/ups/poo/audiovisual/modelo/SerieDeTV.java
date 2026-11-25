package edu.ups.poo.audiovisual.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerieDeTV extends ContenidoAudiovisual {

    private String productor;
    private final List<Temporada> temporadas = new ArrayList<>();

    public SerieDeTV(String titulo, int duracionEnMinutos, String genero, String productor) {
        super(titulo, duracionEnMinutos, genero);
        this.productor = Validaciones.textoNoVacio(productor, "productor");
    }

    public String getProductor() {
        return productor;
    }

    public void setProductor(String productor) {
        this.productor = Validaciones.textoNoVacio(productor, "productor");
    }

    public void agregarTemporada(int numero, int episodios) {
        temporadas.add(new Temporada(numero, episodios));
    }

    public List<Temporada> getTemporadas() {
        return Collections.unmodifiableList(temporadas);
    }

    public int getTotalEpisodios() {
        int total = 0;
        for (Temporada temporada : temporadas) {
            total += temporada.getEpisodios();
        }
        return total;
    }

    @Override
    public String getNombreTipo() {
        return "Serie de TV";
    }

    @Override
    protected String getEtiquetaDuracion() {
        return "Duración por episodio";
    }

    @Override
    protected List<String> detallesEspecificos() {
        List<String> lineas = new ArrayList<>();
        lineas.add("Productor: " + productor);
        lineas.add("Temporadas: " + temporadas.size());
        lineas.add("Episodios en total: " + getTotalEpisodios());
        return lineas;
    }
}
