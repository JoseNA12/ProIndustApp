package reque.proyecto2.jose_.proindust_app.CRUDS;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import reque.proyecto2.jose_.proindust_app.ClaseGlobal;
import reque.proyecto2.jose_.proindust_app.R;
import reque.proyecto2.jose_.proindust_app.modelo.RolUsuario;
import reque.proyecto2.jose_.proindust_app.modelo.Usuario;

public class CrearUsuario extends AppCompatActivity {

    // et_nombre_ID, et_apellidos_ID, sp_rol_ID, sp_proyecto_ID, et_nombreUsuario_ID, et_contrasenia_ID, et_repetirContrasenia_ID

    private EditText et_nombre, et_apellidos, et_nombreUsuario, et_correo, et_contrasenia, et_repetirContrasenia;
    private Button bt_crear;
    private Spinner sp_rolUsuario;
    private ArrayAdapter<String> adapterSpinner_rolUsuario;
    private String protectoSeleccionado;
    private String rolSeleccionado;

    private String msgRol = "Seleccione un usuario...";
    private String msgProyecto = "Seleccione un proyecto...";

    private ProgressDialog progressDialog;

    private ArrayList<RolUsuario> listaRoles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);

        listaRoles = new ArrayList<RolUsuario>();

        et_nombre = (EditText) findViewById(R.id.et_nombre_ID);
        et_apellidos = (EditText) findViewById(R.id.et_apellidos_ID);
        et_nombreUsuario = (EditText) findViewById(R.id.et_nombreUsuario_ID);
        et_correo = (EditText) findViewById(R.id.et_correo_ID);
        et_contrasenia = (EditText) findViewById(R.id.et_contrasenia_ID);
        et_repetirContrasenia = (EditText) findViewById(R.id.et_repetirContrasenia_ID);

        sp_rolUsuario = (Spinner) findViewById(R.id.sp_rol_ID);

        adapterSpinner_rolUsuario = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GetRolesUsuario());

        adapterSpinner_rolUsuario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_rolUsuario.setAdapter(adapterSpinner_rolUsuario);

        sp_rolUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                rolSeleccionado = sp_rolUsuario.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        bt_crear = (Button) findViewById(R.id.bt_crear_ID);
        /*bt_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_CrearUsuario();
            }
        });*/

        DeterminarAccionBoton(savedInstanceState);
    }

    private void DeterminarAccionBoton(Bundle savedInstanceState)
    {
        String accion;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                accion = null;
            }
            else {
                accion = extras.getString("ACCION");

                if (accion.equals("CREAR"))
                {
                    bt_crear.setText("CREAR");
                    bt_crear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Boton_CrearUsuario();
                        }
                    });
                }
                else // MODIFICAR
                {
                    final Usuario miUsuario = (Usuario) getIntent().getSerializableExtra("OBJETO");
                    SetValoresComponentes(miUsuario);

                    bt_crear.setText("MODIFICAR");
                    bt_crear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Boton_ModificarUsuario(miUsuario);
                        }
                    });
                }
            }
        }
        else {
            accion = (String) savedInstanceState.getSerializable("ACCION");
        }

    }

    /**
     * Función atada al boton de crear usuario
     */
    private void Boton_CrearUsuario()
    {
        String nombre = et_nombre.getText().toString();
        String apellidos = et_apellidos.getText().toString();
        String nombreUsuario = et_nombreUsuario.getText().toString();
        String correo = et_correo.getText().toString();
        String contrasenia = et_contrasenia.getText().toString();
        String repetirContrasenia = et_repetirContrasenia.getText().toString();

        if(!nombre.equals("") && !apellidos.equals("") && !nombreUsuario.equals("") &&
                !contrasenia.equals("") && !repetirContrasenia.equals("") && !correo.equals("")) {

            if (!rolSeleccionado.equals(msgRol))
            {
                if (contrasenia.equals(repetirContrasenia))
                {
                    CrearUsuario(ClaseGlobal.INSERT_USUARIO +
                            "?nombre=" + nombre +
                            "&apellidos=" + apellidos +
                            "&idRolUsuario=" + GetIdRol(rolSeleccionado) +
                            "&nombreUsuario=" + nombreUsuario +
                            "&correoElectronico=" + correo +
                            "&contrasenia=" + contrasenia
                    );
                }
                else
                {
                    MessageDialog("Las contraseñas ingresadas no concuerdan!", "Error", "Aceptar");
                }
            }
            else
            {
                MessageDialog("Por favor, seleccione un rol de usuario!", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, complete todos los campos!", "Error", "Aceptar");
        }
    }

    /**
     * Encargado de enviar la peticion de inserción a la base de datos
     * @param URL
     */
    private void CrearUsuario(String URL)
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
                        // MessageDialog("Se ha creado el usuario!", "Éxito", "Aceptar");

                        Snackbar.make(CrearUsuario.this.findViewById(android.R.id.content),
                                "Se ha creado el usuario!", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        MessageDialog("Error al crear el usuario!", "Error", "Aceptar");
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

    /**
     * Obtener los roles de los usuarios (solo el nombre)
     * Administrador o Analista
     * @return
     */
    private List<String> GetRolesUsuario()
    {
        progressDialog.setMessage("Solicitando los roles de usuario...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_ROLESUSUARIOS_ALL;
        final List<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(msgRol);

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
                        String id = jsonArray.getJSONObject(i).get("idRolUsuario").toString();
                        String rol = jsonArray.getJSONObject(i).get("rol").toString();

                        listaRoles.add(new RolUsuario(id, rol));

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
                MessageDialog("Error al solicitar los roles de usuario.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    /**
     * Establecer los datos del objeto selccionado en los componentes de la pantalla
     * @param miColaborador
     */
    private void SetValoresComponentes(Usuario miUsuario)
    {
        String nombre = miUsuario.nombre;
        String apellidos = miUsuario.apellidos;
        String nombreUsuario = miUsuario.nombreUsuario;
        String correoElectronico = miUsuario.correo;
        String contrasenia = miUsuario.contrasenia;

        et_nombre.setText(nombre);
        et_apellidos.setText(apellidos);
        et_nombreUsuario.setText(nombreUsuario);
        et_correo.setText(correoElectronico);
        et_contrasenia.setText(contrasenia);
        et_repetirContrasenia.setText(contrasenia);

        // no sirve, lista vacia
        /*for(int i=0; i < adapterSpinner_rolUsuario.getCount(); i++) {
            if(listaRoles.equals(adapterSpinner_rolUsuario.getItem(i).toString())){
                sp_rolUsuario.setSelection(i);
                break;
            }
        }*/
    }

    private void Boton_ModificarUsuario(Usuario miUsuario)
    {
        String nombre = et_nombre.getText().toString();
        String apellidos = et_apellidos.getText().toString();
        String nombreUsuario = et_nombreUsuario.getText().toString();
        String correoElectronico = et_correo.getText().toString();
        String contrasenia = et_contrasenia.getText().toString();
        String repetirContrasenia = et_repetirContrasenia.getText().toString();

        if(!nombre.equals("") && !apellidos.equals("") && !nombreUsuario.equals("") &&
                !contrasenia.equals("") && !repetirContrasenia.equals("") && !correoElectronico.equals("")) {

            if (!rolSeleccionado.equals(msgRol))
            {
                if (contrasenia.equals(repetirContrasenia))
                {
                    ModificarUsuario(ClaseGlobal.UPDATE_USUARIO +
                            "?idUsuario=" + miUsuario.id +
                            "&nombre=" + nombre +
                            "&apellidos=" + apellidos +
                            "&idRolUsuario=" + GetIdRol(rolSeleccionado) +
                            "&nombreUsuario=" + nombreUsuario +
                            "&correoElectronico=" + correoElectronico +
                            "&contrasenia=" + contrasenia
                    );
                }
                else
                {
                    MessageDialog("Las contraseñas ingresadas no concuerdan!", "Error", "Aceptar");
                }
            }
            else
            {
                MessageDialog("Por favor, seleccione un rol de usuario!", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, complete todos los campos!", "Error", "Aceptar");
        }
    }

    private void ModificarUsuario(String URL)
    {
        progressDialog.setMessage("Modificando información...");
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
                        // MessageDialog("Se ha modificado el usuario!", "Éxito", "Aceptar");
                        Snackbar.make(CrearUsuario.this.findViewById(android.R.id.content),
                                "Se ha modificado el usuario!", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        MessageDialog("Error al modificar el usuario!", "Error", "Aceptar");
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

    private String GetIdRol(String pRol)
    {
        String id = "error";  // siempre existirá
        for (int i = 0; i < listaRoles.size(); i++)
        {
            if (pRol.equals(listaRoles.get(i).rol))
            {
                id = listaRoles.get(i).id;
            }
        }
        return id;
    }

}
