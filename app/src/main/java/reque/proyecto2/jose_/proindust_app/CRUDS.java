package reque.proyecto2.jose_.proindust_app;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

    private ListView lv;
    private ListAdapter theAdapter;
    private BottomNavigationView navigation;
    private FloatingActionButton crear;
    private Class pestaniaActual = CrearProyecto.class; // por defecto se muestra la de proyecto

    // IP del servidor
    private String IP = "http://proindustapp.000webhostapp.com";

    // Rutas de los Web Services
    private String GET_PROYECTO = IP + "/obtener_proyectos.php";
    private String GET_PROYECTO_BY_ID = IP + "/obtener_proyecto_por_id.php";
    private String INSERT_PROYECTO = IP + "/insertar_proyecto.php";
    private String UPDATE_PROYECTO = IP + "/actualizar_proyecto.php";
    private String DELETE_PROYECTO = IP + "/eliminar_proyecto.php";

    private ObtenerWebServices hiloConexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruds);

        // Barra que contiene proyectos, operaciones, tareas, colaboradores y tareas
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String[] ninjaList = {"Jose", "Navarro", "Hola", "Holiwis", "Jelou mai frey", "Prueba 1", "Prueba 2", "Tarea X", "Colaborador X", "Prueba 3", "Prueba 4", "Prueba 5", "Prueba 6"};
        theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, ninjaList);
        lv = (ListView) findViewById(R.id.dy_lista_ID);

        lv.setAdapter(theAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


    }

    private void ConsultarListaDatos() // cada vez que se cambie de pestaña (CRUDS), consultar la respectiva lista de información
    {
        switch (pestaniaActual.getName())
        {
            case "CrearProyecto":
                hiloConexion = new ObtenerWebServices();
                hiloConexion.execute(GET_PROYECTO, "GET_PROYECTO"); // Parámetros que recibe doInBackground

                break;
            case "CrearOperacion":

                break;

            case "CrearTarea":

                break;

            case "CrearColaborador":

                break;

            case "CrearUsuario":

                break;

            default:
                break;
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_proyectos:
                    pestaniaActual = CrearProyecto.class;

                    return true;
                case R.id.navigation_operaciones:
                    pestaniaActual = CrearOperacion.class;

                    return true;
                case R.id.navigation_tareas:
                    pestaniaActual = CrearTarea.class;

                    return true;
                case R.id.navigation_colaboradores:
                    pestaniaActual = CrearColaborador.class;

                    return true;
                case R.id.navigation_usuarios:
                    pestaniaActual = CrearUsuario.class;
                    // mTextMessage.setText(R.string.title_usuarios);
                    return true;
            }
            return false;
        }
    };



    public class ObtenerWebServices extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            // PONER EL RESULTADO EN ALGUN LADO
            // super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // URL de donde queremos obtener la información

            String devuelve = "";

            if(params[1].equals("GET_PROYECTO"))
            {
                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados
                        String resultJSON = respuestaJSON.getString("estado");   // results es el nombre del campo en el JSON

                        //Vamos obteniendo todos los campos que nos interesen.


                        if (resultJSON.equals("1")) { // "1" -> si hay PROYECTOS
                            JSONArray proyectosJSON = respuestaJSON.getJSONArray("proyectos"); // existen proyectos, entonces agarro las lista "proyectos"

                            for (int i = 0; i < proyectosJSON.length(); i++)
                            {
                                devuelve = devuelve + proyectosJSON.getJSONObject(i).getString("idProyecto") + " " +
                                        proyectosJSON.getJSONObject(i).getString("nombre") + " " +
                                        proyectosJSON.getJSONObject(i).getString("descripcion") + " " +
                                        proyectosJSON.getJSONObject(i).getString("nivelConfianza") + " " +
                                        proyectosJSON.getJSONObject(i).getString("rangoInicial") + " " +
                                        proyectosJSON.getJSONObject(i).getString("rangoFinal") + " " +
                                        proyectosJSON.getJSONObject(i).getString("cantMuestreosP") + "\n";
                            }
                        }
                        else if (resultJSON.equals("2")) { // "2" -> no hay proyectos almacenados
                            devuelve = "No existen proyectos!";
                        }

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return devuelve;
            }
            // else if (params[1].equals("GET_PROYECTO"))){}

            return null;
        }
    }
}
