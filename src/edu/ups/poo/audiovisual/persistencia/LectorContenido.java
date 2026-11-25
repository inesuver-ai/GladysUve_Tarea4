package edu.ups.poo.audiovisual.persistencia;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import java.util.List;

public interface LectorContenido {

    List<ContenidoAudiovisual> cargar() throws RepositorioException;
}
