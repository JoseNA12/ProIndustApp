package reque.proyecto2.jose_.proindust_app;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

public class Muestreo extends AppCompatActivity {

    // sp_proyecto_ID, sp_operacion_ID, sp_colaborador_ID, atctv_tarea_ID, tv_descripcion_ID, bt_Registrar_ID

    private Spinner sp_proyecto, sp_operacion, sp_colaborador;

    private AutoCompleteTextView txt_tarea;

    private EditText et_descripcion;

    private Button bt_registrar;

    private ProgressDialog progressDialog;

    private String proyectoSeleccionado;
    private String operacionSeleccionada;
    private String colaboradorSeleccionado;

    private List<String> listaTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestreo);

        listaTarea = GetTareas();

        sp_proyecto = (Spinner) findViewById(R.id.sp_proyecto_ID);
        sp_operacion = (Spinner) findViewById(R.id.sp_operacion_ID);
        sp_colaborador = (Spinner) findViewById(R.id.sp_colaborador_ID);

        txt_tarea = (AutoCompleteTextView) findViewById(R.id.atctv_tarea_ID);

        et_descripcion = (EditText) findViewById(R.id.tv_descripcion_ID);

        bt_registrar = (Button) findViewById(R.id.bt_Registrar_ID);

        sp_proyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                proyectoSeleccionado = sp_proyecto.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                proyectoSeleccionado = null;
            }
        });

        sp_operacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operacionSeleccionada = sp_operacion.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                operacionSeleccionada = null;
            }
        });

        sp_colaborador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colaboradorSeleccionado = sp_colaborador.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                colaboradorSeleccionado = null;
            }
        });

        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_RegistrarMuestra();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_muestreo, listaTarea);

        txt_tarea.setThreshold(3);
        txt_tarea.setAdapter(adapter);

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);
    }

    private void Boton_RegistrarMuestra() {
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

        String humedad = "0";
        String temperatura = "0";

        String tipoMuestra = "1"; //Esto debería ser una variable global que cambie cuando se alcance
        //el limite de 30 muestreos preliminares

        String tarea = txt_tarea.getText().toString();
        String comentario = et_descripcion.getText().toString();
        if(comentario.equals("")){
            comentario = "No agrega comentario";
        }

        if(tarea.equals("") || proyectoSeleccionado.equals("") || operacionSeleccionada.equals("") || colaboradorSeleccionado.equals("")){
            MessageDialog("es necesario que complete todos los datos para continuar", "Error", "Aceptar");
        } else {
            /*RegistrarMuestra(ClaseGlobal.INSERT_MUESTRA +
                    "?comentario=" + comentario +
                    "&fecha_hora=" + hourdateFormat.format(date) +
                    "&humedad=" + humedad +//Hay que buscar la manera de obtenerla
                    "&idTipoMuestra=" + tipoMuestra +
                    "&temperatura=" + temperatura +//Hay que buscar la manera de obtenerla
                    "idUsuario=" + ClaseGlobal.ID_USUARIO_ACTUAL +
                    "idColaborador=" + GetIdColaborador(colaboradorSeleccionado) +//Cambiar por URL
                    "idProyecto=" + GetIdProyecto(proyectoSeleccionado) +//Cambiar por URL
                    "idTarea=" + GetIdtarea(tarea));//Cambiar por URL*/
        }
    }

    private void RegistrarMuestra(String URL){
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        MessageDialog("Se ha creado la tarea!", "Éxito", "Aceptar");
                    } else {
                        MessageDialog("Error al crear la tarea!", "Error", "Aceptar");
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

    private List<String> GetTareas()
    {
        String URL = ClaseGlobal.SELECT_TAREAS_ALL;
        final List<String> listaTareas = new ArrayList<String>();

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
                        String tarea = jsonArray.getJSONObject(i).get("nombre").toString();

                        listaTareas.add(tarea);
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

        return listaTareas;
    }

    private String GetIdProyecto(String URL)
    {
        final List<String> arraySpinner = new ArrayList<String>();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    String idProyecto = jsonArray.getJSONObject(0).get("idProyecto").toString();
                    arraySpinner.add(idProyecto);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner.get(0);
    }

    private String GetIdColaborador(String URL)
    {
        final List<String> arraySpinner = new ArrayList<String>();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    String idProyecto = jsonArray.getJSONObject(0).get("idColaborador").toString();
                    arraySpinner.add(idProyecto);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner.get(0);
    }

    private String GetIdtarea(String URL)
    {
        final List<String> arraySpinner = new ArrayList<String>();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    String idProyecto = jsonArray.getJSONObject(0).get("idTarea").toString();
                    arraySpinner.add(idProyecto);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner.get(0);
    }

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
