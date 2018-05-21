package reque.proyecto2.jose_.proindust_app;

public class ClaseGlobal {

    // IP del servidor
    private static final String IP = "http://proindustapp.000webhostapp.com/";

    // Rutas de los Web Services
    public static final String GET_PROYECTO = IP + "obtener_proyectos.php";
    public static final String GET_PROYECTO_BY_ID = IP + "obtener_proyecto_por_id.php";
    public static final String INSERT_PROYECTO = IP + "insertar_proyecto.php";
    public static final String UPDATE_PROYECTO = IP + "actualizar_proyecto.php";
    public static final String DELETE_PROYECTO = IP + "eliminar_proyecto.php";

    public static final String INSERT_USUARIO = IP + "insertar_usuario.php";
}
