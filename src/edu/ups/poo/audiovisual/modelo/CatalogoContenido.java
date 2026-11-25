package edu.ups.poo.audiovisual.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CatalogoContenido {

    private final List<ContenidoAudiovisual> contenidos = new ArrayList<>();

    public void agregar(ContenidoAudiovisual contenido) {
        contenidos.add(Validaciones.noNulo(contenido, "contenido"));
    }

    public boolean eliminarPorId(int id) {
        return contenidos.removeIf(contenido -> contenido.getId() == id);
    }

    public Optional<ContenidoAudiovisual> buscarPorId(int id) {
        return contenidos.stream().filter(contenido -> contenido.getId() == id).findFirst();
    }

    public List<ContenidoAudiovisual> buscarPorTitulo(String texto) {
        String buscado = Validaciones.textoNoVacio(texto, "texto de búsqueda").toLowerCase(Locale.ROOT);
        List<ContenidoAudiovisual> encontrados = new ArrayList<>();
        for (ContenidoAudiovisual contenido : contenidos) {
            if (contenido.getTitulo().toLowerCase(Locale.ROOT).contains(buscado)) {
                encontrados.add(contenido);
            }
        }
        return encontrados;
    }

    public void reemplazarContenidos(Collection<? extends ContenidoAudiovisual> nuevos) {
        List<ContenidoAudiovisual> copia = new ArrayList<>(nuevos);
        contenidos.clear();
        contenidos.addAll(copia);
    }

    public List<ContenidoAudiovisual> listar() {
        return Collections.unmodifiableList(contenidos);
    }

    public int cantidad() {
        return contenidos.size();
    }
}
