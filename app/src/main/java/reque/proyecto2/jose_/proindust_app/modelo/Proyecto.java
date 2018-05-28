package reque.proyecto2.jose_.proindust_app.modelo;

public class Proyecto {

    public String id, nombre, descripcion, nivelConfianza, rangoInicial, rangoFinal, cantMuestreosP, tiempoRecorrido;

    public Proyecto(String id, String nombre, String descripcion, String nivelConfianza, String rangoInicial, String rangoFinal, String cantMuestreosP, String tiempoRecorrido) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivelConfianza = nivelConfianza;
        this.rangoInicial = rangoInicial;
        this.rangoFinal = rangoFinal;
        this.cantMuestreosP = cantMuestreosP;
        this.tiempoRecorrido = tiempoRecorrido;
    }

    public Proyecto(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
