package reque.proyecto2.jose_.proindust_app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        lv = (ListView) findViewById(R.id.dy_lista_ID);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                //CODIGO AQUI

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
