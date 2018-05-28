package reque.proyecto2.jose_.proindust_app.modelo;

public class Colaborador {

    public String id, pseudonimo, descripcion;

    public Colaborador(String id, String pseudonimo, String descripcion) {
        this.id = id;
        this.pseudonimo = pseudonimo;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Pseudonimo: " + pseudonimo + "\n" +
                "Descripcion: " + descripcion ;
    }
}
