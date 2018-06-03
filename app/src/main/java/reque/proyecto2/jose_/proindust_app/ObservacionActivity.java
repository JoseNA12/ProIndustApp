package reque.proyecto2.jose_.proindust_app;

// PUMA
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import reque.proyecto2.jose_.proindust_app.modelo.Colaborador;
import reque.proyecto2.jose_.proindust_app.modelo.Operacion;
import reque.proyecto2.jose_.proindust_app.modelo.Proyecto;
import reque.proyecto2.jose_.proindust_app.modelo.Tarea;

public class ObservacionActivity extends AppCompatActivity {

    // sp_proyecto_ID, sp_operacion_ID, sp_colaborador_ID, atctv_tarea_ID, tv_descripcion_ID, bt_Registrar_ID

    private Spinner sp_proyecto, sp_operacion, sp_colaborador;

    private AutoCompleteTextView atctv_tarea;

    private EditText et_descripcion;

    private Button bt_registrar;

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

    // contienen todos los datos registrados en la base de datos (solo el nombre)
    private List<String> listaProyectos_todos, listaOperaciones_todos, listaColaboradores_todos, listaTareas_todos;
    // contiene los datos filtrados por parametros, proyectos de acuerdo a usuarios, operaciones de acuerdo a proyectos, etc
    private List<String> listaProyectos_filtro, listaOperaciones_filtro, listaColaboradores_filtro, listaTareas_filtro;

    // request temperatura y humedad
    static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion);

        sp_proyecto = (Spinner) findViewById(R.id.sp_proyecto_ID);
        sp_operacion = (Spinner) findViewById(R.id.sp_operacion_ID);
        sp_colaborador = (Spinner) findViewById(R.id.sp_colaborador_ID);
        atctv_tarea = (AutoCompleteTextView) findViewById(R.id.atctv_tarea_ID);

        listaDatosProyectos = new ArrayList<Proyecto>();
        listaDatosOperaciones = new ArrayList<Operacion>();
        listaDatosColaborador = new ArrayList<Colaborador>();
        listaDatosTareas = new ArrayList<Tarea>();

        // listaProyectos_filtro = GetProyectos();
        // listaOperaciones_filtro = GetOperaciones();
        // listaColaboradores_filtro = GetColaboradores();
        // listaTareas_filtro = GetTareas();

        listaProyectos_todos = new ArrayList<String>();
        listaOperaciones_todos = new ArrayList<String>();
        listaColaboradores_todos = new ArrayList<String>();
        listaTareas_todos = new ArrayList<String>();

        et_descripcion = (EditText) findViewById(R.id.tv_descripcion_ID);
        bt_registrar = (Button) findViewById(R.id.bt_Registrar_ID);

        sp_proyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                proyectoSeleccionado = sp_proyecto.getSelectedItem().toString();

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

                Boton_RegistrarMuestra();
            }
        });

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Llama DeterminarProyectos()
        ParametrosAmbiente(); // Temperatura y humedad
    }

    /**
     * GetTiempo(): apenas se registren las valores (temp. y humedad) cargar los proyectos
     * InsertarParemetrosManual(): apenas se inserten los valores (temp. y humedad) cargar los proyectos
     */
    private void DeterminarProyectos()
    {
        if (ClaseGlobal.usuarioActual.idRolUsuario.equals("2")) // Analista
        {
            listaProyectos_filtro = Get_Proyectos_activos_de_usuario(ClaseGlobal.usuarioActual.id);
            ActualizarListaDatos_Proyectos(listaProyectos_filtro);
        }
        else // administrador
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageDialog("Error al solicitar operaciones.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

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

    /**
     * Función atada al boton de registrar
     */
    private void Boton_RegistrarMuestra()
    {
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

        String tipoMuestra = "1"; //Esto debería ser una variable global que cambie cuando se alcance
        //el limite de 30 muestreos preliminares

        String tarea = atctv_tarea.getText().toString();
        String comentario = et_descripcion.getText().toString();
        if(comentario.equals("")){
            comentario = "Sin comentario";
        }

        if(tarea.equals("") || proyectoSeleccionado.equals("") || operacionSeleccionada.equals("") || colaboradorSeleccionado.equals("")){
            MessageDialog("Es necesario que complete todos los datos para continuar", "Error", "Aceptar");
        } else {
            String idProyecto = "-1";
            String idColaborador = "-1";
            String idTarea = "-1";
            String idOperacion = "-1";

            for(int i=0; i < listaDatosProyectos.size(); i++)
            {
                if(listaDatosProyectos.get(i).nombre.equals(proyectoSeleccionado)){
                    idProyecto = listaDatosProyectos.get(i).id;
                }
            }

            for(int i=0; i < listaDatosColaborador.size(); i++){
                if(listaDatosColaborador.get(i).pseudonimo.equals(colaboradorSeleccionado))
                {
                    idColaborador = listaDatosColaborador.get(i).id;
                }
            }

            for(int i = 0; i < listaDatosOperaciones.size(); i++){
                if(listaDatosOperaciones.get(i).nombre.equals(operacionSeleccionada))
                {
                    idOperacion = listaDatosOperaciones.get(i).id;
                }
            }

            for(int i=0; i < listaDatosTareas.size(); i++)
            {
                if(listaDatosTareas.get(i).nombre.equals(tarea)){
                    idTarea = listaDatosTareas.get(i).id;
                }
            }

            RegistrarMuestra(ClaseGlobal.INSERT_MUESTRA +
                    "?comentario=" + comentario +
                    "&fecha_hora=" + hourdateFormat.format(date).toString() +
                    "&humedad=" + ClaseGlobal.humedad +
                    "&idTipoMuestra=" + tipoMuestra +
                    "&temperatura=" + ClaseGlobal.temperatura +
                    "&idUsuario=" + ClaseGlobal.usuarioActual.id +
                    "&idColaborador=" + idColaborador +
                    "&idOperacion=" + idOperacion +
                    "&idTarea=" + idTarea);
        }
    }

    /**
     * Función encargada de realizar el request a la base de datos para almacenar la muestra
     * @param URL
     */
    private void RegistrarMuestra(String URL)
    {
        progressDialog.setMessage("Insertando nueva información...");
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
                                "Se ha creado la muestra!", Snackbar.LENGTH_SHORT).show();
                        // MessageDialog("Se ha creado la muestra!", "Éxito", "Aceptar");
                    }
                    else {
                        MessageDialog("Error al crear la muestra!", "Error", "Aceptar");
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
                    DeterminarProyectos();

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

                    DeterminarProyectos();

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