package reque.proyecto2.jose_.proindust_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CrearProyecto extends AppCompatActivity {

    // et_nombre_ID, tv_descripcion_ID, sb_nivelConfianza_ID, et_rangoInicio_ID,
    // et_rangoFinal_ID, sch_rangoAleatorio_ID, et_cantMuestreosP, bt_crear_ID

    EditText et_nombre, et_descripcion, et_rangoInicio, et_rangoFinal, et_cantMuestreosP;
    Switch sch_rangoAleatorio;
    Button bt_crear;
    SeekBar barraNivelConfianza;
    TextView textoNivelConfianza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proyecto);

        et_nombre = (EditText) findViewById(R.id.et_nombre_ID);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion_ID);
        et_rangoInicio = (EditText) findViewById(R.id.et_rangoInicio_ID);
        et_rangoFinal = (EditText) findViewById(R.id.et_rangoFinal_ID);
        et_cantMuestreosP = (EditText) findViewById(R.id.et_cantMuestreosP_ID);

        sch_rangoAleatorio = (Switch) findViewById(R.id.sch_rangoAleatorio_ID);

        bt_crear = (Button) findViewById(R.id.bt_crear_ID);

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

        bt_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_CrearProyecto();
            }
        });
    }

    private void Boton_CrearProyecto()
    {
        if(!et_nombre.getText().toString().equals("") &&
                !et_rangoInicio.getText().toString().equals("") && !et_rangoFinal.getText().toString().equals("") &&
                !et_cantMuestreosP.getText().toString().equals("")) {

            CrearProyecto(ClaseGlobal.INSERT_PROYECTO +
                    "?Nombre=" + et_nombre.getText().toString() +
                    "&Descripcion=" + et_descripcion.getText().toString() +
                    "&NivelConfianza=" + barraNivelConfianza.getProgress() +
                    "&RangoInicial=" + et_rangoInicio.getText().toString() +
                    "&RangoFinal=" + et_rangoFinal.getText().toString() +
                    "&CantMuestreosP=" + et_cantMuestreosP.getText().toString()
            );

        }
        else
        {
            errorMessageDialog("Por favor, complete todos los campos!");
        }
    }

    private void CrearProyecto(String URL){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        correctMessageDialog("Se ha creado el proyecto!");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorMessageDialog("Error, al procesar la solicitud.\nVerifique su conexión a internet!.");
            }
        });queue.add(stringRequest);
    }

    private void errorMessageDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message).setTitle("Error").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void correctMessageDialog(String message){ // mostrar mensaje emergente
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message).setTitle("Éxito").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
