package reque.proyecto2.jose_.proindust_app;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import reque.proyecto2.jose_.proindust_app.fragmentsMuestreo.FragmentMuestreo_Crear;
import reque.proyecto2.jose_.proindust_app.fragmentsMuestreo.FragmentMuestreo_Modificar;

public class MuestreoActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestreo);

        // Barra que contiene proyectos, operaciones, tareas, colaboradores y tareas
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Mostrar por defecto el frame de crear muestreos
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_muestreo, new FragmentMuestreo_Crear()).commit();
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
                case R.id.navigation_crear_muestreo:

                    // cambiar de fragmento al momento de presionar un elemento de la barra
                    fragmentTransaction.replace(R.id.frameLayout_muestreo, new FragmentMuestreo_Crear()).commit();
                    return true;

                case R.id.navigation_modificar_muestreo:

                    fragmentTransaction.replace(R.id.frameLayout_muestreo, new FragmentMuestreo_Modificar()).commit();
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
