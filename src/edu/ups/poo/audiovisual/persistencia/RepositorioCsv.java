package edu.ups.poo.audiovisual.persistencia;

import static edu.ups.poo.audiovisual.persistencia.FormatoCsv.SEPARADOR_CAMPO;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RepositorioCsv implements RepositorioContenido {

    private static final String PREFIJO_COMENTARIO = "#";
    private static final int CAMPOS_COMUNES = 4;

    private final Path ruta;
    private final RegistroConversores conversores;

    public RepositorioCsv(Path ruta, RegistroConversores conversores) {
        this.ruta = ruta;
        this.conversores = conversores;
    }

    @Override
    public List<ContenidoAudiovisual> cargar() throws RepositorioException {
        List<ContenidoAudiovisual> contenidos = new ArrayList<>();
        try (BufferedReader lector = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
            String linea;
            int numeroDeLinea = 0;
            while ((linea = lector.readLine()) != null) {
                numeroDeLinea++;
                if (!esLineaIgnorable(linea)) {
                    contenidos.add(interpretarLinea(linea, numeroDeLinea));
                }
            }
        } catch (NoSuchFileException e) {
            throw new RepositorioException("No existe el archivo: " + ruta, e);
        } catch (IOException e) {
            throw new RepositorioException("No se pudo leer el archivo " + ruta + ": " + e.getMessage(), e);
        }
        return contenidos;
    }

    @Override
    public void guardar(List<ContenidoAudiovisual> contenidos) throws RepositorioException {
        Path temporal = ruta.resolveSibling(ruta.getFileName() + ".tmp");
        try {
            crearCarpetaPadre();
            escribirArchivo(temporal, contenidos);
            Files.move(temporal, ruta, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | IllegalArgumentException e) {
            borrarSilenciosamente(temporal);
            throw new RepositorioException("No se pudo guardar el archivo " + ruta + ": " + e.getMessage(), e);
        }
    }

    private void escribirArchivo(Path destino, List<ContenidoAudiovisual> contenidos) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            escritor.write("# TIPO;titulo;duracionEnMinutos;genero;campos específicos del tipo");
            escritor.newLine();
            for (ContenidoAudiovisual contenido : contenidos) {
                escritor.write(construirLinea(contenido));
                escritor.newLine();
            }
        }
    }

    private String construirLinea(ContenidoAudiovisual contenido) {
        ConversorContenido conversor = conversores.paraContenido(contenido);
        List<String> campos = new ArrayList<>(Arrays.asList(
                conversor.getTipo(),
                FormatoCsv.escapar(contenido.getTitulo()),
                String.valueOf(contenido.getDuracionEnMinutos()),
                FormatoCsv.escapar(contenido.getGenero())));
        campos.addAll(conversor.aCamposEspecificos(contenido));
        return FormatoCsv.unir(campos, SEPARADOR_CAMPO);
    }

    private ContenidoAudiovisual interpretarLinea(String linea, int numeroDeLinea) throws RepositorioException {
        try {
            List<String> campos = FormatoCsv.dividir(linea, SEPARADOR_CAMPO);
            if (campos.size() < CAMPOS_COMUNES) {
                throw new IllegalArgumentException("Faltan campos obligatorios.");
            }
            ConversorContenido conversor = conversores.paraTipo(campos.get(0));
            exigirCantidadDeCampos(campos, conversor);
            return conversor.crear(
                    FormatoCsv.desescapar(campos.get(1)),
                    FormatoCsv.aEntero(campos.get(2), "duración"),
                    FormatoCsv.desescapar(campos.get(3)),
                    campos.subList(CAMPOS_COMUNES, campos.size()));
        } catch (IllegalArgumentException e) {
            throw new RepositorioException("Línea " + numeroDeLinea + " inválida: " + e.getMessage(), e);
        }
    }

    private void exigirCantidadDeCampos(List<String> campos, ConversorContenido conversor) {
        int esperados = CAMPOS_COMUNES + conversor.getCantidadCamposEspecificos();
        if (campos.size() != esperados) {
            throw new IllegalArgumentException("Se esperaban " + esperados + " campos para "
                    + conversor.getTipo() + " y hay " + campos.size() + ".");
        }
    }

    private boolean esLineaIgnorable(String linea) {
        String limpia = linea.trim();
        return limpia.isEmpty() || limpia.startsWith(PREFIJO_COMENTARIO);
    }

    private void crearCarpetaPadre() throws IOException {
        Path padre = ruta.toAbsolutePath().getParent();
        if (padre != null) {
            Files.createDirectories(padre);
        }
    }

    private void borrarSilenciosamente(Path archivo) {
        try {
            Files.deleteIfExists(archivo);
        } catch (IOException ignorada) {
            // Solo limpieza del temporal: el error importante ya se informa al llamador.
        }
    }
}
