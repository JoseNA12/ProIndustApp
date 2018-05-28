package reque.proyecto2.jose_.proindust_app.CRUDS;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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

import reque.proyecto2.jose_.proindust_app.ClaseGlobal;
import reque.proyecto2.jose_.proindust_app.R;

public class CrearProyecto extends AppCompatActivity {

    // et_nombre_ID, tv_descripcion_ID, sb_nivelConfianza_ID, et_rangoInicio_ID,
    // et_rangoFinal_ID, sch_rangoAleatorio_ID, et_cantMuestreosP, bt_crear_ID

    private EditText et_nombre, et_descripcion, et_rangoInicio, et_rangoFinal, et_cantMuestreosP, et_tiempoRecorrido;
    private Switch sch_rangoAleatorio;
    private Button bt_crear;
    private SeekBar barraNivelConfianza;
    private TextView textoNivelConfianza;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proyecto);

        et_nombre = (EditText) findViewById(R.id.et_nombre_ID);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion_ID);
        et_rangoInicio = (EditText) findViewById(R.id.et_rangoInicio_ID);
        et_rangoFinal = (EditText) findViewById(R.id.et_rangoFinal_ID);
        et_cantMuestreosP = (EditText) findViewById(R.id.et_cantMuestreosP_ID);
        et_tiempoRecorrido = (EditText) findViewById(R.id.et_tiempoRecorrido_ID);

        sch_rangoAleatorio = (Switch) findViewById(R.id.sch_rangoAleatorio_ID);
        sch_rangoAleatorio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                et_rangoInicio.setEnabled(!isChecked);
                et_rangoFinal.setEnabled(!isChecked);
            }
        });

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

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);
    }

    private void Boton_CrearProyecto()
    {
        if(!et_nombre.getText().toString().equals("") &&
                !et_rangoInicio.getText().toString().equals("") && !et_rangoFinal.getText().toString().equals("") &&
                !et_cantMuestreosP.getText().toString().equals("") && !et_tiempoRecorrido.getText().toString().equals("")) {

            CrearProyecto(ClaseGlobal.INSERT_PROYECTO +
                    "?nombre=" + et_nombre.getText().toString() +
                    "&descripcion=" + et_descripcion.getText().toString() +
                    "&nivelConfianza=" + Integer.toString(barraNivelConfianza.getProgress()) +
                    "&rangoInicial=" + et_rangoInicio.getText().toString() +
                    "&rangoFinal=" + et_rangoFinal.getText().toString() +
                    "&cantMuestreosP=" + et_cantMuestreosP.getText().toString() +
                    "&tiempoRecorrido=" + et_tiempoRecorrido.getText().toString()
            );

        }
        else
        {
            MessageDialog("Por favor, complete todos los campos!", "Error", "Aceptar");
        }
    }

    private void CrearProyecto(String URL)
    {
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("false"))
                    {
                        MessageDialog("Error al crear el proyecto!", "Error", "Aceptar");
                    }
                    else
                    {
                        MessageDialog("Se ha creado el proyecto!", "Éxito", "Aceptar");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al procesar la solicitud.\n",
                        "Error de conexión", "Aceptar");
            }
        });queue.add(stringRequest);
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
