package edu.ups.poo.audiovisual.persistencia;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import java.util.List;

public interface EscritorContenido {

    void guardar(List<ContenidoAudiovisual> contenidos) throws RepositorioException;
}
