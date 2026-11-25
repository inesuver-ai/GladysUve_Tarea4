package edu.ups.poo.audiovisual.persistencia;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import java.util.HashMap;
import java.util.Map;

public class RegistroConversores {

    private final Map<String, ConversorContenido> porTipo = new HashMap<>();
    private final Map<Class<?>, ConversorContenido> porClase = new HashMap<>();

    public static RegistroConversores porDefecto() {
        RegistroConversores registro = new RegistroConversores();
        registro.registrar(new ConversorPelicula());
        registro.registrar(new ConversorSerieDeTV());
        registro.registrar(new ConversorDocumental());
        registro.registrar(new ConversorPodcast());
        registro.registrar(new ConversorProgramaDeRadio());
        return registro;
    }

    public void registrar(ConversorContenido conversor) {
        porTipo.put(conversor.getTipo(), conversor);
        porClase.put(conversor.getClaseSoportada(), conversor);
    }

    public ConversorContenido paraTipo(String tipo) {
        ConversorContenido conversor = porTipo.get(tipo.trim().toUpperCase());
        if (conversor == null) {
            throw new IllegalArgumentException("Tipo de contenido desconocido: '" + tipo + "'.");
        }
        return conversor;
    }

    public ConversorContenido paraContenido(ContenidoAudiovisual contenido) {
        ConversorContenido conversor = porClase.get(contenido.getClass());
        if (conversor == null) {
            throw new IllegalArgumentException("No hay conversor para la clase "
                    + contenido.getClass().getSimpleName() + ".");
        }
        return conversor;
    }
}
