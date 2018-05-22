package reque.proyecto2.jose_.proindust_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CrearUsuario extends AppCompatActivity {

    // et_nombre_ID, et_apellidos_ID, sp_rol_ID, sp_proyecto_ID, et_nombreUsuario_ID, et_contrasenia_ID, et_repetirContrasenia_ID

    private EditText et_nombre, et_apellidos, et_nombreUsuario, et_contrasenia, et_repetirContrasenia;
    private Button bt_crear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        et_nombre = (EditText) findViewById(R.id.et_nombre_ID);
        et_apellidos = (EditText) findViewById(R.id.et_apellidos_ID);
        et_nombreUsuario = (EditText) findViewById(R.id.et_nombreUsuario_ID);
        et_contrasenia = (EditText) findViewById(R.id.et_contrasenia_ID);
        et_repetirContrasenia = (EditText) findViewById(R.id.et_repetirContrasenia_ID);

        bt_crear = (Button) findViewById(R.id.bt_crear_ID);
        bt_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_CrearUsuario();
            }
        });
    }

    private void Boton_CrearUsuario()
    {
        if(!et_nombre.getText().toString().equals("") && !et_apellidos.getText().toString().equals("") &&
                !et_nombreUsuario.getText().toString().equals("") && !et_contrasenia.getText().toString().equals("") &&
                !et_repetirContrasenia.getText().toString().equals("")) {

            if (et_contrasenia.getText().toString().equals(et_repetirContrasenia.getText().toString())) {

                CrearUsuario(ClaseGlobal.INSERT_USUARIO +
                        "?Nombre=" + et_nombre.getText().toString() +
                        "&Apellidos=" + et_apellidos.getText().toString() +
                        "&IdRolUsuario=123" +
                        "&NombreUsuario=" + et_nombreUsuario.getText().toString() +
                        "&Contrasenia=" + et_contrasenia.getText().toString()
                );
            }
            else
            {
                MessageDialog("Las contraseñas ingresadas no concuerdan!", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, complete todos los campos!", "Error", "Aceptar");
        }
    }

    private void CrearUsuario(String URL)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                //CrearUsuarioAux(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        MessageDialog("Se ha creado el usuario!", "Éxito", "Aceptar");
                    }
                    else
                    {
                        MessageDialog("Error al crear el usuario!", "Error", "Aceptar");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageDialog("Error, al procesar la solicitud.\nVerifique su conexión a internet!.",
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
