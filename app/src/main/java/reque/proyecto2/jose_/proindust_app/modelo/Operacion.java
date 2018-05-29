package reque.proyecto2.jose_.proindust_app.modelo;

import java.io.Serializable;

public class Operacion implements Serializable {

    public String id, nombre, descripcion;

    public Operacion(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + "\n" +
                "Descripcion: " + descripcion;
    }
}
