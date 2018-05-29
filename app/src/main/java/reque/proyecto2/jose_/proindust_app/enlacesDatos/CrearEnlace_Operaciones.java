package reque.proyecto2.jose_.proindust_app.enlacesDatos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import reque.proyecto2.jose_.proindust_app.modelo.Operacion;
import reque.proyecto2.jose_.proindust_app.modelo.Proyecto;
import reque.proyecto2.jose_.proindust_app.modelo.Tarea;

public class CrearEnlace_Operaciones extends AppCompatActivity {

    private Spinner sp_proyecto, sp_operacion;
    private ArrayAdapter<String> adapterSpinner_proyecto, adapterSpinner_operacion;
    private Button bt_enlazar;

    private String proyectoSeleccionado, operacionSeleccionada;

    private String msgProyecto = "Seleccione un proyecto...";
    private String msgOperacion = "Seleccione una operación...";

    private ArrayList<Proyecto> listaProyectos;
    private ArrayList<Operacion> listaOperaciones;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_enlace__operaciones);

        sp_proyecto = (Spinner) findViewById(R.id.sp_proyecto_ID);
        sp_operacion = (Spinner) findViewById(R.id.sp_operacion_ID);

        bt_enlazar = (Button) findViewById(R.id.bt_enlazar_ID);

        progressDialog = new ProgressDialog(this);
        // progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);

        listaProyectos = new ArrayList<Proyecto>();
        listaOperaciones = new ArrayList<Operacion>();

        adapterSpinner_proyecto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GetProyectos());
        adapterSpinner_proyecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_proyecto.setAdapter(adapterSpinner_proyecto);
        sp_proyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                proyectoSeleccionado = sp_proyecto.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

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

        bt_enlazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_Enlazar();
            }
        });
    }

    private void Boton_Enlazar()
    {
        if (!proyectoSeleccionado.equals(msgProyecto))
        {
            if (!operacionSeleccionada.equals(msgOperacion))
            {
                CrearEnlace(ClaseGlobal.INSERT_PROYECTOOPERACION +
                        "?idProyecto=" + GetIdProyecto(proyectoSeleccionado) +
                        "&idOperacion=" + GetIdOperacion(operacionSeleccionada));
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
                        // MessageDialog("Se ha creado el enlace!", "Éxito", "Aceptar");

                        Snackbar.make(CrearEnlace_Operaciones.this.findViewById(android.R.id.content),
                                "Se ha creado el enlace!", Snackbar.LENGTH_SHORT).show();
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

    private List<String> GetProyectos()
    {
        progressDialog.setMessage("Solicitando proyectos...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_PROYECTOS_ALL;
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
                        String id = jsonArray.getJSONObject(i).get("idProyecto").toString();
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        arraySpinner.add(nombre);

                        listaProyectos.add(new Proyecto(id, nombre));
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

    private List<String> GetOperaciones()
    {
        progressDialog.setMessage("Solicitando operaciones...");
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

    private String GetIdProyecto(String pVar)
    {
        String id = "error"; // siempre existirá
        for (int i = 0; i < listaProyectos.size(); i++)
        {
            if (pVar.equals(listaProyectos.get(i).nombre))
            {
                id = listaProyectos.get(i).id;
            }
        }
        return id;
    }

    private String GetIdOperacion(String pVar)
    {
        String id = "error"; // siempre existirá
        for (int i = 0; i < listaOperaciones.size(); i++)
        {
            if (pVar.equals(listaOperaciones.get(i).nombre))
            {
                id = listaOperaciones.get(i).id;
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
