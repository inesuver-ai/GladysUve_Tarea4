package edu.ups.poo.audiovisual.persistencia;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import java.util.List;

public interface ConversorContenido {

    String getTipo();

    Class<? extends ContenidoAudiovisual> getClaseSoportada();

    int getCantidadCamposEspecificos();

    List<String> aCamposEspecificos(ContenidoAudiovisual contenido);

    ContenidoAudiovisual crear(String titulo, int duracionEnMinutos, String genero,
            List<String> camposEspecificos);
}
