package edu.ups.poo.audiovisual.modelo;

public class Investigador {

    private final String nombre;
    private final String especialidad;
    private final String institucion;

    public Investigador(String nombre, String especialidad, String institucion) {
        this.nombre = Validaciones.textoNoVacio(nombre, "nombre del investigador");
        this.especialidad = Validaciones.textoNoVacio(especialidad, "especialidad");
        this.institucion = Validaciones.textoNoVacio(institucion, "institución");
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getInstitucion() {
        return institucion;
    }
}
