package reque.proyecto2.jose_.proindust_app.modelo;

public class Tarea {

    public String id, nombre, descripcion, idActividad;

    public Tarea(String id, String nombre, String descripcion, String idActividad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idActividad = idActividad;
    }

    public Tarea(String pId, String nombre)
    {
        this.id = pId;
        this.nombre = nombre;
    }
}
