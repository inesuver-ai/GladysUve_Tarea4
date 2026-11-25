package edu.ups.poo.audiovisual.persistencia;

import static edu.ups.poo.audiovisual.persistencia.FormatoCsv.*;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.Podcast;
import java.util.Arrays;
import java.util.List;

public class ConversorPodcast extends ConversorBase<Podcast> {

    public ConversorPodcast() {
        super("PODCAST", Podcast.class, 3);
    }

    @Override
    protected List<String> campos(Podcast podcast) {
        return Arrays.asList(escapar(podcast.getTemaPrincipal()),
                String.valueOf(podcast.getNumeroDeEpisodio()), escapar(podcast.getPlataforma()));
    }

    @Override
    public ContenidoAudiovisual crear(String titulo, int duracion, String genero, List<String> campos) {
        return new Podcast(titulo, duracion, genero, desescapar(campos.get(0)),
                aEntero(campos.get(1), "número de episodio"), desescapar(campos.get(2)));
    }
}
