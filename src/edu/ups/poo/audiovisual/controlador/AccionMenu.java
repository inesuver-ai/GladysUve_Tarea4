package edu.ups.poo.audiovisual.controlador;

import java.util.ArrayList;
import java.util.List;

public enum AccionMenu {
    LISTAR("Listar contenidos"),
    AGREGAR("Agregar contenido"),
    BUSCAR("Buscar por título"),
    ELIMINAR("Eliminar por ID"),
    AGREGAR_ACTOR("Agregar actor a una película"),
    AGREGAR_TEMPORADA("Agregar temporada a una serie"),
    AGREGAR_INVESTIGADOR("Agregar investigador a un documental"),
    GUARDAR("Guardar en archivo"),
    CARGAR("Cargar desde archivo"),
    SALIR("Salir");

    private final String texto;

    AccionMenu(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public static List<String> textosDelMenu() {
        List<String> textos = new ArrayList<>();
        for (AccionMenu accion : values()) {
            if (accion != SALIR) {
                textos.add(accion.texto);
            }
        }
        return textos;
    }

    public static AccionMenu desdeOpcion(int opcion) {
        if (opcion == 0) {
            return SALIR;
        }
        if (opcion < 0 || opcion >= values().length) {
            throw new IllegalArgumentException("Opción de menú inválida: " + opcion);
        }
        return values()[opcion - 1];
    }
}
