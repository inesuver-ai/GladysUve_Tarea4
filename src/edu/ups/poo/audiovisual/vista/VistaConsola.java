package edu.ups.poo.audiovisual.vista;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VistaConsola implements Vista {

    private final BufferedReader entrada;
    private final PrintStream salida;

    public VistaConsola(InputStream entrada, PrintStream salida) {
        this.entrada = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8));
        this.salida = salida;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        salida.println(mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        salida.println("[ERROR] " + mensaje);
    }

    @Override
    public void mostrarContenidos(List<ContenidoAudiovisual> contenidos) {
        if (contenidos.isEmpty()) {
            salida.println("No hay contenidos para mostrar.");
            return;
        }
        for (ContenidoAudiovisual contenido : contenidos) {
            salida.println("[ID " + contenido.getId() + "]");
            salida.println(contenido.obtenerDetalles());
            salida.println();
        }
    }

    @Override
    public int elegirOpcion(String titulo, List<String> opciones) {
        while (true) {
            imprimirOpciones(titulo, opciones);
            String texto = leerLinea("Opción");
            try {
                int opcion = Integer.parseInt(texto.trim());
                if (opcion >= 0 && opcion <= opciones.size()) {
                    return opcion;
                }
            } catch (NumberFormatException e) {
                // Se informa abajo, junto con el caso de número fuera de rango.
            }
            mostrarError("Elija un número entre 0 y " + opciones.size() + ".");
        }
    }

    @Override
    public String leerTexto(String etiqueta) {
        return leerLinea(etiqueta).trim();
    }

    @Override
    public int leerEntero(String etiqueta) {
        while (true) {
            String texto = leerLinea(etiqueta);
            try {
                return Integer.parseInt(texto.trim());
            } catch (NumberFormatException e) {
                mostrarError("Ingrese un número entero válido.");
            }
        }
    }

    private void imprimirOpciones(String titulo, List<String> opciones) {
        salida.println();
        salida.println("--- " + titulo + " ---");
        for (int i = 0; i < opciones.size(); i++) {
            salida.println((i + 1) + ". " + opciones.get(i));
        }
        salida.println("0. Salir / Cancelar");
    }

    private String leerLinea(String etiqueta) {
        salida.print(etiqueta + ": ");
        salida.flush();
        try {
            String linea = entrada.readLine();
            if (linea == null) {
                throw new EntradaFinalizadaException("No hay más datos de entrada.");
            }
            return linea;
        } catch (IOException e) {
            throw new EntradaFinalizadaException("No se pudo leer la entrada.", e);
        }
    }
}
