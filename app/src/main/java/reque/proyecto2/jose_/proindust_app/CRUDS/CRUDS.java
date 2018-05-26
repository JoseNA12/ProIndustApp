package reque.proyecto2.jose_.proindust_app.CRUDS;

import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import reque.proyecto2.jose_.proindust_app.R;
import reque.proyecto2.jose_.proindust_app.fragmentsCRUDS.FragmentColaborador;
import reque.proyecto2.jose_.proindust_app.fragmentsCRUDS.FragmentOperacion;
import reque.proyecto2.jose_.proindust_app.fragmentsCRUDS.FragmentProyecto;
import reque.proyecto2.jose_.proindust_app.fragmentsCRUDS.FragmentTarea;
import reque.proyecto2.jose_.proindust_app.fragmentsCRUDS.FragmentUsuario;


public class CRUDS extends AppCompatActivity {

    private BottomNavigationView navigation;

    private Class pestaniaActual = CrearProyecto.class; // por defecto se muestra la de proyecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruds);

        // Barra que contiene proyectos, operaciones, tareas, colaboradores y tareas
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Mostrar por defecto el frame de proyectos
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentProyecto()).commit();
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
                case R.id.navigation_proyectos:

                    // cambiar de fragmento al momento de presionar un elemento de la barra
                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentProyecto()).commit();
                    return true;

                case R.id.navigation_operaciones:

                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentOperacion()).commit();
                    return true;

                case R.id.navigation_tareas:

                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentTarea()).commit();
                    return true;

                case R.id.navigation_colaboradores:

                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentColaborador()).commit();
                    return true;

                case R.id.navigation_usuarios:

                    fragmentTransaction.replace(R.id.frameLayout_cruds, new FragmentUsuario()).commit();
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
