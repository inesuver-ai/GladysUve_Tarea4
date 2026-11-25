package edu.ups.poo.audiovisual.persistencia;

import static edu.ups.poo.audiovisual.persistencia.FormatoCsv.*;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.SerieDeTV;
import edu.ups.poo.audiovisual.modelo.Temporada;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConversorSerieDeTV extends ConversorBase<SerieDeTV> {

    public ConversorSerieDeTV() {
        super("SERIE", SerieDeTV.class, 2);
    }

    @Override
    protected List<String> campos(SerieDeTV serie) {
        List<String> temporadas = new ArrayList<>();
        for (Temporada temporada : serie.getTemporadas()) {
            temporadas.add(unir(Arrays.asList(String.valueOf(temporada.getNumero()),
                    String.valueOf(temporada.getEpisodios())), SEPARADOR_DATO));
        }
        return Arrays.asList(escapar(serie.getProductor()), unir(temporadas, SEPARADOR_ELEMENTO));
    }

    @Override
    public ContenidoAudiovisual crear(String titulo, int duracion, String genero, List<String> campos) {
        SerieDeTV serie = new SerieDeTV(titulo, duracion, genero, desescapar(campos.get(0)));
        for (String elemento : dividirLista(campos.get(1))) {
            List<String> datos = dividir(elemento, SEPARADOR_DATO);
            exigirDatos(datos, 2, "temporada");
            serie.agregarTemporada(aEntero(datos.get(0), "número de temporada"),
                    aEntero(datos.get(1), "episodios"));
        }
        return serie;
    }
}
