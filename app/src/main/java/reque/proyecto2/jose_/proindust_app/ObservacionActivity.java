package reque.proyecto2.jose_.proindust_app;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import reque.proyecto2.jose_.proindust_app.modelo.Colaborador;
import reque.proyecto2.jose_.proindust_app.modelo.Muestreo;
import reque.proyecto2.jose_.proindust_app.modelo.Operacion;
import reque.proyecto2.jose_.proindust_app.modelo.Proyecto;
import reque.proyecto2.jose_.proindust_app.modelo.Tarea;
import reque.proyecto2.jose_.proindust_app.modelo.DistNormal;

import static junit.framework.Assert.assertEquals;

public class ObservacionActivity extends AppCompatActivity {


    private Spinner sp_proyecto, sp_operacion, sp_colaborador;

    private AutoCompleteTextView atctv_tarea;

    private EditText et_descripcion;

    private Button bt_registrar, bt_terminar;

    private String msgProyecto = "Seleccione un proyecto...";
    private String msgOperacion = "Seleccione una operación...";
    private String msgColaborador = "Seleccione un colaborador...";

    private ProgressDialog progressDialog;

    private String proyectoSeleccionado;
    private String operacionSeleccionada;
    private String colaboradorSeleccionado;

    private ArrayAdapter<String> adapterSpinner_proyecto, adapterSpinner_operacion, adapterSpinner_colaborador;

    private ArrayList<Proyecto> listaDatosProyectos;
    private ArrayList<Operacion> listaDatosOperaciones;
    private ArrayList<Tarea> listaDatosTareas;
    private ArrayList<Colaborador> listaDatosColaborador;
    private Muestreo miMuestreoActual;

    private String horaActual;
    private String fechaActual;

    private boolean registroObservacion = false;
    private int cantObservRegistradas_actual = 0;
    private int cantObservRequeridas_actual = 0;

    private int totalObservacionesProductivas = 0;
    private int totalObservaciones = 0;

    private boolean puedeTerminar = false;

    // contienen todos los datos registrados en la base de datos (solo el nombre)
    // private List<String> listaProyectos_todos, listaOperaciones_todos, listaColaboradores_todos, listaTareas_todos;
    // contiene los datos filtrados por parametros, proyectos de acuerdo a usuarios, operaciones de acuerdo a proyectos, etc
    private List<String> listaProyectos_filtro, listaOperaciones_filtro, listaColaboradores_filtro, listaTareas_filtro;

    // request temperatura y humedad
    static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        horaActual = formatoHora.format(cal.getTime());
        fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        registroObservacion = false;
        cantObservRegistradas_actual = 0;
        cantObservRequeridas_actual = 0;

        GetProyectos();
        GetOperaciones();
        GetColaboradores();
        GetTareas();

        sp_proyecto = (Spinner) findViewById(R.id.sp_proyecto_ID);
        sp_operacion = (Spinner) findViewById(R.id.sp_operacion_ID);
        sp_colaborador = (Spinner) findViewById(R.id.sp_colaborador_ID);
        atctv_tarea = (AutoCompleteTextView) findViewById(R.id.atctv_tarea_ID);

        listaDatosProyectos = new ArrayList<Proyecto>();
        listaDatosOperaciones = new ArrayList<Operacion>();
        listaDatosColaborador = new ArrayList<Colaborador>();
        listaDatosTareas = new ArrayList<Tarea>();

        // listaProyectos_todos = new ArrayList<String>();
        // listaOperaciones_todos = new ArrayList<String>();
        // listaColaboradores_todos = new ArrayList<String>();
        // listaTareas_todos = new ArrayList<String>();

        et_descripcion = (EditText) findViewById(R.id.tv_descripcion_ID);
        bt_registrar = (Button) findViewById(R.id.bt_Registrar_ID);
        bt_terminar = (Button) findViewById(R.id.bt_Terminar_ID);

        sp_proyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                proyectoSeleccionado = sp_proyecto.getSelectedItem().toString();

                miMuestreoActual = null;
                String idProyecto = GetIdProyecto(proyectoSeleccionado);

                BuscarMuestroActivoProyecto(idProyecto);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_operacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                operacionSeleccionada = sp_operacion.getSelectedItem().toString();

