package edu.ups.poo.audiovisual.modelo;

import java.util.Arrays;
import java.util.List;

public class Podcast extends ContenidoAudiovisual implements Compartible {

    private String temaPrincipal;
    private int numeroDeEpisodio;
    private String plataforma;

    public Podcast(String titulo, int duracionEnMinutos, String genero,
            String temaPrincipal, int numeroDeEpisodio, String plataforma) {
        super(titulo, duracionEnMinutos, genero);
        this.temaPrincipal = Validaciones.textoNoVacio(temaPrincipal, "tema principal");
        this.numeroDeEpisodio = Validaciones.positivo(numeroDeEpisodio, "número de episodio");
        this.plataforma = Validaciones.textoNoVacio(plataforma, "plataforma");
    }

    public String getTemaPrincipal() {
        return temaPrincipal;
    }

    public void setTemaPrincipal(String temaPrincipal) {
        this.temaPrincipal = Validaciones.textoNoVacio(temaPrincipal, "tema principal");
    }

    public int getNumeroDeEpisodio() {
        return numeroDeEpisodio;
    }

    public void setNumeroDeEpisodio(int numeroDeEpisodio) {
        this.numeroDeEpisodio = Validaciones.positivo(numeroDeEpisodio, "número de episodio");
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = Validaciones.textoNoVacio(plataforma, "plataforma");
    }

    @Override
    public String compartir() {
        return "Compartiendo el episodio " + numeroDeEpisodio + " en " + plataforma + "... ¡Listo!";
    }

    @Override
    public String getNombreTipo() {
        return "Podcast";
    }

    @Override
    protected List<String> detallesEspecificos() {
        return Arrays.asList(
                "Tema principal: " + temaPrincipal,
                "Número de episodio: " + numeroDeEpisodio,
                "Plataforma: " + plataforma);
    }
}
