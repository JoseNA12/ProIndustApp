package reque.proyecto2.jose_.proindust_app.modelo;

import java.io.Serializable;

public class Tarea implements Serializable {

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

    @Override
    public String toString() {
        return "Nombre: " + nombre + "\n" +
                "Descripcion: " + descripcion + "\n" +
                "Actividad: " + getActividad(idActividad) + "\n";
    }

    private String getActividad(String pId)
    {
        String var = "";
        if (pId.equals("1")) { var = "PRODUCTIVA"; }
        else
        {
            if (pId.equals("2")) {   var = "COLABORATIVA"; }
            else { var = "IMPRODUCTIVA"; }
        }
        return var;
    }
}
