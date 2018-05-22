package reque.proyecto2.jose_.proindust_app;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CRUDS extends AppCompatActivity {

    private ListView listaComponente;
    // private ArrayList<String> listaElementos = new ArrayList<String>();
    private ListAdapter theAdapter;

    private BottomNavigationView navigation;
    private FloatingActionButton crear;
    private Class pestaniaActual = CrearProyecto.class; // por defecto se muestra la de proyecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruds);

        // Barra que contiene proyectos, operaciones, tareas, colaboradores y tareas
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        
        listaComponente = (ListView) findViewById(R.id.dy_lista_ID);

        listaComponente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                PopupMenu popup = new PopupMenu(CRUDS.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(CRUDS.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        if (item.getTitle().equals("Información"))
                        {
                            Toast.makeText(CRUDS.this,"Información", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                            if (item.getTitle().equals("Modificar"))
                            {

                                Toast.makeText(CRUDS.this,"Modificar", Toast.LENGTH_SHORT).show();
                            }
                            else // Eliminar
                            {
                                Toast.makeText(CRUDS.this,"Eliminar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        // Boton flotante, lado derecha abajo (simbolo de una llave)
        crear = (FloatingActionButton) findViewById(R.id.fab_crear_ID);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CRUDS.this, pestaniaActual);
                startActivity(myIntent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        ConsultarListaDatos();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        /**
         * Permite obtener todos los datos de cada CRUD al momento de seleccionar en pantalla
         * algun componente de la barra
         * Se utiliza "pestaniaActual" para actualizar el identificador y asi saber en cual pestalla
         * estamos observando
         * @param item
         * @return
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_proyectos:
                    pestaniaActual = CrearProyecto.class;
                    ConsultarListaDatos();
                    return true;

                case R.id.navigation_operaciones:
                    pestaniaActual = CrearOperacion.class;
                    ConsultarListaDatos();
                    return true;

                case R.id.navigation_tareas:
                    pestaniaActual = CrearTarea.class;
                    ConsultarListaDatos();
                    return true;

                case R.id.navigation_colaboradores:
                    pestaniaActual = CrearColaborador.class;
                    ConsultarListaDatos();
                    return true;

                case R.id.navigation_usuarios:
                    pestaniaActual = CrearUsuario.class;
                    ConsultarListaDatos();
                    return true;
            }

            return false;
        }
    };

    /**
     * Permite obtener todos los datos de cada actividad (proyectos, operaciones, etc. Mediante una
     * etiqueta php) al momento de cambiar de pestalla y finalmente actualizar la tabla ListView.
     * Funcion llamada mediante el listener del componente: BottomNavigationView navigation
     */
    private void ConsultarListaDatos() // cada vez que se cambie de pestaña (CRUDS), consultar la respectiva lista de información
    {
        switch (pestaniaActual.getSimpleName())
        {
            case "CrearProyecto": // CREAR es solo por nombre, en teoria deberia ser Proyecto xd
                ConsultarDatosTablas(ClaseGlobal.SELECT_PROYECTOS_ALL, "nombre");
                break;
            case "CrearOperacion":
                ConsultarDatosTablas(ClaseGlobal.SELECT_OPERACIONES_ALL, "nombre");
                break;

            case "CrearTarea":
                ConsultarDatosTablas(ClaseGlobal.SELECT_TAREAS_ALL, "nombre");
                break;

            case "CrearColaborador":
                ConsultarDatosTablas(ClaseGlobal.SELECT_COLABORADORES_ALL, "pseudonimo");
                break;

            case "CrearUsuario":
                ConsultarDatosTablas(ClaseGlobal.SELECT_USUARIOS_ALL, "nombre");
                break;

            default:
                break;
        }

    }

    /***
     * Permite actualizar los datos que se despliegan del componente ListaView
     * @param lista: Lista con los elementos a refrescar
     */
    private void ActualizarListView(ArrayList<String> lista)
    {
        theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, lista);
        listaComponente.setAdapter(theAdapter);
    }

    /***
     * Funcion que obtiene las consultas hechas mediante los SELECT_*****_ALL.php
     * SOLO SE CONSULTA UNA ETIQUETA!
     * @param URL: Direccion web del archivo php de la consulta
     * @param pEtiqueta: Etiqueta del php, se obtendra el nombre por ejemplo, entonces es 'nombre'
     */
    private void ConsultarDatosTablas(String URL, final String pEtiquetaPHP)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    // ArrayList<String> lista = new ArrayList<String>();
                    ArrayList<String> listaElementos = new ArrayList<String>();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String nombreUsuario = jsonArray.getJSONObject(i).get(pEtiquetaPHP).toString();
                        listaElementos.add(nombreUsuario);
                    }

                    if (listaElementos.size() != 0)
                    {
                        ActualizarListView(listaElementos);
                    }
                    else
                    {
                        MessageDialog("Aun no hay registros de información almacenados!",
                                "Atención", "Aceptar");
                    }

                }
                catch (JSONException e ) { e.printStackTrace(); };
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageDialog("Error al solicitar los datos.\nVerifique su conexión a internet!.",
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
