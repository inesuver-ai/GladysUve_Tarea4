package edu.ups.poo.audiovisual.modelo;

public class Actor {

    private final String nombre;
    private final String nacionalidad;
    private final int anioNacimiento;

    public Actor(String nombre, String nacionalidad, int anioNacimiento) {
        this.nombre = Validaciones.textoNoVacio(nombre, "nombre del actor");
        this.nacionalidad = Validaciones.textoNoVacio(nacionalidad, "nacionalidad");
        this.anioNacimiento = Validaciones.positivo(anioNacimiento, "año de nacimiento");
    }

    public String getNombre() {
        return nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public int getAnioNacimiento() {
        return anioNacimiento;
    }
}
