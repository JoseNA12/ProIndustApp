package reque.proyecto2.jose_.proindust_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

public class CrearEnlace_Tareas extends AppCompatActivity {

    private Spinner sp_operacion, sp_tarea;
    private Button bt_enlazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_enlace__tareas);

        sp_operacion = (Spinner) findViewById(R.id.sp_operacion_ID);
        sp_tarea = (Spinner) findViewById(R.id.sp_tarea_ID);

        bt_enlazar = (Button) findViewById(R.id.bt_enlazar_ID);
    }
}
