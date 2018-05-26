package reque.proyecto2.jose_.proindust_app.modelo;

public class Usuario {

    public String id, nombre, apellidos, idRolUsuario, nombreUsuario, correo, constrasenia;

    public Usuario(String id, String nombre, String apellidos, String idRolUsuario, String nombreUsuario, String correo, String constrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idRolUsuario = idRolUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.constrasenia = constrasenia;
    }
}
