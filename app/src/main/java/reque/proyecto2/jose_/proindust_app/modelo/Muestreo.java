package reque.proyecto2.jose_.proindust_app.modelo;

public class Muestreo {

    public String idMuestreo, idProyecto, fechaInicio, lapsoInicial, lapsoFinal,
            horaObservacion, tiempoRecorrido, estado, descripcion, cantObservRegistradas, cantObservRequeridas;

    public Muestreo(String idMuestreo, String idProyecto, String fechaInicio, String lapsoInicial,
                    String lapsoFinal, String horaObservacion, String tiempoRecorrido, String estado, String descripcion,
                    String cantObservRegistradas, String cantObservRequeridas)
    {
        this.idMuestreo = idMuestreo;
        this.idProyecto = idProyecto;
        this.fechaInicio = fechaInicio;
        this.lapsoInicial = lapsoInicial;
        this.lapsoFinal = lapsoFinal;
        this.horaObservacion = horaObservacion;
        this.tiempoRecorrido = tiempoRecorrido;
        this.estado = estado;
        this.descripcion = descripcion;
        this.cantObservRegistradas = cantObservRegistradas;
        this.cantObservRequeridas = cantObservRequeridas;
    }

    @Override
    public String toString() {
        return  "idMuestreo: " + idMuestreo + "\n" +
                "idProyecto: " + idProyecto + "\n" +
                "fechaInicio: " + fechaInicio + "\n" +
                "lapsoInicial: " + lapsoInicial + "\n" +
                "lapsoFinal: " + lapsoFinal + "\n" +
                "horaObservacion: " + horaObservacion + "\n" +
                "estado: " + estado + "\n" +
                "descripcion: " + descripcion + "\n" +
                "cantObservRegistradas: " + cantObservRegistradas + "\n" +
                "cantObservRequeridas: " + cantObservRequeridas;
    }
}
