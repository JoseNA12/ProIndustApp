package reque.proyecto2.jose_.proindust_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class CrearOperacion extends AppCompatActivity {

    // bt_Crear_ID, lv_listaTareas_ID, et_nombre_ID, actv_tarea_ID

    private FloatingActionButton fab;
    private EditText et_nombre;
    private Button bt_crear;
    private ListView lv_listaTareas;
    private AutoCompleteTextView actv_tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_operacion);

        bt_crear = (Button) findViewById(R.id.bt_crear_ID);
        et_nombre = (EditText) findViewById(R.id.et_nombre_ID);
        lv_listaTareas = (ListView) findViewById(R.id.lv_listaTareas_ID);
        actv_tarea = (AutoCompleteTextView) findViewById(R.id.actv_tarea_ID);

        // Boton flotante, lado derecha abajo (simbolo de una llave)
        fab = (FloatingActionButton) findViewById(R.id.fab_agregarTarea_ID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        String[] fruits = {"Apple", "Antes", "Ahora", "Banana", "Cherry", "Date", "Datos", "Grape", "Kiwi", "Mango", "Pear"};

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, fruits);

        // Getting the instance of AutoCompleteTextView

        actv_tarea.setThreshold(1); //will start working from first character

        actv_tarea.setAdapter(adapter); //setting the adapter data into the AutoCompleteTextView

    }
}
