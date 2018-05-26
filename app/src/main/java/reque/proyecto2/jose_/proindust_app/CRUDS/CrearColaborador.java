package reque.proyecto2.jose_.proindust_app.CRUDS;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class CrearColaborador extends AppCompatActivity {

    // et_pseudonimo_ID, sp_tipoActividad_ID, tv_descripcion_ID, bt_crear_ID

    private EditText et_pseudonimo, et_descripcion;
    private Button bt_crear;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_colaborador);

        et_pseudonimo = (EditText) findViewById(R.id.et_pseudonimo_ID);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion_ID);

        bt_crear = (Button) findViewById(R.id.bt_crear_ID);
        bt_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_CrearColaborador();
            }
        });

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);
    }

    private void Boton_CrearColaborador(){
        String pseudonimo = et_pseudonimo.getText().toString();
        String descripcion = et_descripcion.getText().toString();

        if(!pseudonimo.equals(""))
        {
                CrearColaborador(ClaseGlobal.INSERT_COLABORADOR +
                        "?pseudonimo=" + pseudonimo +
                        "&descripcion=" + descripcion );
        }
        else
        {
            MessageDialog("Por favor, llene los campos de texto requeridos.","Error", "Aceptar");
        }
    }

    private void CrearColaborador(String URL){
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        MessageDialog("Se ha creado el colaborador.", "Éxito", "Aceptar");
                    }
                    else
                    {
                        MessageDialog("Error al crear el colaborador.", "Error", "Aceptar");
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
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde.","Error de conexión","Aceptar");
            }
        });queue.add(stringRequest);
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
