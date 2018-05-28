package reque.proyecto2.jose_.proindust_app.modelo;

public class Usuario {

    public String id, nombre, apellidos, idRolUsuario, nombreUsuario, correo, contrasenia;

    public Usuario(String id, String nombre, String apellidos, String idRolUsuario, String nombreUsuario, String correo, String contrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idRolUsuario = idRolUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }

    public Usuario(String id, String nombreUsuario) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + "\n" +
                "Apellidos: " + apellidos + "\n" +
                "Rol de Usuario: " + getRol(idRolUsuario) + "\n" +
                "Nombre de Usuario: " + nombreUsuario + "\n" +
                "Correo: " + correo + "\n";
    }

    private String getRol(String pId)
    {
        String var = "";
        if (pId.equals("1")) { var = "ADMINISTRADOR"; }
        else { var = "ANALISTA"; }
        return var;
    }
}
