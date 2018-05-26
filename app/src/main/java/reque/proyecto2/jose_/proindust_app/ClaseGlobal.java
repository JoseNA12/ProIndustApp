package reque.proyecto2.jose_.proindust_app;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClaseGlobal {

    // IP del servidor
    private static final String IP = "https://proindustapp.000webhostapp.com/";

    // Rutas de los Web Services

    // Proyectos
    public static final String INSERT_PROYECTO = IP + "Proyecto/insertar_proyecto.php";
    public static final String SELECT_PROYECTOS_ALL = IP + "Proyecto/select_proyectos_all.php";

    // Operaciones
    public static final String INSERT_OPERACION = IP + "Operacion/insertar_operacion.php";
    public static final String SELECT_OPERACIONES_ALL = IP + "Operacion/select_operaciones_all.php";

    // Tareas
    public static final String INSERT_TAREA = IP + "Tarea/insertar_tarea.php";
    public static final String SELECT_TAREAS_ALL = IP + "Tarea/select_tareas_all.php";

    // Colaboradores
    public static final String INSERT_COLABORADOR = IP + "Colaborador/insertar_colaborador.php";
    public static final String SELECT_COLABORADORES_ALL = IP + "Colaborador/select_colaboradores_all.php";

    // Usuarios
    public static final String SELECT_USUARIO = IP + "Usuario/select_usuario.php";
    public static final String SELECT_USUARIOS_ALL = IP + "Usuario/select_usuarios_all.php";
    public static final String INSERT_USUARIO = IP + "Usuario/insertar_usuario.php";

    public static final String SELECT_ROLESUSUARIOS_ALL = IP + "Usuario/select_rolusuarios_all.php";

    // Actividades
    public static final String SELECT_ACTIVIDADES_ALL = IP +"Actividad/select_all_actividad.php";
    public static final String SELECT_ACTIVIDAD_BY_NOMBRE = IP +"Actividad/select_actividad_by_nombre.php";

    // OperacionTarea
    public static final String INSERT_OPERACIONTAREA = IP + "OperacionTarea/insertar_OperacionTarea.php";
    public static final String SELECT_OPERACIONTAREA_ALL = IP + "OperacionTarea/select_OperacionTarea.php";

    // ProyectoColaborador
    public static final String INSERT_PROYECTOCOLABORADOR = IP + "ProyectoColaborador/insertar_ProyectoColaborador.php";
    public static final String SELECT_PROYECTOCOLABORADOR_ALL = IP + "ProyectoColaborador/select_ProyectoColaborador.php";

    // ProyectoOperacion
    public static final String INSERT_PROYECTOOPERACION = IP + "ProyectoOperacion/insertar_ProyectoOperacion.php";
    public static final String SELECT_PROYECTOOPERACION_ALL = IP + "ProyectoOperacion/select_ProyectoOperacion.php ";

    // ProyectoUsuario
    public static final String INSERT_PROYECTOUSUARIO = IP + "ProyectoUsuario/insertar_ProyectoUsuario.php";
    public static final String SELECT_PROYECTOUSUARIO_ALL = IP + "ProyectoUsuario/select_ProyectoUsuario.php";


}
