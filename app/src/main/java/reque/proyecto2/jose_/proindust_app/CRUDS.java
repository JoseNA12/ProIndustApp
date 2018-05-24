package reque.proyecto2.jose_.proindust_app;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    private BottomNavigationView navigation;

    private Class pestaniaActual = CrearProyecto.class; // por defecto se muestra la de proyecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruds);

        // Barra que contiene proyectos, operaciones, tareas, colaboradores y tareas
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Mostrar por defecto el frame de proyectos
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentProyecto()).commit();
    }

    /**
     * Listener de la barra de inferior (Proyecto, Operaciones, etc)
     */
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

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_proyectos:

                    // cambiar de fragmento al momento de presionar un elemento de la barra
                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentProyecto()).commit();
                    return true;

                case R.id.navigation_operaciones:

                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentOperacion()).commit();
                    return true;

                case R.id.navigation_tareas:

                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentTarea()).commit();
                    return true;

                case R.id.navigation_colaboradores:

                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentColaborador()).commit();
                    return true;

                case R.id.navigation_usuarios:

                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentUsuario()).commit();
                    return true;
            }

            return false;
        }
    };

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
