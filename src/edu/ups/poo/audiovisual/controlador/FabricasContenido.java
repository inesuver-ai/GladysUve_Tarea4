package edu.ups.poo.audiovisual.controlador;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.Documental;
import edu.ups.poo.audiovisual.modelo.Pelicula;
import edu.ups.poo.audiovisual.modelo.Podcast;
import edu.ups.poo.audiovisual.modelo.ProgramaDeRadio;
import edu.ups.poo.audiovisual.modelo.SerieDeTV;
import java.util.Arrays;
import java.util.List;

public final class FabricasContenido {

    private FabricasContenido() {
    }

    private interface Constructor {
        ContenidoAudiovisual construir(String titulo, int duracion, String genero, List<String> valores);
    }

    private static final class FabricaSimple implements FabricaContenido {

        private final String nombreTipo;
        private final List<String> etiquetas;
        private final Constructor constructor;

        FabricaSimple(String nombreTipo, List<String> etiquetas, Constructor constructor) {
            this.nombreTipo = nombreTipo;
            this.etiquetas = etiquetas;
            this.constructor = constructor;
        }

        @Override
        public String getNombreTipo() {
            return nombreTipo;
        }

        @Override
        public List<String> getEtiquetasCamposEspecificos() {
            return etiquetas;
        }

        @Override
        public ContenidoAudiovisual crear(String titulo, int duracion, String genero, List<String> valores) {
            return constructor.construir(titulo, duracion, genero, valores);
        }
    }

    public static List<FabricaContenido> porDefecto() {
        return Arrays.asList(
                new FabricaSimple("Película", Arrays.asList("Director"),
                        (t, d, g, v) -> new Pelicula(t, d, g, v.get(0))),
                new FabricaSimple("Serie de TV", Arrays.asList("Productor"),
                        (t, d, g, v) -> new SerieDeTV(t, d, g, v.get(0))),
                new FabricaSimple("Documental", Arrays.asList("Tema"),
                        (t, d, g, v) -> new Documental(t, d, g, v.get(0))),
                new FabricaSimple("Podcast",
                        Arrays.asList("Tema principal", "Número de episodio", "Plataforma"),
                        (t, d, g, v) -> new Podcast(t, d, g, v.get(0), aEntero(v.get(1), "número de episodio"),
                                v.get(2))),
                new FabricaSimple("Programa de Radio",
                        Arrays.asList("Locutor", "Frecuencia", "¿En vivo? (si/no)"),
                        (t, d, g, v) -> new ProgramaDeRadio(t, d, g, v.get(0), v.get(1), aBooleano(v.get(2)))));
    }

    private static int aEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser un número entero.", e);
        }
    }

    private static boolean aBooleano(String texto) {
        String limpio = texto.trim().toLowerCase();
        if (limpio.equals("si") || limpio.equals("sí") || limpio.equals("s")) {
            return true;
        }
        if (limpio.equals("no") || limpio.equals("n")) {
            return false;
        }
        throw new IllegalArgumentException("Responda 'si' o 'no' en el campo '¿En vivo?'.");
    }
}
