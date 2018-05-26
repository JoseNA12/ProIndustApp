package reque.proyecto2.jose_.proindust_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CrearColaborador extends AppCompatActivity {

    // et_pseudonimo_ID, sp_tipoActividad_ID, tv_descripcion_ID, bt_crear_ID

    private EditText et_pseudonimo, et_descripcion;
    private Spinner sp_tipoActividad;
    private Button bt_crear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_colaborador);

        et_pseudonimo = (EditText) findViewById(R.id.et_pseudonimo_ID);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion_ID);
        sp_tipoActividad = (Spinner) findViewById(R.id.sp_tipoActividad_ID);
        bt_crear = (Button) findViewById(R.id.bt_crear_ID);
    }
}
