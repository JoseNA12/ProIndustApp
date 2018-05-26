package reque.proyecto2.jose_.proindust_app.enlacesDatos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import reque.proyecto2.jose_.proindust_app.R;

public class CrearEnlace_Operaciones extends AppCompatActivity {

    private Spinner sp_proyecto, sp_operacion;
    private Button bt_enlazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_enlace__operaciones);

        sp_proyecto = (Spinner) findViewById(R.id.sp_proyecto_ID);
        sp_operacion = (Spinner) findViewById(R.id.sp_operacion_ID);

        bt_enlazar = (Button) findViewById(R.id.bt_enlazar_ID);
    }
}
