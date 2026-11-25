package edu.ups.poo.audiovisual.persistencia;

import static edu.ups.poo.audiovisual.persistencia.FormatoCsv.*;

import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.ProgramaDeRadio;
import java.util.Arrays;
import java.util.List;

public class ConversorProgramaDeRadio extends ConversorBase<ProgramaDeRadio> {

    public ConversorProgramaDeRadio() {
        super("RADIO", ProgramaDeRadio.class, 3);
    }

    @Override
    protected List<String> campos(ProgramaDeRadio programa) {
        return Arrays.asList(escapar(programa.getLocutor()), escapar(programa.getFrecuencia()),
                String.valueOf(programa.isEnVivo()));
    }

    @Override
    public ContenidoAudiovisual crear(String titulo, int duracion, String genero, List<String> campos) {
        return new ProgramaDeRadio(titulo, duracion, genero, desescapar(campos.get(0)),
                desescapar(campos.get(1)), aBooleano(campos.get(2), "en vivo"));
    }
}
