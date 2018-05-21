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

    private TextView PRUEBA;

    private ListView lv;
    private ListAdapter theAdapter;
    private BottomNavigationView navigation;
    private FloatingActionButton crear;
    private Class pestaniaActual = CrearProyecto.class; // por defecto se muestra la de proyecto

    /*// IP del servidor
    private String IP = "http://proindustapp.000webhostapp.com";

    // Rutas de los Web Services
    private String GET_PROYECTO = IP + "/obtener_proyectos.php";
    private String GET_PROYECTO_BY_ID = IP + "/obtener_proyecto_por_id.php";
    private String INSERT_PROYECTO = IP + "/insertar_proyecto.php";
    private String UPDATE_PROYECTO = IP + "/actualizar_proyecto.php";
    private String DELETE_PROYECTO = IP + "/eliminar_proyecto.php";*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruds);

        // Barra que contiene proyectos, operaciones, tareas, colaboradores y tareas
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
/* */
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

        ConsultarListaDatos();
    }

    private void ConsultarListaDatos() // cada vez que se cambie de pestaña (CRUDS), consultar la respectiva lista de información
    {
        Log.d("IMPRIMIR", pestaniaActual.getSimpleName());

        switch (pestaniaActual.getSimpleName())
        {
            case "CrearProyecto":

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

}
