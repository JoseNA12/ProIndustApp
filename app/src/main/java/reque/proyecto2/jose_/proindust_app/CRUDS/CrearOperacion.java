package reque.proyecto2.jose_.proindust_app.CRUDS;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import reque.proyecto2.jose_.proindust_app.ClaseGlobal;
import reque.proyecto2.jose_.proindust_app.R;


public class CrearOperacion extends AppCompatActivity {

    // bt_Crear_ID, lv_listaTareas_ID, et_nombre_ID, actv_tarea_ID

    private ProgressDialog progressDialog;

    private EditText et_nombre;
    private Button bt_crear;
    private EditText et_descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_operacion);

        bt_crear = (Button) findViewById(R.id.bt_crear_ID);
        et_nombre = (EditText) findViewById(R.id.et_nombre_ID);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion_ID);

        bt_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_CrearOperacion();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);

    }

    private void Boton_CrearOperacion(){
        String nombre = et_nombre.getText().toString();
        String descripcion = et_descripcion.getText().toString();

        if(nombre.equals(""))
        {
            MessageDialog("Por favor, ingrese un nombre para la operación!", "Error", "Aceptar");
        }
        else
        {
                CrearOperacion(ClaseGlobal.INSERT_OPERACION +
                        "?nombre=" + nombre +
                        "&descripcion=" + descripcion);
        }
    }

    private void CrearOperacion(String URL)
    {
        Log.d("PUTA", URL);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.getString("status").equals("false"))
                    {
                        MessageDialog("Error al crear la operación!", "Error", "Aceptar");
                    }
                    else
                    {
                        MessageDialog("Se ha creado la operación!", "Éxito", "Aceptar");
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
                        "Error",
                        "Aceptar");
            }
        });
        queue.add(stringRequest);
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
