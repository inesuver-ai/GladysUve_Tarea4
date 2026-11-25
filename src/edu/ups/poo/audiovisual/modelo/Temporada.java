package edu.ups.poo.audiovisual.modelo;

public class Temporada {

    private final int numero;
    private final int episodios;

    public Temporada(int numero, int episodios) {
        this.numero = Validaciones.positivo(numero, "número de temporada");
        this.episodios = Validaciones.positivo(episodios, "episodios");
    }

    public int getNumero() {
        return numero;
    }

    public int getEpisodios() {
        return episodios;
    }
}
