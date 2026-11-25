package edu.ups.poo.audiovisual.persistencia;

import static edu.ups.poo.audiovisual.persistencia.FormatoCsv.*;

import edu.ups.poo.audiovisual.modelo.Actor;
import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.Pelicula;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConversorPelicula extends ConversorBase<Pelicula> {

    public ConversorPelicula() {
        super("PELICULA", Pelicula.class, 2);
    }

    @Override
    protected List<String> campos(Pelicula pelicula) {
        List<String> actores = new ArrayList<>();
        for (Actor actor : pelicula.getActores()) {
            actores.add(unir(Arrays.asList(escapar(actor.getNombre()), escapar(actor.getNacionalidad()),
                    String.valueOf(actor.getAnioNacimiento())), SEPARADOR_DATO));
        }
        return Arrays.asList(escapar(pelicula.getDirector()), unir(actores, SEPARADOR_ELEMENTO));
    }

    @Override
    public ContenidoAudiovisual crear(String titulo, int duracion, String genero, List<String> campos) {
        Pelicula pelicula = new Pelicula(titulo, duracion, genero, desescapar(campos.get(0)));
        for (String elemento : dividirLista(campos.get(1))) {
            List<String> datos = dividir(elemento, SEPARADOR_DATO);
            exigirDatos(datos, 3, "actor");
            pelicula.agregarActor(new Actor(desescapar(datos.get(0)), desescapar(datos.get(1)),
                    aEntero(datos.get(2), "año de nacimiento")));
        }
        return pelicula;
    }
}
