package reque.proyecto2.jose_.proindust_app.modelo;

import java.io.Serializable;

public class Proyecto implements Serializable {

    public String id, nombre, descripcion, nivelConfianza, rangoInicial, rangoFinal, cantMuestreosP, tiempoRecorrido, estado;

    public Proyecto(String id, String nombre, String descripcion, String nivelConfianza, String rangoInicial, String rangoFinal, String cantMuestreosP, String tiempoRecorrido, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivelConfianza = nivelConfianza;
        this.rangoInicial = rangoInicial;
        this.rangoFinal = rangoFinal;
        this.cantMuestreosP = cantMuestreosP;
        this.tiempoRecorrido = tiempoRecorrido;
        this.estado = estado;
    }

    public Proyecto(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + "\n" +
                "Descripcion: " + descripcion + "\n" +
                "Nivel de confianza: " + nivelConfianza + "\n" +
                "Rango Inicial(min): " + rangoInicial + "\n" +
                "Rango Final(min): " + rangoFinal + "\n" +
                "Cant. de Muestreos pre.: " + cantMuestreosP + "\n" +
                "Tiempo del recorrido(min): " + tiempoRecorrido + "\n" +
                "Estado: " + estado;
    }
}
