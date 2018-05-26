package reque.proyecto2.jose_.proindust_app.enlacesDatos;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import reque.proyecto2.jose_.proindust_app.R;
import reque.proyecto2.jose_.proindust_app.fragmentsEnlaces.FragmentEnlace_Colaboradores;
import reque.proyecto2.jose_.proindust_app.fragmentsEnlaces.FragmentEnlace_Operaciones;
import reque.proyecto2.jose_.proindust_app.fragmentsEnlaces.FragmentEnlace_Tareas;
import reque.proyecto2.jose_.proindust_app.fragmentsEnlaces.FragmentEnlace_Usuarios;

public class Enlaces_Datos extends AppCompatActivity {

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlaces_datos);

        // Barra que contiene proyectos, operaciones, tareas, colaboradores y tareas
        navigation = (BottomNavigationView) findViewById(R.id.navigation_enlaces);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Mostrar por defecto el frame de proyectos
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_enlaces, new FragmentEnlace_Tareas()).commit();
    }

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
                case R.id.navigation_enlaces_tareas:

                    // cambiar de fragmento al momento de presionar un elemento de la barra
                    fragmentTransaction.replace(R.id.frameLayout_enlaces, new FragmentEnlace_Tareas()).commit();
                    return true;

                case R.id.navigation_enlaces_operaciones:

                    fragmentTransaction.replace(R.id.frameLayout_enlaces, new FragmentEnlace_Operaciones()).commit();
                    return true;

                case R.id.navigation_enlaces_colaboradores:

                    fragmentTransaction.replace(R.id.frameLayout_enlaces, new FragmentEnlace_Colaboradores()).commit();
                    return true;

                case R.id.navigation_enlaces_usuarios:

                    fragmentTransaction.replace(R.id.frameLayout_enlaces, new FragmentEnlace_Usuarios()).commit();
                    return true;
            }

            return false;
        }
    };
}
