package reque.proyecto2.jose_.proindust_app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class CRUDS extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruds);

        mTextMessage = (TextView) findViewById(R.id.textView3);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
