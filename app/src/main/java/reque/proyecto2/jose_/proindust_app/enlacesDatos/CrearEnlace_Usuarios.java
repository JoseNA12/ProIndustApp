package reque.proyecto2.jose_.proindust_app.enlacesDatos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import reque.proyecto2.jose_.proindust_app.R;

public class CrearEnlace_Usuarios extends AppCompatActivity {

    private Spinner sp_proyecto, sp_usuario;
    private Button bt_enlazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_enlace__usuarios);

        sp_proyecto = (Spinner) findViewById(R.id.sp_proyecto_ID);
        sp_usuario = (Spinner) findViewById(R.id.sp_usuario_ID);

        bt_enlazar = (Button) findViewById(R.id.bt_enlazar_ID);
    }
}
