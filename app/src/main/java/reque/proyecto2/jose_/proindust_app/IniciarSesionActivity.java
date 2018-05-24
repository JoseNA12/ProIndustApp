package reque.proyecto2.jose_.proindust_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IniciarSesionActivity extends AppCompatActivity {


    Button boton_Ingresar;
    EditText campoTexto_nombreUsuario, campoTexto_contasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        campoTexto_nombreUsuario = (EditText) findViewById(R.id.et_usuario_ID);
        campoTexto_contasenia = (EditText) findViewById(R.id.et_contrasenia_ID);

        boton_Ingresar = (Button)findViewById(R.id.bt_ingresar_ID);

        boton_Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_menuPrincipal = new Intent(IniciarSesionActivity.this, MenuPrincipal.class);
                startActivity(intent_menuPrincipal);
            }
        });

    }
}
