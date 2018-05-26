package reque.proyecto2.jose_.proindust_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IniciarSesionActivity extends AppCompatActivity {


    private Button bt_Ingresar;
    private EditText et_nombreUsuario, et_contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        et_nombreUsuario = (EditText) findViewById(R.id.et_nombreUsuario_ID);
        et_contrasenia = (EditText) findViewById(R.id.et_contrasenia_ID);

        bt_Ingresar = (Button)findViewById(R.id.bt_ingresar_ID);

        bt_Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Boton_IniciarSesion();
            }
        });
    }

    /**
     * Funcion atada al boton de iniciar sesión
     */
    private void Boton_IniciarSesion()
    {
        String nombreUsuario = et_nombreUsuario.getText().toString();
        String contrasenia = et_contrasenia.getText().toString();

        if (!nombreUsuario.equals(""))
        {
            if (!contrasenia.equals(""))
            {

            }
            else
            {
                MessageDialog("Por favor, ingrese la contraseña!", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, ingrese el nombre de usuario!", "Error", "Aceptar");
        }

        Intent intent_menuPrincipal = new Intent(IniciarSesionActivity.this, MenuPrincipal.class);
        startActivity(intent_menuPrincipal);
    }

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
