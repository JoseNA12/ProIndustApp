package reque.proyecto2.jose_.proindust_app;

public class ClaseGlobal {

    // IP del servidor
    private static final String IP = "https://proindustapp.000webhostapp.com/";

    // Rutas de los Web Services

    // Proyectos
    public static final String INSERT_PROYECTO = IP + "Proyecto/insertar_proyecto.php";
    public static final String SELECT_PROYECTOS_ALL = IP + "Proyecto/select_proyectos_all.php";

    // Operaciones
    public static final String SELECT_OPERACIONES_ALL = IP + "Operacion/select_operaciones_all.php";

    // Tareas
    public static final String SELECT_TAREAS_ALL = IP + "Tarea/select_tareas_all.php";

    // Colaboradores
    public static final String SELECT_COLABORADORES_ALL = IP + "Colaborador/select_colaboradores_all.php";

    // Usuarios
    public static final String SELECT_USUARIO = IP + "Usuario/select_usuario.php";
    public static final String SELECT_USUARIOS_ALL = IP + "Usuario/select_usuarios_all.php";
    public static final String INSERT_USUARIO = IP + "Usuario/insertar_usuario.php";

}
