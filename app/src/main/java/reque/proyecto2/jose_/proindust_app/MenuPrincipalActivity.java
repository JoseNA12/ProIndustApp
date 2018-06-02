package reque.proyecto2.jose_.proindust_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import reque.proyecto2.jose_.proindust_app.CRUDS.CRUDS;
import reque.proyecto2.jose_.proindust_app.enlacesDatos.Enlaces_Datos;

public class MenuPrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button bt_Muestreo, bt_graficos;
    private Toolbar toolbar_operaciones;
    private FloatingActionButton fab_;
    private FloatingActionButton fab_enlaces;
    private FloatingActionButton fab_muestreos;

    private TextView tv_usuario, tv_rolUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        toolbar_operaciones = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_operaciones);

        // Boton flotante, lado derecha abajo (simbolo de una llave)
        fab_ = (FloatingActionButton) findViewById(R.id.fab);
        fab_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_cruds = new Intent(MenuPrincipalActivity.this, CRUDS.class);
                startActivity(intent_cruds);
            }
        });

        fab_enlaces = (FloatingActionButton) findViewById(R.id.fab_enlaces_ID);
        fab_enlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_cruds = new Intent(MenuPrincipalActivity.this, Enlaces_Datos.class);
                startActivity(intent_cruds);
            }
        });

        fab_muestreos = (FloatingActionButton) findViewById(R.id.fab_muestreos_ID);
        fab_muestreos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_cruds = new Intent(MenuPrincipalActivity.this, MuestreoActivity.class);
                startActivity(intent_cruds);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar_operaciones, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Boton Registrar Observación
        bt_Muestreo = (Button) findViewById(R.id.bt_muestreo_ID);
        bt_Muestreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_menuPrincipal = new Intent(MenuPrincipalActivity.this, ObservacionActivity.class);
                startActivity(intent_menuPrincipal);
            }
        });

        // Boton de los graficos
        bt_graficos = (Button) findViewById(R.id.bt_graficos_ID);
        bt_graficos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ = new Intent(MenuPrincipalActivity.this, GraficosActivity.class);
                startActivity(intent_);
            }
        });

        View hView = navigationView.getHeaderView(0);
        tv_usuario = (TextView) hView.findViewById(R.id.tv_usuario_ID);
        tv_usuario.setText(ClaseGlobal.usuarioActual.nombre);

        tv_rolUsuario = (TextView) hView.findViewById(R.id.tv_rolUsuario_ID);
        tv_rolUsuario.setText(ClaseGlobal.usuarioActual.correo);

        MostrarComponentes_usuario(savedInstanceState);

    }

    /**
     * Indentificar el tipo de usuario al momento de crear la pantalla
     * Recibe un parametro "ROL" por parte de la pantalla del login
     * @param savedInstanceState
     */
    private void MostrarComponentes_usuario(Bundle savedInstanceState)
    {
        String rol;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                rol = null;
            }
            else {
                rol = extras.getString("ROL");

                if (rol.equals("ADMINISTRADOR"))
                {
                    fab_muestreos.setEnabled(true);
                    fab_enlaces.setEnabled(true);
                    fab_.setEnabled(true);
                    bt_graficos.setEnabled(true);

                    fab_muestreos.setVisibility(View.VISIBLE);
                    fab_enlaces.setVisibility(View.VISIBLE);
                    fab_.setVisibility(View.VISIBLE);
                    bt_graficos.setVisibility(View.VISIBLE);

                }
                else // es ANALISTA
                {
                    fab_muestreos.setEnabled(false);
                    fab_enlaces.setEnabled(false);
                    fab_.setEnabled(false);
                    bt_graficos.setEnabled(false);

                    fab_muestreos.setVisibility(View.INVISIBLE);
                    fab_enlaces.setVisibility(View.INVISIBLE);
                    fab_.setVisibility(View.INVISIBLE);
                    bt_graficos.setVisibility(View.INVISIBLE);

                }
            }
        }
        else {
            rol = (String) savedInstanceState.getSerializable("ROL");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_info_contacto) {
            MessageDialog(ClaseGlobal.usuarioActual.toString(), "", "Cerrar");
        }
        else if (id == R.id.nav_cerrar_sesion)
        {
            Intent intent_iniciarsesion = new Intent(MenuPrincipalActivity.this, IniciarSesionActivity.class);
            ClaseGlobal.usuarioActual = null;
            startActivity(intent_iniciarsesion);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Impedir que el usuario retroceda mediante el boton de atras del dispositivo
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

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
