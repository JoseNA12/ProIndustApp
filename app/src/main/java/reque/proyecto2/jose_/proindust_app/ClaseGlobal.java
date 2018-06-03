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
import java.util.List;

import reque.proyecto2.jose_.proindust_app.modelo.Usuario;

public class ClaseGlobal {

    public static Usuario usuarioActual;
    // public static final String[] estadosProyecto = {"EN CURSO", "PAUSADO"};
    public static final String[] estadosMuestreo = {"EN CURSO", "PAUSADO"}; // CONCLUIDO
    public static final String cantObservRequeridas_default = "30";

    // IP del servidor
    private static final String IP = "https://proindustapp.000webhostapp.com/";

    // Rutas de los Web Services

    // Proyectos
    public static final String INSERT_PROYECTO = IP + "Proyecto/insertar_proyecto.php";
    public static final String SELECT_PROYECTOS_ALL = IP + "Proyecto/select_proyectos_all.php";
    public static final String DELECT_PROYECTO = IP + "Proyecto/eliminar_proyecto.php";
    public static final String UPDATE_PROYECTO = IP + "Proyecto/actualizar_proyecto.php";
    public static final String SELECT_PROYECTOS_CON_MUESTREOS_ACTIVOS = IP + "Proyecto/select_proyectos_con_muestreos_activos.php";
    public static final String SELECT_PROYECTOS_SIN_MUESTREOS_ACTIVOS = IP + "Proyecto/select_proyectos_sin_muestreos_activos.php";
    public static final String SELECT_PROYECTOS_CON_MUESTREOS_ACTIVOS_PAUSADOS = IP + "Proyecto/select_proyectos_con_muestreos_activos_pausados.php";
    public static final String SELECT_PROYECTOS_ACTIVOS_DE_USUARIO = IP + "Proyecto/select_proyectos_activos_de_usuario.php";

    // Operaciones
    public static final String INSERT_OPERACION = IP + "Operacion/insertar_operacion.php";
    public static final String SELECT_OPERACIONES_ALL = IP + "Operacion/select_operaciones_all.php";
    public static final String DELECT_OPERACION = IP + "Operacion/eliminar_operacion.php";
    public static final String UPDATE_OPERACION = IP + "Operacion/actualizar_operacion.php";

    // Tareas
    public static final String INSERT_TAREA = IP + "Tarea/insertar_tarea.php";
    public static final String SELECT_TAREAS_ALL = IP + "Tarea/select_tareas_all.php";
    public static final String DELECT_TAREA = IP + "Tarea/eliminar_Tarea.php";
    public static final String UPDATE_TAREA = IP + "Tarea/actualizar_tarea.php";

    // Colaboradores
    public static final String INSERT_COLABORADOR = IP + "Colaborador/insertar_colaborador.php";
    public static final String SELECT_COLABORADORES_ALL = IP + "Colaborador/select_colaboradores_all.php";
    public static final String DELECT_COLABORADOR = IP + "Colaborador/eliminar_colaborador.php";
    public static final String UPDATE_COLABORADOR = IP + "Colaborador/actualizar_colaborador.php";

    // Usuarios
    public static final String SELECT_USUARIOS_ALL = IP + "Usuario/select_usuarios_all.php";
    public static final String INSERT_USUARIO = IP + "Usuario/insertar_usuario.php";
    public static final String SELECT_USUARIO_LOGIN = IP + "Usuario/select_usuario_iniciar_sesion.php";
    public static final String DELECT_USUARIO = IP + "Usuario/eliminar_usuario.php";
    public static final String UPDATE_USUARIO = IP + "Usuario/actualizar_usuario.php";

    public static final String SELECT_ROLESUSUARIOS_ALL = IP + "Usuario/select_rolusuarios_all.php";

    // Actividades
    public static final String SELECT_ACTIVIDADES_ALL = IP +"Actividad/select_all_actividad.php";
    public static final String SELECT_ACTIVIDAD_BY_NOMBRE = IP +"Actividad/select_actividad_by_nombre.php";

    // OperacionTarea
    public static final String INSERT_OPERACIONTAREA = IP + "OperacionTarea/insertar_OperacionTarea.php";
    public static final String SELECT_OPERACIONTAREA_ALL = IP + "OperacionTarea/select_OperacionTarea.php";
    public static final String DELECT_OPERACIONTAREA_ID_ID = IP +"OperacionTarea/eliminar_operacionTarea.php";
    public static final String SELECT_TAREAS_DE_OPERACION = IP + "OperacionTarea/select_tareas_de_operacion.php";

    // ProyectoColaborador
    public static final String INSERT_PROYECTOCOLABORADOR = IP + "ProyectoColaborador/insertar_ProyectoColaborador.php";
    public static final String SELECT_PROYECTOCOLABORADOR_ALL = IP + "ProyectoColaborador/select_ProyectoColaborador.php";
    public static final String DELECT_PROYECTOCOLABORADOR_ID_ID = IP + "ProyectoColaborador/eliminar_proyectoColaborador.php";
    public static final String SELECT_COLABORADORES_DE_PROYECTO = IP + "ProyectoColaborador/select_colaboradores_de_proyecto.php";

    // ProyectoOperacion
    public static final String INSERT_PROYECTOOPERACION = IP + "ProyectoOperacion/insertar_ProyectoOperacion.php";
    public static final String SELECT_PROYECTOOPERACION_ALL = IP + "ProyectoOperacion/select_ProyectoOperacion.php";
    public static final String DELECT_PROYECTOOPERACION_ID_ID = IP + "ProyectoOperacion/eliminar_proyectoOperacion.php";
    public static final String SELECT_OPERACIONES_DE_PROYECTO = IP + "ProyectoOperacion/select_operaciones_de_proyecto.php";

    // ProyectoUsuario
    public static final String INSERT_PROYECTOUSUARIO = IP + "ProyectoUsuario/insertar_ProyectoUsuario.php";
    public static final String SELECT_PROYECTOUSUARIO_ALL = IP + "ProyectoUsuario/select_ProyectoUsuario.php";
    public static final String DELECT_PROYECTOUSUARIO_ID_ID = IP + "ProyectoUsuario/eliminar_proyectoUsuario.php";

    // HorasLibres_C
    public static final String INSERT_HORALIBRE = IP + "HorasLibres/insertar_horasLibres.php";
    public static final String SELECT_HORASLIBRES_ALL = IP + "HorasLibres/select_all_horasLibres.php";
    public static final String DELECT_HORALIBRE = IP + "HorasLibres/eliminar_horasLibres.php";

    // Iniciar sesion
    public static final String INICIAR_SESION = IP + "iniciar_sesion.php";

    // Observacion
    public static final String INSERT_OBSERVACION = IP + "Observacion/insertar_muestra.php";

    // Muestreo
    public static final String INSERT_MUESTREO = IP + "Muestreo/insertar_muestreo.php";
    public static final String SELECT_MUESTREOS_ALL = IP + "Muestreo/select_muestreos_all.php";
    public static final String UPDATE_MUESTREO = IP + "Muestreo/actualizar_muestreo.php";
    public static final String SELECT_MUESTREO_ACTIVO_DE_PROYECTO = IP + "Muestreo/select_muestreo_activo_de_proyecto.php";

    public static String longitud;
    public static String latitud;
    public static String humedad;
    public static String temperatura;


}