                ActualizarListaDatos_Tarea(Get_Tareas_de_operacion(GetIdOperacion(operacionSeleccionada)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_colaborador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                colaboradorSeleccionado = sp_colaborador.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boton_RegistrarObservacion();
            }
        });

        bt_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boton_TerminarObservaciones();
            }
        });

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Llama a DeterminarProyectos()
        ParametrosAmbiente(); // Temperatura y humedad
    }

    private void Boton_TerminarObservaciones()
    {
        if (registroObservacion)
        {
            Actualizar_y_terminar_muestreos_realizados();
        }
        else
        {
            MessageDialog("Debe registrar al menos 1 observacion para poder terminar con el muestreo!",
                    "Atención!", "Aceptar");
        }
    }

    private void Actualizar_y_terminar_muestreos_realizados()
    {
        String horaInicio = ObtenerHoraMuestreo(
                GetTiempoAleatorio(Integer.parseInt(miMuestreoActual.lapsoInicial),
                        Integer.parseInt(miMuestreoActual.lapsoFinal) +
                                Integer.parseInt(miMuestreoActual.tiempoRecorrido)));

        String URL = ClaseGlobal.UPDATE_OBSER_REGIST_MUESTREO +
                "?idMuestreo=" + miMuestreoActual.idMuestreo +
                "&cantObservRegistradas=" + Integer.toString(cantObservRegistradas_actual) +
                "&horaObservacion=" + horaInicio;

        progressDialog.setMessage("Registrado muestreos...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        miMuestreoActual.cantObservRegistradas = Integer.toString(cantObservRegistradas_actual);

                        Intent intent_ = new Intent(ObservacionActivity.this, MenuPrincipalActivity.class);

                        if (ClaseGlobal.usuarioActual.idRolUsuario.equals("1")) {
                            intent_.putExtra("ROL", "ADMINISTRADOR");
                        }
                        else
                        {
                            intent_.putExtra("ROL", "ANALISTA");
                        }
                        startActivity(intent_);

                        Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                                "Muestreo finalizado!", Snackbar.LENGTH_LONG).show();

                    }
                    else
                    {
                        MessageDialog("Error al finalizar el muestreo!", "Error", "Aceptar");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error de conexión", "Aceptar");
            }
        });queue.add(stringRequest);
    }

    /**
     * Dado un rango (valor minimo y maximo) obtener un valor aleatorio dentro de ese rango
     * @param pLapsoInicial
     * @param pLapsoFinal
     * @return
     */
    private Integer GetTiempoAleatorio(int pLapsoInicial, int pLapsoFinal)
    {
        return ThreadLocalRandom.current().nextInt(pLapsoInicial, pLapsoFinal + 1);
    }

    /**
     * Obtener la hora del primer muestreo preliminar segun el valor generado aleatoriamente
     * acorde al rango de minutos ingresado
     * @param pValor
     * @return
     */
    private String ObtenerHoraMuestreo(int pValor)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, pValor);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String nuevaHora = Integer.toString(hour) + ":" + Integer.toString(minute) + ":" + second;

        return nuevaHora;
    }

    /**
     * Función atada al boton de registrar
     */
    private void Boton_RegistrarObservacion()
    {
        String tareaSeleccionada = atctv_tarea.getText().toString();
        String comentario = et_descripcion.getText().toString();

        if (!proyectoSeleccionado.equals(msgProyecto))
        {
            if (!operacionSeleccionada.equals(msgOperacion))
            {
                if (!colaboradorSeleccionado.equals(msgColaborador))
                {
                    String idTarea = GetIdTarea(tareaSeleccionada);

                    if (!idTarea.equals("error"))
                    {
                        RegistrarObservacion(ClaseGlobal.INSERT_OBSERVACION +
                                "?comentario=" + comentario +
                                "&temperatura=" + ClaseGlobal.temperatura +
                                "&humedad=" + ClaseGlobal.humedad +
                                "&fecha=" + fechaActual +
                                "&hora=" + horaActual +
                                "&idUsuario=" + ClaseGlobal.usuarioActual.id +
                                "&idColaborador=" + GetIdColaborador(colaboradorSeleccionado) +
                                "&idOperacion=" + GetIdOperacion(operacionSeleccionada) +
                                "&idTarea=" + GetIdTarea(tareaSeleccionada) +
                                "&idMuestreo=" + miMuestreoActual.idMuestreo
                        );
                    }
                    else
                    {
                        MessageDialog("Por favor, ingrese una tarea válida", "Error", "Aceptar");
                    }
                }
                else
                {
                    MessageDialog("Por favor, seleccione un colaborador", "Error", "Aceptar");
                }
            }
            else
            {
                MessageDialog("Por favor, seleccione una operación", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, seleccione un proyecto", "Error", "Aceptar");
        }
    }

    /**
     * Función encargada de realizar el request a la base de datos para almacenar la muestra
     * @param URL
     */
    private void RegistrarObservacion(String URL)
    {
        progressDialog.setMessage("Registrando observación...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("true"))
                    {
                        Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                                "Se ha registrado la observación!", Snackbar.LENGTH_SHORT).show();

                        registroObservacion = true;

                        DeterminarFinMuestreo();
                    }
                    else
                    {
                        MessageDialog("Error al crear la observación!", "Error", "Aceptar");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        }); queue.add(stringRequest);
    }

    private void DeterminarFinMuestreo()
    {
        cantObservRegistradas_actual += 1;

        if (cantObservRegistradas_actual >= cantObservRequeridas_actual)
        {
            DeterminarActualizarMuestreo(miMuestreoActual.idMuestreo);
        }
    }

    private void DeterminarActualizarMuestreo(String pIdMuestreo)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(ObservacionActivity.this);
        alert.setTitle("Atención!");
        alert.setCancelable(false);

        LinearLayout layout = new LinearLayout(ObservacionActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView et_mensaje = new TextView(ObservacionActivity.this);
        et_mensaje.setText("Se ha alcanzado el tope de muestreos preliminares definidos!. \n" +
                "¿Que desea hacer, continuar registrando muestras o finalizar?");
        layout.addView(et_mensaje);

        alert.setView(layout);

        alert.setPositiveButton("FINALIZAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                ObtenerTotalObservaciones();
                ObtenerTotalObservaciones_Productivas();

                InsertarValoresFormula(); // ------------------------------------------------------------------------------------
            }
        });

        alert.setNegativeButton("CONTINUAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                InsertarCantMuestreosNuevos();
            }
        });

        alert.show();

    }

    private void Poner_en_concluido_muestreo()
    {
        String URL = ClaseGlobal.UPDATE_PONER_CONCLUIDO_MUESTREO +
                "?idMuestreo=" + miMuestreoActual.idMuestreo +
                "&estado=" + "CONCLUIDO";

        progressDialog.setMessage("Finalizando muestreo...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                                "Muestreo finalizado!", Snackbar.LENGTH_LONG).show();

                    }
                    else
                    {
                        MessageDialog("Error al finalizar el muestreo!", "Error", "Aceptar");
                        DeterminarActualizarMuestreo(miMuestreoActual.idMuestreo);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error de conexión", "Aceptar");
            }
        });queue.add(stringRequest);
    }

    private void InsertarCantMuestreosNuevos()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(ObservacionActivity.this);
        alert.setTitle("Continuar muestreo");

        LinearLayout layout = new LinearLayout(ObservacionActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText et_cantMuestreos = new EditText(ObservacionActivity.this);
        et_cantMuestreos.setHint("Cant. de muestreos por agregar");
        et_cantMuestreos.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(et_cantMuestreos);

        alert.setView(layout);

        alert.setPositiveButton("REGISTRAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String pCantidad = et_cantMuestreos.getText().toString();

                if (!pCantidad.equals("") )
                {
                    ActualizarMuestreo_nuevo_tope(pCantidad, false);
                }
                else
                {
                    // no insertó el valor
                    InsertarCantMuestreosNuevos();
                    Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                            "Debe ingresar la cantidad agregada de muestreos!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                // Intent intent_menuPrincipal = new Intent(ObservacionActivity.this, MenuPrincipalActivity.class);
                // startActivity(intent_menuPrincipal);
                DeterminarActualizarMuestreo(miMuestreoActual.idMuestreo);
            }
        });

        alert.show();
    }

    private void ActualizarMuestreo_nuevo_tope(final String pCantidad, final boolean puedeTerminar)
    {
        String temp = Integer.toString(cantObservRequeridas_actual + Integer.parseInt(pCantidad));

        String URL = ClaseGlobal.UPDATE_OBSER_REQUE_MUESTREO +
                "?idMuestreo=" + miMuestreoActual.idMuestreo +
                "&cantObservRequeridas=" + temp;

        progressDialog.setMessage("Actualizando muestreo...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        cantObservRequeridas_actual += Integer.parseInt(pCantidad);
                        miMuestreoActual.cantObservRequeridas = Integer.toString(cantObservRequeridas_actual);

                        Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                                "Se ha actualizado el muestreo!", Snackbar.LENGTH_LONG).show();

                        if (puedeTerminar) { Poner_en_concluido_muestreo(); Boton_TerminarObservaciones(); }

                    }
                    else
                    {
                        MessageDialog("Error al actualizar el muestreo!", "Error", "Aceptar");
                        DeterminarActualizarMuestreo(miMuestreoActual.idMuestreo);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error de conexión", "Aceptar");
            }
        });queue.add(stringRequest);

    }

    private void InsertarValoresFormula()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(ObservacionActivity.this);
        alert.setTitle("Registrar valores");
        alert.setCancelable(false);

        LinearLayout layout = new LinearLayout(ObservacionActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText et_nivelConfianza = new EditText(ObservacionActivity.this);
        et_nivelConfianza.setHint("Nivel de confianza");
        et_nivelConfianza.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(et_nivelConfianza);

        final EditText et_nivelError = new EditText(ObservacionActivity.this);
        et_nivelError.setHint("Nivel de error (%)");
        et_nivelError.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(et_nivelError);

        alert.setView(layout);

        alert.setPositiveButton("REGISTRAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String pNivelConfianza = et_nivelConfianza.getText().toString();
                String pNivelError = et_nivelError.getText().toString();

                if (!pNivelConfianza.equals("") && !pNivelError.equals(""))
                {
                    int pNivelConfianza_int = Integer.parseInt(pNivelConfianza);
                    int pNivelError_int = Integer.parseInt(pNivelError);

                    if ((pNivelConfianza_int <= 99) && (pNivelConfianza_int >= 90))
                    {

                        ObtenerMuestreosNecesarios_al_finalizar(pNivelConfianza, pNivelError);

                    }
                    else
                    {
                        Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                                "El nivel de confianza debe ser entre 90 y 99!", Snackbar.LENGTH_LONG).show();
                        InsertarValoresFormula();
                    }
                    // Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                            // "Valores registrados!", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    // no insertó alguno o ninguno de loa valores

                    InsertarValoresFormula();
                    Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                            "Debe ingresar todos los valores!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                // Intent intent_menuPrincipal = new Intent(ObservacionActivity.this, MenuPrincipalActivity.class);
                // startActivity(intent_menuPrincipal);
                DeterminarActualizarMuestreo(miMuestreoActual.idMuestreo);
            }
        });

        alert.show();
    }

    private void ObtenerMuestreosNecesarios_al_finalizar(String pNivelConfinza, String pNivelError)
    {
        int cantidad = 0;

        DistNormal a = new DistNormal(
                Integer.parseInt(pNivelError),
                Integer.parseInt(pNivelConfinza),
                totalObservaciones, totalObservacionesProductivas);

        cantidad = (int) a.CalcularNMuestras();

        Determinar_Muestras_pendientes_debido_valores(cantidad);

    }

    private void Determinar_Muestras_pendientes_debido_valores(final int pValor)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(ObservacionActivity.this);
        alert.setTitle("Confirmar ?");
        alert.setCancelable(false);

        LinearLayout layout = new LinearLayout(ObservacionActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView et_mensaje = new TextView(ObservacionActivity.this);

        puedeTerminar = false;
        String msg = "";
        if (pValor == 0)
        {
            puedeTerminar = true;
            msg = "No se requieren muestreos adicionales!";
        }
        else
        {
            msg = "Se necesitan " + Integer.toString(pValor) + " muestras más segun los valores ingresados!";
        }

        et_mensaje.setText(msg);
        layout.addView(et_mensaje);

        alert.setView(layout);

        alert.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                ActualizarMuestreo_nuevo_tope(Integer.toString(pValor), puedeTerminar);
            }
        });

        alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                InsertarValoresFormula();
            }
        });

        alert.show();
    }

    private int ObtenerTotalObservaciones()
    {
        String URL = ClaseGlobal.SELECT_TOTAL_OBSERVACIONES_MUESTREO +
                "?idMuestreo=" + miMuestreoActual.idMuestreo;

        progressDialog.setMessage("Obteniendo total de tareas registradas...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    if (jsonArray.length() > 0)
                    {
                        String count = jsonArray.getJSONObject(0).get("COUNT(idObservacion)").toString();
                        totalObservaciones = Integer.parseInt(count);

                    }
                    else
                    {
                        MessageDialog("Error al obtener el total de observaciones del muestreo!", "Error", "Aceptar");
                        // NO SE QUE HACER SI FALLA
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error de conexión", "Aceptar");
            }
        });queue.add(stringRequest);

        return totalObservaciones;
    }

    private int ObtenerTotalObservaciones_Productivas()
    {
        String URL = ClaseGlobal.SELECT_TOTAL_OBSERVACIONES_PRODUCTIVAS_MUESTREO +
                "?idMuestreo=" + miMuestreoActual.idMuestreo;

        progressDialog.setMessage("Obteniendo total de tareas registradas...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    if (jsonArray.length() > 0)
                    {
                        String count = jsonArray.getJSONObject(0).get("COUNT(idObservacion)").toString();
                        totalObservacionesProductivas = Integer.parseInt(count);
                    }
                    else
                    {
                        MessageDialog("Error al obtener el total de observaciones del muestreo!", "Error", "Aceptar");
                        // NO SE QUE HACER SI FALLA
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error de conexión", "Aceptar");
            }
        });queue.add(stringRequest);

        return totalObservacionesProductivas;
    }

    /**
     * Retornar el objeto del muestreo activo perteneciente al ID del proyecto insertado
     * Sirve tambien para verificar si existe dicho muestreo en estado de activo, esto
     * para dar paso a efectuar las observaciones
     * @param pIdProyecto
     * @return
     */
    private void BuscarMuestroActivoProyecto(String pIdProyecto)
    {
        progressDialog.setMessage("Consultando proyectos...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_MUESTREO_ACTIVO_DE_PROYECTO + "?idProyecto=" + pIdProyecto;
        // final List<Muestreo> array = new ArrayList<Muestreo>();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    // en teoria deber ser una sola iteración (1 muestreo activo por proyecto)
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String idMuestreo = jsonArray.getJSONObject(i).get("idMuestreo").toString();
                        String idProyecto = jsonArray.getJSONObject(i).get("idProyecto").toString();
                        String fechaInicio = jsonArray.getJSONObject(i).get("fechaInicio").toString();
                        String lapsoInicial = jsonArray.getJSONObject(i).get("lapsoInicial").toString();
                        String lapsoFinal = jsonArray.getJSONObject(i).get("lapsoFinal").toString();
                        String horaObservacion = jsonArray.getJSONObject(i).get("horaObservacion").toString();
                        String tiempoRecorrido = jsonArray.getJSONObject(i).get("tiempoRecorrido").toString();
                        String estado = jsonArray.getJSONObject(i).get("estado").toString();
                        String descripcion = jsonArray.getJSONObject(i).get("descripcion").toString();
                        String cantObservRegistradas = jsonArray.getJSONObject(i).get("cantObservRegistradas").toString();
                        String cantObservRequeridas = jsonArray.getJSONObject(i).get("cantObservRequeridas").toString();

                        //array.add(new
                        miMuestreoActual = new Muestreo(idMuestreo, idProyecto, fechaInicio, lapsoInicial, lapsoFinal,
                                horaObservacion, tiempoRecorrido, estado, descripcion, cantObservRegistradas,
                                cantObservRequeridas);

                        cantObservRegistradas_actual = Integer.parseInt(cantObservRegistradas);
                        cantObservRequeridas_actual = Integer.parseInt(cantObservRequeridas);

                        progressDialog.dismiss();
                        DecidirPermitirRegistrarObservaciones(idProyecto);

                        break; // por si las moscas (caso extremo)
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al solicitar operaciones.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);
    }

    /**
     * Dado un ID de proyecto, determinar si se encuentra en el dia, y en la hora indicada para hacer
     * el muestreo
     * @param pIdProyecto
     */
    private void DecidirPermitirRegistrarObservaciones(String pIdProyecto) // .. ?
    {
        // hora y fecha muestreo
        String horaM = miMuestreoActual.horaObservacion;
        String fechaM = miMuestreoActual.fechaInicio;

        long dias = CalcularDiferenciaDias(fechaM);
        if (dias != 0)
        {
            ActualizarListaDatos_Colaboradores(new ArrayList<String>()); colaboradorSeleccionado = msgOperacion;
            ActualizarListaDatos_Operaciones(new ArrayList<String>()); operacionSeleccionada = msgOperacion;
            MessageDialog("Aún faltan " + Long.toString(dias) + " días para dar inicio al muestreo!",
                    "Atención!", "Aceptar");
        }
        else
        {
            int minutos = CalcularDiferenciaMinutos(horaM);

            if (minutos < 1)
            {
                Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                        "Es momento de realizar observaciones!", Snackbar.LENGTH_LONG).show();

                ActualizarListaDatos_Operaciones(Get_Operaciones_de_proyecto(pIdProyecto));
                ActualizarListaDatos_Colaboradores(Get_Colaboradores_de_proyecto(pIdProyecto));
            }
            else
            {
                ActualizarListaDatos_Operaciones(new ArrayList<String>()); colaboradorSeleccionado = msgOperacion;
                ActualizarListaDatos_Colaboradores(new ArrayList<String>()); operacionSeleccionada = msgOperacion;
                MessageDialog("Aún faltan " + Integer.toString(minutos) + " minutos para dar inicio al muestreo!",
                        "Atención!", "Aceptar");
            }
        }
    }

    private int CalcularDiferenciaMinutos(String pHora)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

        long difference = 0;
        int minutes = 0;
        try
        {
            Date date2 = formatoHora.parse(pHora);
            Date date1 = formatoHora.parse(formatoHora.format(cal.getTime()));
            difference = (date2.getTime() - date1.getTime());

            minutes = (int) TimeUnit.MILLISECONDS.toMinutes(difference);

            // if(minutes<0)minutes += 1440;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return minutes;
    }

    private long CalcularDiferenciaDias(String pFecha)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        int fechaActual_anio = Integer.parseInt(dateFormat.format(date).substring(0, 4));
        int fechaActual_mes = Integer.parseInt(dateFormat.format(date).substring(5, 7));
        int fechaActual_dia = Integer.parseInt(dateFormat.format(date).substring(8, 10));

        int fechaMuestreo_anio = Integer.parseInt(pFecha.substring(0, 4));
        int fechaMuestreo_mes = Integer.parseInt(pFecha.substring(5, 7));
        int fechaMuestreo_dia = Integer.parseInt(pFecha.substring(8, 10));

        Calendar c2 = new GregorianCalendar(fechaMuestreo_anio, fechaMuestreo_mes, fechaMuestreo_dia);
        Calendar c1 = new GregorianCalendar(fechaActual_anio, fechaActual_mes, fechaActual_dia);

        // 1000 x 60 X 60 X 24 = MS per Day
        long diasRestantes = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000*60*60*24);

        return diasRestantes;
    }

    /**
     * GetTiempo(): apenas se registren las valores (temp. y humedad) cargar los proyectos
     * InsertarParemetrosManual(): apenas se inserten los valores (temp. y humedad) cargar los proyectos
     */
    private void Determinar_Proyectos_a_mostrar()
    {
        if (ClaseGlobal.usuarioActual.idRolUsuario.equals("2")) // Analista
        {
            listaProyectos_filtro = Get_Proyectos_activos_de_usuario(ClaseGlobal.usuarioActual.id);
            ActualizarListaDatos_Proyectos(listaProyectos_filtro);
        }
        else // Administrador
        {
            listaProyectos_filtro = Get_Proyectos_activos();
            ActualizarListaDatos_Proyectos(listaProyectos_filtro);
        }
    }

    /**
     * Obtiene todos los proyectos con muestreos activos
     * Usado para administrador, ya que él puede muestrear cualquier proyecto
     * @return
     */
    private List<String> Get_Proyectos_activos()
    {
        progressDialog.setMessage("Consultando proyectos...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_PROYECTOS_CON_MUESTREOS_ACTIVOS;
        final List<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(msgProyecto);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        arraySpinner.add(nombre);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al solicitar operaciones.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    /**
     * Obtiene todos los proyectos con muestreos en estado de ACTIVO dado un usuario analista
     * @param pIdUsuario
     * @return
     */
    private List<String> Get_Proyectos_activos_de_usuario(String pIdUsuario)
    {
        progressDialog.setMessage("Consultando proyectos...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_PROYECTOS_ACTIVOS_DE_USUARIO + "?idUsuario=" + pIdUsuario;
        final List<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(msgProyecto);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        arraySpinner.add(nombre);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al solicitar proyectos.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    /**
     * Obtiene todas las operaciones relacionadas a un proyecto dado, mediante el ID del proyecto
     * @param pIdProyecto
     * @return
     */
    private List<String> Get_Operaciones_de_proyecto(String pIdProyecto)
    {
        progressDialog.setMessage("Consultando operaciones...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_OPERACIONES_DE_PROYECTO + "?idProyecto=" + pIdProyecto;
        final List<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(msgOperacion);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        arraySpinner.add(nombre);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al solicitar operaciones.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    /**
     * Obtiene todos los colaboradores de un proyecto mediante el ID del proyecto
     * @param pIdProyecto
     * @return
     */
    private List<String> Get_Colaboradores_de_proyecto(String pIdProyecto)
    {
        progressDialog.setMessage("Consultando colaboradores...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_COLABORADORES_DE_PROYECTO + "?idProyecto=" + pIdProyecto;
        final List<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(msgColaborador);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String pseudonimo = jsonArray.getJSONObject(i).get("pseudonimo").toString();

                        arraySpinner.add(pseudonimo);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al solicitar colaboradores.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    /**
     * Obtiene todas las tareas relacionadas con una operacion, mediante el ID de la operacione
     * @param pIdOperacion
     * @return
     */
    private List<String> Get_Tareas_de_operacion(String pIdOperacion)
    {
        progressDialog.setMessage("Consultando tareas...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_TAREAS_DE_OPERACION + "?idOperacion=" + pIdOperacion;
        final List<String> arraySpinner = new ArrayList<String>();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        arraySpinner.add(nombre);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al solicitar colaboradores.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    /**
     * Regrescar el contenido del spinner
     * @param lista
     */
    private void ActualizarListaDatos_Proyectos(List<String> lista)
    {
        adapterSpinner_proyecto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adapterSpinner_proyecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_proyecto.setAdapter(adapterSpinner_proyecto);
    }

    private void ActualizarListaDatos_Operaciones(List<String> lista)
    {
        adapterSpinner_operacion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adapterSpinner_operacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_operacion.setAdapter(adapterSpinner_operacion);
    }

    private void ActualizarListaDatos_Colaboradores(List<String> lista)
    {
        adapterSpinner_colaborador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adapterSpinner_colaborador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_colaborador.setAdapter(adapterSpinner_colaborador);
    }

    private void ActualizarListaDatos_Tarea(List<String> lista)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lista);

        atctv_tarea.setThreshold(3); // al 3er caracter insertado mostrar los resultados que concuerdan
        atctv_tarea.setAdapter(adapter);
    }

    private void GetTareas()
    {
        String URL = ClaseGlobal.SELECT_TAREAS_ALL;
        // final List<String> listaTareas = new ArrayList<String>();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();
                        String id = jsonArray.getJSONObject(i).get("idTarea").toString();

                        // listaTareas.add(nombre);
                        listaDatosTareas.add(new Tarea(id, nombre));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });queue.add(stringRequest);

        // return listaTareas;
    }

    private void GetProyectos()
    {
        String URL = ClaseGlobal.SELECT_PROYECTOS_ALL;
        // final List<String> arraySpinner = new ArrayList<String>();
        // arraySpinner.add(msgProyecto);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String id = jsonArray.getJSONObject(i).get("idProyecto").toString();
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        // arraySpinner.add(nombre);

                        listaDatosProyectos.add(new Proyecto(id, nombre));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageDialog("Error al solicitar operaciones.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        // return arraySpinner;
    }

    private void GetOperaciones()
    {
        String URL = ClaseGlobal.SELECT_OPERACIONES_ALL;
        // final List<String> arraySpinner2 = new ArrayList<String>();
        // arraySpinner2.add(msgOperacion);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String id = jsonArray.getJSONObject(i).get("idOperacion").toString();
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        // arraySpinner2.add(nombre);

                        listaDatosOperaciones.add(new Operacion(id, nombre, ""));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MessageDialog("Error al solicitar operaciones.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        // return arraySpinner2;
    }

    private void GetColaboradores()
    {
        String URL = ClaseGlobal.SELECT_COLABORADORES_ALL;
        // final List<String> arraySpinner = new ArrayList<String>();
        // arraySpinner.add(msgColaborador);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String id = jsonArray.getJSONObject(i).get("idColaborador").toString();
                        String pseudonimo = jsonArray.getJSONObject(i).get("pseudonimo").toString();

                        // arraySpinner.add(pseudonimo);

                        listaDatosColaborador.add(new Colaborador(id, pseudonimo, ""));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageDialog("Error al solicitar colaboradores.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        // return arraySpinner;
    }

    /**
     * Buscar los objetos en la lista, el ID acorde a un nombre
     * A estas listas, al ejecutar la pantalla, se cargan todos los datos que pertencen
     * a una tabla
     * @param pNombre
     * @return
     */
    private String GetIdProyecto(String pNombre)
    {
        String id = "error"; // siempre existirá
        for (int i = 0; i < listaDatosProyectos.size(); i++)
        {
            if (pNombre.equals(listaDatosProyectos.get(i).nombre))
            {
                id = listaDatosProyectos.get(i).id;
            }
        }
        return id;
    }

    private String GetIdOperacion(String pNombre)
    {
        String id = "error"; // siempre existirá
        for (int i = 0; i < listaDatosOperaciones.size(); i++)
        {
            if (pNombre.equals(listaDatosOperaciones.get(i).nombre))
            {
                id = listaDatosOperaciones.get(i).id;
            }
        }
        return id;
    }

    private String GetIdColaborador(String pPseudonimo)
    {
        String id = "error"; // siempre existirá
        for (int i = 0; i < listaDatosColaborador.size(); i++)
        {
            if (pPseudonimo.equals(listaDatosColaborador.get(i).pseudonimo))
            {
                id = listaDatosColaborador.get(i).id;
            }
        }
        return id;
    }

    private String GetIdTarea(String pNombre)
    {
        String id = "error"; // siempre existirá
        for (int i = 0; i < listaDatosTareas.size(); i++)
        {
            if (pNombre.equals(listaDatosTareas.get(i).nombre))
            {
                id = listaDatosTareas.get(i).id;
            }
        }
        return id;
    }

    /**
     * Función llamada en onCreate.
     * Solicita primero los parametros de latitud y longitud para posteriormente ejecutar
     * la función encargada de hacer el request de los valores de humedad y temperatura
     */
    private void ParametrosAmbiente()
    {
        progressDialog.setMessage("Obteniendo la temperatura y humedad...");
        progressDialog.show();

        GetLocacion();

        if (!ClaseGlobal.latitud.equals("error") && !ClaseGlobal.longitud.equals("error"))
        {
            GetTiempo();
        }
        else
        {
            MensajeErrorConexion();
        }
        progressDialog.dismiss();
    }

    /**
     * Función encargada de obtener y hacer el request de los valores de humedad y temperatura
     */
    private void GetTiempo()
    {
        ClaseGlobal.humedad = "error"; ClaseGlobal.temperatura = "error";

        Weather.placeIdTask asyncTask = new Weather.placeIdTask(new Weather.AsyncResponse()
        {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn) {

                ClaseGlobal.humedad = weather_humidity;
                ClaseGlobal.temperatura = weather_temperature;

                if (ClaseGlobal.humedad.equals("error") || ClaseGlobal.temperatura.equals("error"))
                {
                    MensajeErrorConexion();
                }
                else
                {
                    Determinar_Proyectos_a_mostrar();

                    Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                            "Temperatura: " + ClaseGlobal.temperatura + "°" + "  |  " +
                                    "Humedad: " + ClaseGlobal.humedad + "%", Snackbar.LENGTH_LONG).show();

                }
            }
        });
        asyncTask.execute(ClaseGlobal.latitud, ClaseGlobal.longitud); //  asyncTask.execute("Latitude", "Longitude")
    }

    /**
     * Función encargada de obtener la latitud y longitud de la ubicación actual de la
     * persona
     */
    private void GetLocacion()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }
        else
        {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null)
            {
                ClaseGlobal.latitud = Double.toString(location.getLatitude());
                ClaseGlobal.longitud = Double.toString(location.getLongitude());
            }
            else
            {
                ClaseGlobal.latitud = "error"; // Caso de fallo
                ClaseGlobal.longitud = "error";

                Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                        "La funcionalidad de GPS del dispositivo se encuentra desactivada!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Despliega un cuadro en pantalla indicando que no ha sido posible obtener
     * los valores de temperatura y humedad.
     * Se pregunta si desea cancelar, reintentar o insertar los valores manualmente
     */
    private void MensajeErrorConexion()
    {
        final AlertDialog.Builder builderEliminar = new AlertDialog.Builder(this);
        builderEliminar.setTitle("Atención!");
        builderEliminar.setMessage("No es posible obtener la humedad y la temperatura!");
        builderEliminar.setCancelable(false);

        builderEliminar.setPositiveButton("MANUAL", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                InsertarParemetrosManual();
                dialog.dismiss();
            }
        });

        builderEliminar.setNegativeButton("REINTENTAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                ParametrosAmbiente();
            }
        });

        builderEliminar.setNeutralButton("CANCELAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                // ADMINISTRADOR
                if (ClaseGlobal.usuarioActual.idRolUsuario.equals("1"))
                {
                    Intent intent_ = new Intent(ObservacionActivity.this, MenuPrincipalActivity.class);
                    intent_.putExtra("ROL", "ADMINISTRADOR");
                    startActivity(intent_);
                }
                else // ANALISTA
                {
                    Intent intent_ = new Intent(ObservacionActivity.this, MenuPrincipalActivity.class);
                    intent_.putExtra("ROL", "ANALISTA");
                    startActivity(intent_);
                }
            }
        });

        AlertDialog alert = builderEliminar.create();
        alert.show();
    }

    /**
     * Función que despliega un cuadro en pantalla para insertar los valores
     * de temperatura y humedad en caso de querer realizar el procedimiento manual
     * o por problemas con GPS
     */
    public void InsertarParemetrosManual()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(ObservacionActivity.this);
        alert.setTitle("Registrar valores");

        LinearLayout layout = new LinearLayout(ObservacionActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText et_temperatura = new EditText(ObservacionActivity.this);
        et_temperatura.setHint("Temperatura");
            et_temperatura.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(et_temperatura);

        final EditText et_humedad = new EditText(ObservacionActivity.this);
        et_humedad.setHint("Humedad");
        et_humedad.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(et_humedad);

        alert.setView(layout);

        alert.setPositiveButton("REGISTRAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String pTemperatura = et_temperatura.getText().toString();
                String pHumedad = et_humedad.getText().toString();

                if (!pTemperatura.equals("") && !pHumedad .equals(""))
                {
                    ClaseGlobal.temperatura = pTemperatura;
                    ClaseGlobal.humedad = pHumedad;

                    Determinar_Proyectos_a_mostrar();

                    Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                            "Valores registrados!\n" + "Temperatura: " + ClaseGlobal.temperatura + "°" + "  |  " +
                                    "Humedad: " + ClaseGlobal.humedad + "%", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    // no insertó alguno o ninguno de loa valores

                    InsertarParemetrosManual();
                    Snackbar.make(ObservacionActivity.this.findViewById(android.R.id.content),
                            "Debe ingresar todos los valores!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                // Intent intent_menuPrincipal = new Intent(ObservacionActivity.this, MenuPrincipalActivity.class);
                // startActivity(intent_menuPrincipal);
                MensajeErrorConexion();
            }
        });

        alert.show();
    }

    /**
     * Función requisito para solicitar la latitud y longitud actual del usuario
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                GetLocacion();
                break;
        }
    }


    /**
     * Impedir que el usuario retroceda mediante el boton de atras del dispositivo
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && registroObservacion) {

            MessageDialog("Se han registrado 1 o más observaciones.\nPor favor, finalice el procedimiento!",
                    "Atención!", "Aceptar");

            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Despliega un mensaje emergente en pantalla
     * @param message
     * @param pTitulo
     * @param pLabelBoton
     */
    private void MessageDialog(String message, String pTitulo, String pLabelBoton){ // mostrar mensaje emergente
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message).setTitle(pTitulo).setPositiveButton(pLabelBoton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}