package reque.proyecto2.jose_.proindust_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CrearOperacion extends AppCompatActivity {

    // bt_Crear_ID, dy_listaTareas_ID, et_nombre_ID, actv_tarea_ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_operacion);

        // Boton flotante, lado derecha abajo (simbolo de una llave)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_agregarTarea_ID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }
}
