package edu.ups.poo.audiovisual.persistencia;

import static edu.ups.poo.audiovisual.persistencia.FormatoCsv.*;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.Documental;
import edu.ups.poo.audiovisual.modelo.Investigador;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConversorDocumental extends ConversorBase<Documental> {

    public ConversorDocumental() {
        super("DOCUMENTAL", Documental.class, 2);
    }

    @Override
    protected List<String> campos(Documental documental) {
        List<String> investigadores = new ArrayList<>();
        for (Investigador investigador : documental.getInvestigadores()) {
            investigadores.add(unir(Arrays.asList(escapar(investigador.getNombre()),
                    escapar(investigador.getEspecialidad()), escapar(investigador.getInstitucion())),
                    SEPARADOR_DATO));
        }
        return Arrays.asList(escapar(documental.getTema()), unir(investigadores, SEPARADOR_ELEMENTO));
    }

    @Override
    public ContenidoAudiovisual crear(String titulo, int duracion, String genero, List<String> campos) {
        Documental documental = new Documental(titulo, duracion, genero, desescapar(campos.get(0)));
        for (String elemento : dividirLista(campos.get(1))) {
            List<String> datos = dividir(elemento, SEPARADOR_DATO);
            exigirDatos(datos, 3, "investigador");
            documental.agregarInvestigador(new Investigador(desescapar(datos.get(0)),
                    desescapar(datos.get(1)), desescapar(datos.get(2))));
        }
        return documental;
    }
}
