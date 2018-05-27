package reque.proyecto2.jose_.proindust_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import reque.proyecto2.jose_.proindust_app.modelo.OperacionTarea;

public class IniciarSesionActivity extends AppCompatActivity {


    private Button bt_Ingresar;
    private EditText et_nombreUsuario, et_contrasenia;

    private ProgressDialog progressDialog;

    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);

        et_nombreUsuario = (EditText) findViewById(R.id.et_nombreUsuario_ID);
        et_contrasenia = (EditText) findViewById(R.id.et_contrasenia_ID);

        bt_Ingresar = (Button)findViewById(R.id.bt_ingresar_ID);

        bt_Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Boton_IniciarSesion();
            }
        });
    }

    /**
     * Funcion atada al boton de iniciar sesión
     */
    private void Boton_IniciarSesion()
    {
        String nombreUsuario = et_nombreUsuario.getText().toString();
        String contrasenia = et_contrasenia.getText().toString();

        if (!nombreUsuario.equals(""))
        {
            if (!contrasenia.equals(""))
            {
                IniciarSesionRequest(ClaseGlobal.INICIAR_SESION +
                        "?nombreUsuario=" + nombreUsuario +
                        "&contrasenia=" + contrasenia);
            }
            else
            {
                MessageDialog("Por favor, ingrese la contraseña!", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, ingrese el nombre de usuario!", "Error", "Aceptar");
        }

        Intent intent_menuPrincipal = new Intent(IniciarSesionActivity.this, MenuPrincipal.class);
        startActivity(intent_menuPrincipal);
    }

    private void IniciarSesionRequest(String URL)
    {
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("value");

                        idUsuario = jsonArray.getJSONObject(0).get("idUsuario").toString();
                    }
                    else
                    {
                        idUsuario = "error";
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
                MessageDialog("Error al solicitar datos.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

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
