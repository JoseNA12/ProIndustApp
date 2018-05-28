package reque.proyecto2.jose_.proindust_app.enlacesDatos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
import reque.proyecto2.jose_.proindust_app.modelo.Operacion;
import reque.proyecto2.jose_.proindust_app.modelo.Tarea;

public class CrearEnlace_Tareas extends AppCompatActivity {

    private Spinner sp_operacion, sp_tarea;
    private ArrayAdapter<String> adapterSpinner_operacion, adapterSpinner_tarea;
    private Button bt_enlazar;

    private String operacionSeleccionada, tareaSeleccionada;

    private String msgOperacion = "Seleccione una operación...";
    private String msgTarea = "Seleccione una tarea...";

    private ArrayList<Operacion> listaOperaciones;
    private ArrayList<Tarea> listaTareas;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_enlace__tareas);

        progressDialog = new ProgressDialog(this);
        // progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);

        listaOperaciones = new ArrayList<Operacion>();
        listaTareas = new ArrayList<Tarea>();

        sp_operacion = (Spinner) findViewById(R.id.sp_operacion_ID);
        adapterSpinner_operacion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GetOperaciones());
        adapterSpinner_operacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_operacion.setAdapter(adapterSpinner_operacion);
        sp_operacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                operacionSeleccionada = sp_operacion.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        sp_tarea = (Spinner) findViewById(R.id.sp_tarea_ID);
        adapterSpinner_tarea = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GetTareas());
        adapterSpinner_tarea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_tarea.setAdapter(adapterSpinner_tarea);
        sp_tarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tareaSeleccionada = sp_tarea.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        bt_enlazar = (Button) findViewById(R.id.bt_enlazar_ID);
        bt_enlazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_Enlazar();
            }
        });
    }

    private void Boton_Enlazar()
    {
        if (!operacionSeleccionada.equals(msgOperacion))
        {
            if (!tareaSeleccionada.equals(msgTarea))
            {
                CrearEnlace(ClaseGlobal.INSERT_OPERACIONTAREA +
                        "?idOperacion=" + GetIdOperacion(operacionSeleccionada) +
                        "&idTarea=" + GetIdTarea(tareaSeleccionada));
            }
            else
            {
                MessageDialog("Por favor, seleccione una tarea", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, seleccione una operación", "Error", "Aceptar");
        }
    }

    private void CrearEnlace(String URL)
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
                        MessageDialog("Se ha creado el enlace!", "Éxito", "Aceptar");
                    }
                    else
                    {
                        MessageDialog("Error al crear el enlace!", "Error", "Aceptar");
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

    private List<String> GetOperaciones()
    {
        progressDialog.setMessage("Solicitando información...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_OPERACIONES_ALL;
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
                        String id = jsonArray.getJSONObject(i).get("idOperacion").toString();
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        arraySpinner.add(nombre);

                        listaOperaciones.add(new Operacion(id, nombre, ""));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                // progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                MessageDialog("Error al solicitar operaciones.\nIntente mas tarde!.",
                        "Error de conexión", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    private List<String> GetTareas()
    {
        String URL = ClaseGlobal.SELECT_TAREAS_ALL;
        final List<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(msgTarea);

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
                        String id = jsonArray.getJSONObject(i).get("idTarea").toString();
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        arraySpinner.add(nombre);

                        listaTareas.add(new Tarea(id, nombre));
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
                MessageDialog("Error al solicitar tareas.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    private String GetIdOperacion(String pVar)
    {
        String id = "error";

        for (int i = 0; i < listaOperaciones.size(); i++)
        {
            if (pVar.equals(listaOperaciones.get(i).nombre))
            {
                id = listaOperaciones.get(i).id;
            }
        }
        return id;
    }

    private String GetIdTarea(String pVar)
    {
        String id = "error";
        for (int i = 0; i < listaTareas.size(); i++)
        {
            if (pVar.equals(listaTareas.get(i).nombre))
            {
                id = listaTareas.get(i).id;
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
