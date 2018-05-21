package reque.proyecto2.jose_.proindust_app;

import android.content.DialogInterface;
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
                CrearUsuario();
            }
        });
    }

    private void CrearUsuario()
    {
        if(!et_nombre.getText().toString().equals("") && !et_apellidos.getText().toString().equals("") &&
                !et_nombreUsuario.getText().toString().equals("") && !et_contrasenia.getText().toString().equals("") &&
                !et_repetirContrasenia.getText().toString().equals("")) {


            createUser(ClaseGlobal.INSERT_USUARIO +
                    "?Nombre=" + et_nombre.getText().toString() +
                    "&Apellidos=" + "PRUEBAS" +
                    "&IdRol=123" +
                    "&IdProyecto=123" +
                    "&NombreUsuario=CaraBotella" +
                    "&Contrasenia=123");

            correctMessageDialog("1");


            //createUser(ClaseGlobal.Usuario_Insert+"?Usuario="+txt_NombreUsuario.getText().toString()+"&Password="+txt_Password.getText().toString()+"&Tipo_Usuario=Asist");


        }else{
            errorMessageDialog("Llene todos las casillas para crear el usuario");
        }
    }

    private void createUser(String URL){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                Log.d("IMPRIMIR", response);
                createUserAux(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorMessageDialog("No se pudo conectar al servidor");
            }
        });queue.add(stringRequest);
    }

    private void createUserAux(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);

            if(jsonObject.getString("status").equals("false"))
            {
                errorMessageDialog("No se pudo hacer xd");
            }
            else
            {
                correctMessageDialog("Si se pudo hacer alv");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void errorMessageDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message).setTitle("Error").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void correctMessageDialog(String message){ // mostrar mensaje emergente
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message).setTitle("Éxito").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
