package edu.ups.poo.audiovisual;

import edu.ups.poo.audiovisual.controlador.ControladorContenido;
import edu.ups.poo.audiovisual.controlador.FabricasContenido;
import edu.ups.poo.audiovisual.modelo.CatalogoContenido;
import edu.ups.poo.audiovisual.persistencia.RegistroConversores;
import edu.ups.poo.audiovisual.persistencia.RepositorioContenido;
import edu.ups.poo.audiovisual.persistencia.RepositorioCsv;
import edu.ups.poo.audiovisual.vista.Vista;
import edu.ups.poo.audiovisual.vista.VistaConsola;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainAudiovisual {

    private static final String RUTA_POR_DEFECTO = "data/contenidos.csv";

    public static void main(String[] args) {
        Path ruta = Paths.get(args.length > 0 ? args[0] : RUTA_POR_DEFECTO);

        PrintStream salida = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Vista vista = new VistaConsola(System.in, salida);
        RepositorioContenido repositorio = new RepositorioCsv(ruta, RegistroConversores.porDefecto());
        ControladorContenido controlador = new ControladorContenido(
                new CatalogoContenido(), repositorio, vista, FabricasContenido.porDefecto());

        controlador.iniciar();
    }
}
