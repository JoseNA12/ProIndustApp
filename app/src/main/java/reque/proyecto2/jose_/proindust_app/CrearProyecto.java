package reque.proyecto2.jose_.proindust_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class CrearProyecto extends AppCompatActivity {

    SeekBar barraNivelConfianza;
    TextView textoNivelConfianza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proyecto);

        barraNivelConfianza = (SeekBar) findViewById(R.id.sb_nivelConfianza_ID);
        textoNivelConfianza = (TextView) findViewById(R.id.tv_porcentaje_ID);

        barraNivelConfianza.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                String valor = String.valueOf(progress) + "%";
                textoNivelConfianza.setText(valor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }
}
