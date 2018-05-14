package reque.proyecto2.jose_.proindust_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IniciarSesionActivity extends AppCompatActivity {

    Button boton_Ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

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
