package reque.proyecto2.jose_.proindust_app.CRUDS;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

import reque.proyecto2.jose_.proindust_app.ClaseGlobal;
import reque.proyecto2.jose_.proindust_app.R;
import reque.proyecto2.jose_.proindust_app.modelo.Actividad;
import reque.proyecto2.jose_.proindust_app.modelo.Tarea;

public class CrearTarea extends AppCompatActivity {

    // et_nombreTarea_ID, et_descripcion_ID, sp_tipoActividad_ID,

    private EditText et_nombreTarea, et_descripcion;
    private Spinner sp_tipoActividad;
    private Button bt_crear;
    private String actividadSeleccionada;
    private ArrayAdapter<String> adapterSpinner_actividades;
    private ProgressDialog progressDialog;

    private ArrayList<Actividad> listaActividades;

    private String msgActividad = "Seleccione el tipo de actividad...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);

        listaActividades = new ArrayList<Actividad>();

        et_nombreTarea = (EditText) findViewById(R.id.et_nombreTarea_ID);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion_ID);

        sp_tipoActividad = (Spinner) findViewById(R.id.sp_tipoActividad_ID);
        adapterSpinner_actividades = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GetActividades());
        adapterSpinner_actividades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_tipoActividad.setAdapter(adapterSpinner_actividades);

        sp_tipoActividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                actividadSeleccionada = sp_tipoActividad.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        bt_crear = (Button) findViewById(R.id.bt_crear_ID);
        bt_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_CrearTarea();
            }
        });
    }

    /**
     * Función atada al boton de crear tarea
     */
    private void Boton_CrearTarea()
    {
        String nombre = et_nombreTarea.getText().toString();
        String descripcion = et_descripcion.getText().toString();

        if (!nombre.equals(""))
        {
            if (!actividadSeleccionada.equals(msgActividad))
            {
                CrearTarea(ClaseGlobal.INSERT_TAREA +
                        "?nombre=" + nombre +
                        "&descripcion=" + descripcion +
                        "&idActividad=" + GetIdActividad(actividadSeleccionada));
            }
            else
            {
                MessageDialog("Por favor, seleccione el tipo de actividad para la tarea!", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, ingrese un nombre para la tarea!", "Error", "Aceptar");
        }
    }

    /**
     * Encargado de enviar la peticion de inserción a la base de datos
     * @param URL
     */
    private void CrearTarea(String URL)
    {
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        MessageDialog("Se ha creado la tarea!", "Éxito", "Aceptar");
                    }
                    else
                    {
                        MessageDialog("Error al crear la tarea!", "Error", "Aceptar");
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
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);
    }

    /**
     * Obtener todos los datos acerca de las actividades
     * @return
     */
    private List<String> GetActividades()
    {
        progressDialog.setMessage("Solicitando los tipos de actividades...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_ACTIVIDADES_ALL;
        final List<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(msgActividad);

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
                        String id = jsonArray.getJSONObject(i).get("idActividad").toString();
                        String rol = jsonArray.getJSONObject(i).get("tipo").toString();

                        listaActividades.add(new Actividad(id, rol));

                        arraySpinner.add(rol);
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
                MessageDialog("Error al solicitar los tipos de actividades.\nIntente mas tarde!.",
                        "Error de conexión", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    private String GetIdActividad(String pTipo)
    {
        String id = "1"; // siempre existirá
        for (int i = 0; i < listaActividades.size(); i++)
        {
            if (pTipo.equals(listaActividades.get(i).tipo))
            {
                id = listaActividades.get(i).id;
            }
        }
        return id;
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
