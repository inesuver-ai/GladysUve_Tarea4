package edu.ups.poo.audiovisual.vista;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import java.util.List;

public interface Vista {

    void mostrarMensaje(String mensaje);

    void mostrarError(String mensaje);

    void mostrarContenidos(List<ContenidoAudiovisual> contenidos);

    int elegirOpcion(String titulo, List<String> opciones);

    String leerTexto(String etiqueta);

    int leerEntero(String etiqueta);
}
