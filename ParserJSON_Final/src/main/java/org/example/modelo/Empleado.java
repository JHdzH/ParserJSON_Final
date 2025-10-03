package org.example.modelo;

import java.util.List;

public class Empleado {
    private String nombre;
    private String apellido;
    private int edad;
    private Direccion dir;
    private List<Telefono> telefonos;

    public Empleado() {
    }

    public Empleado(String nombre, String apellido, int edad, Direccion dir, List<Telefono> telefonos) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.dir = dir;
        this.telefonos = telefonos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Direccion getDir() {
        return dir;
    }

    public void setDir(Direccion dir) {
        this.dir = dir;
    }

    public List<Telefono> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<Telefono> telefonos) {
        this.telefonos = telefonos;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  \"nombre\": \"" + nombre + "\",\n" +
                "  \"apellido\": \"" + apellido + "\",\n" +
                "  \"edad\": " + edad + ",\n" +
                "  \"dir\": " + dir + ",\n" +
                "  \"telefonos\": " + telefonos + "\n" +
                "}";
    }


}