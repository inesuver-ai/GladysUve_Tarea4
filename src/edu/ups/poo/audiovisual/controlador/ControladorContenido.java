package edu.ups.poo.audiovisual.controlador;

import edu.ups.poo.audiovisual.modelo.Actor;
import edu.ups.poo.audiovisual.modelo.CatalogoContenido;
import edu.ups.poo.audiovisual.modelo.ContenidoAudiovisual;
import edu.ups.poo.audiovisual.modelo.Documental;
import edu.ups.poo.audiovisual.modelo.Investigador;
import edu.ups.poo.audiovisual.modelo.Pelicula;
import edu.ups.poo.audiovisual.modelo.SerieDeTV;
import edu.ups.poo.audiovisual.persistencia.RepositorioContenido;
import edu.ups.poo.audiovisual.persistencia.RepositorioException;
import edu.ups.poo.audiovisual.vista.EntradaFinalizadaException;
import edu.ups.poo.audiovisual.vista.Vista;
import java.util.ArrayList;
import java.util.List;

public class ControladorContenido {

    private final CatalogoContenido catalogo;
    private final RepositorioContenido repositorio;
    private final Vista vista;
    private final List<FabricaContenido> fabricas;

    public ControladorContenido(CatalogoContenido catalogo, RepositorioContenido repositorio,
            Vista vista, List<FabricaContenido> fabricas) {
        this.catalogo = catalogo;
        this.repositorio = repositorio;
        this.vista = vista;
        this.fabricas = fabricas;
    }

    public void iniciar() {
        vista.mostrarMensaje(" Catálogo de Contenido Audiovisual ");
        try {
            cargarContenidos();
            AccionMenu accion;
            do {
                accion = pedirAccion();
                ejecutar(accion);
            } while (accion != AccionMenu.SALIR);
        } catch (EntradaFinalizadaException e) {
            vista.mostrarMensaje("Entrada finalizada. Hasta luego.");
        }
    }

    public void ejecutar(AccionMenu accion) {
        try {
            switch (accion) {
                case LISTAR -> listarContenidos();
                case AGREGAR -> agregarContenido();
                case BUSCAR -> buscarPorTitulo();
                case ELIMINAR -> eliminarContenido();
                case AGREGAR_ACTOR -> agregarActor();
                case AGREGAR_TEMPORADA -> agregarTemporada();
                case AGREGAR_INVESTIGADOR -> agregarInvestigador();
                case GUARDAR -> guardarContenidos();
                case CARGAR -> cargarContenidos();
                case SALIR -> vista.mostrarMensaje("Hasta luego.");
            }
        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void listarContenidos() {
        vista.mostrarContenidos(catalogo.listar());
    }

    public void agregarContenido() {
        int indice = vista.elegirOpcion("Tipo de contenido", nombresDeTipos());
        if (indice == 0) {
            return;
        }
        FabricaContenido fabrica = fabricas.get(indice - 1);
        String titulo = vista.leerTexto("Título");
        int duracion = vista.leerEntero("Duración en minutos");
        String genero = vista.leerTexto("Género");
        List<String> valores = new ArrayList<>();
        for (String etiqueta : fabrica.getEtiquetasCamposEspecificos()) {
            valores.add(vista.leerTexto(etiqueta));
        }
        ContenidoAudiovisual contenido = fabrica.crear(titulo, duracion, genero, valores);
        catalogo.agregar(contenido);
        vista.mostrarMensaje(fabrica.getNombreTipo() + " agregado con ID " + contenido.getId() + ".");
    }

    public void buscarPorTitulo() {
        String texto = vista.leerTexto("Texto a buscar en el título");
        List<ContenidoAudiovisual> encontrados = catalogo.buscarPorTitulo(texto);
        vista.mostrarContenidos(encontrados);
    }

    public void eliminarContenido() {
        int id = vista.leerEntero("ID del contenido a eliminar");
        if (catalogo.eliminarPorId(id)) {
            vista.mostrarMensaje("Contenido " + id + " eliminado.");
        } else {
            vista.mostrarError("No existe un contenido con ID " + id + ".");
        }
    }

    public void agregarActor() {
        Pelicula pelicula = buscarContenidoDeTipo(vista.leerEntero("ID de la película"), Pelicula.class, "película");
        String nombre = vista.leerTexto("Nombre del actor");
        String nacionalidad = vista.leerTexto("Nacionalidad");
        int anioNacimiento = vista.leerEntero("Año de nacimiento");
        pelicula.agregarActor(new Actor(nombre, nacionalidad, anioNacimiento));
        vista.mostrarMensaje("Actor agregado a '" + pelicula.getTitulo() + "'.");
    }

    public void agregarTemporada() {
        SerieDeTV serie = buscarContenidoDeTipo(vista.leerEntero("ID de la serie"), SerieDeTV.class, "serie");
        int numero = vista.leerEntero("Número de temporada");
        int episodios = vista.leerEntero("Cantidad de episodios");
        serie.agregarTemporada(numero, episodios);
        vista.mostrarMensaje("Temporada agregada a '" + serie.getTitulo() + "'.");
    }

    public void agregarInvestigador() {
        Documental documental = buscarContenidoDeTipo(vista.leerEntero("ID del documental"),
                Documental.class, "documental");
        String nombre = vista.leerTexto("Nombre del investigador");
        String especialidad = vista.leerTexto("Especialidad");
        String institucion = vista.leerTexto("Institución");
        documental.agregarInvestigador(new Investigador(nombre, especialidad, institucion));
        vista.mostrarMensaje("Investigador agregado a '" + documental.getTitulo() + "'.");
    }

    public void guardarContenidos() {
        try {
            repositorio.guardar(catalogo.listar());
            vista.mostrarMensaje("Se guardaron " + catalogo.cantidad() + " contenidos.");
        } catch (RepositorioException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void cargarContenidos() {
        try {
            catalogo.reemplazarContenidos(repositorio.cargar());
            vista.mostrarMensaje("Se cargaron " + catalogo.cantidad() + " contenidos.");
        } catch (RepositorioException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private AccionMenu pedirAccion() {
        int opcion = vista.elegirOpcion("Menú principal", AccionMenu.textosDelMenu());
        return AccionMenu.desdeOpcion(opcion);
    }

    private List<String> nombresDeTipos() {
        List<String> nombres = new ArrayList<>();
        for (FabricaContenido fabrica : fabricas) {
            nombres.add(fabrica.getNombreTipo());
        }
        return nombres;
    }

    private <T extends ContenidoAudiovisual> T buscarContenidoDeTipo(int id, Class<T> tipo, String descripcion) {
        return catalogo.buscarPorId(id)
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe una " + descripcion + " con ID " + id + "."));
    }
}
