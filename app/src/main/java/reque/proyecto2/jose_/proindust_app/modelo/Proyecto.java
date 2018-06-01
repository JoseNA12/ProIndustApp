package reque.proyecto2.jose_.proindust_app.modelo;

import java.io.Serializable;

public class Proyecto implements Serializable {

    public String id, nombre, descripcion;

    public Proyecto(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Proyecto(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + "\n" +
                "Descripcion: " + descripcion + "\n";
    }
}
