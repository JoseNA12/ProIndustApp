package reque.proyecto2.jose_.proindust_app;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CRUDS extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruds);

        mTextMessage = (TextView) findViewById(R.id.textView3);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String[] ninjaList = {"Jose", "Navarro", "Hola", "Holiwis", "Jelou mai frey"};
        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, ninjaList);
        lv = (ListView) findViewById(R.id.dy_lista_ID);
        lv.setAdapter(theAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                PopupMenu popup = new PopupMenu(CRUDS.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(CRUDS.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        Intent intent_iniciarsesion = new Intent(CRUDS.this, CrearProyecto.class);
                        startActivity(intent_iniciarsesion);

                        return true;
                    }
                });

                popup.show();//showing popup menu

                mTextMessage.setText("Viva vegetta ostia");
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_proyectos:
                    mTextMessage.setText(R.string.title_proyectos);
                    return true;
                case R.id.navigation_operaciones:
                    mTextMessage.setText(R.string.title_operaciones);
                    return true;
                case R.id.navigation_tareas:
                    mTextMessage.setText(R.string.title_tareas);
                    return true;
                case R.id.navigation_colaboradores:
                    mTextMessage.setText(R.string.title_colaboradores);
                    return true;
                case R.id.navigation_usuarios:
                    mTextMessage.setText(R.string.title_usuarios);
                    return true;
            }
            return false;
        }
    };



}
