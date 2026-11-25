package edu.ups.poo.audiovisual.controlador;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import java.util.List;


public interface FabricaContenido {

    String getNombreTipo();

    List<String> getEtiquetasCamposEspecificos();

    ContenidoAudiovisual crear(String titulo, int duracionEnMinutos, String genero, List<String> valores);
}
