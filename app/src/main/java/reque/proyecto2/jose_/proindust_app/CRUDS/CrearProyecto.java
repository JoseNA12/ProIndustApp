package reque.proyecto2.jose_.proindust_app.CRUDS;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import reque.proyecto2.jose_.proindust_app.ClaseGlobal;
import reque.proyecto2.jose_.proindust_app.R;
import reque.proyecto2.jose_.proindust_app.modelo.Proyecto;

public class CrearProyecto extends AppCompatActivity {

    // et_nombre_ID, tv_descripcion_ID, sb_nivelConfianza_ID, et_rangoInicio_ID,
    // et_rangoFinal_ID, sch_rangoAleatorio_ID, et_cantMuestreosP, bt_crear_ID

    private EditText et_nombre, et_descripcion, et_rangoInicio, et_rangoFinal, et_cantMuestreosP, et_tiempoRecorrido;
    private Switch sch_rangoAleatorio;
    private Button bt_crear;
    private SeekBar barraNivelConfianza;
    private TextView textoNivelConfianza;
    private int nivelConfianza = 90;

    private Spinner sp_estado;
    private ArrayAdapter<String> adapterSpinner_estado;
    private String estadoSeleccionado;

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
        sch_rangoAleatorio.setVisibility(View.INVISIBLE);
        sch_rangoAleatorio.setEnabled(false);

        bt_crear = (Button) findViewById(R.id.bt_crear_ID);

        textoNivelConfianza = (TextView) findViewById(R.id.tv_porcentaje_ID);
        barraNivelConfianza = (SeekBar) findViewById(R.id.sb_nivelConfianza_ID);
        final int MIN = 90; final int MAX = 100; final int STEP = 1;
        // int value = (100 * (STEP - MIN)) / (MAX - MIN);
        textoNivelConfianza.setText("90%");

        barraNivelConfianza.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                double value = Math.round((progress * (MAX - MIN)) / 100);
                nivelConfianza = (((int) value + MIN) / STEP) * STEP;

                textoNivelConfianza.setText(Integer.toString(nivelConfianza) + "%");
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

        /*bt_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_CrearProyecto();
            }
        });*/

        sp_estado = (Spinner) findViewById(R.id.sp_estado_ID);
        adapterSpinner_estado = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ClaseGlobal.estadosProyecto);
        adapterSpinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapterSpinner_estado);
        sp_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                estadoSeleccionado = sp_estado.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.setCancelable(false);

        DeterminarAccionBoton(savedInstanceState);
    }

    private void DeterminarAccionBoton(Bundle savedInstanceState)
    {
        String accion;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                accion = null;
            }
            else {
                accion = extras.getString("ACCION");

                if (accion.equals("CREAR"))
                {
                    bt_crear.setText("CREAR");
                    bt_crear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Boton_CrearProyecto();
                        }
                    });
                }
                else // MODIFICAR
                {
                    final Proyecto miProyecto = (Proyecto) getIntent().getSerializableExtra("OBJETO");
                    SetValoresComponentes(miProyecto);

                    bt_crear.setText("MODIFICAR");
                    bt_crear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Boton_ModificarProyecto(miProyecto);
                        }
                    });
                }
            }
        }
        else {
            accion = (String) savedInstanceState.getSerializable("ACCION");
        }

    }

    private void Boton_CrearProyecto()
    {
        if(!et_nombre.getText().toString().equals("") &&
                !et_cantMuestreosP.getText().toString().equals("") && !et_tiempoRecorrido.getText().toString().equals(""))
        {
            int cantMuestreosP_int = Integer.parseInt(et_cantMuestreosP.getText().toString());
            if (cantMuestreosP_int >= 30)
            {
                if (!sch_rangoAleatorio.isChecked())
                {
                    if (!et_rangoInicio.getText().toString().equals("") && !et_rangoFinal.getText().toString().equals(""))
                    {
                        int rangoInicio_int = Integer.parseInt(et_rangoInicio.getText().toString());
                        int rangoFinal_int = Integer.parseInt(et_rangoFinal.getText().toString());

                        if (rangoInicio_int < rangoFinal_int)
                        {
                            CrearProyecto(ClaseGlobal.INSERT_PROYECTO +
                                    "?nombre=" + et_nombre.getText().toString() +
                                    "&descripcion=" + et_descripcion.getText().toString() +
                                    "&nivelConfianza=" + Integer.toString(nivelConfianza) +
                                    "&rangoInicial=" + et_rangoInicio.getText().toString() +
                                    "&rangoFinal=" + et_rangoFinal.getText().toString() +
                                    "&cantMuestreosP=" + et_cantMuestreosP.getText().toString() +
                                    "&tiempoRecorrido=" + et_tiempoRecorrido.getText().toString() +
                                    "&estado=" + estadoSeleccionado
                            );
                        }
                        else
                        {
                            MessageDialog("El valor inicial de tiempo debe ser mayor al valor final de tiempo!", "Error", "Aceptar");
                        }
                    }
                    else
                    {
                        MessageDialog("Por favor, ingrese los rangos de tiempo!", "Error", "Aceptar");
                    }
                }
                else // rangos aleatorios
                {
                    CrearProyecto(ClaseGlobal.INSERT_PROYECTO +
                            "?nombre=" + et_nombre.getText().toString() +
                            "&descripcion=" + et_descripcion.getText().toString() +
                            "&nivelConfianza=" + Integer.toString(barraNivelConfianza.getProgress()) +
                            "&rangoInicial=" + "123123123" +
                            "&rangoFinal=" + "123123123" +
                            "&cantMuestreosP=" + et_cantMuestreosP.getText().toString() +
                            "&tiempoRecorrido=" + et_tiempoRecorrido.getText().toString() +
                            "&estado=" + estadoSeleccionado
                    );
                }
            }
            else
            {
                MessageDialog("La cantidad de muestreos preliminares debe ser igual o mayor a 30!", "Error", "Aceptar");
            }
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

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        // MessageDialog("Se ha creado el proyecto!", "Éxito", "Aceptar");
                        Snackbar.make(CrearProyecto.this.findViewById(android.R.id.content),
                                "Se ha creado el proyecto!", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        MessageDialog("Error al crear el proyecto!", "Error", "Aceptar");
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

    private List<Integer> ObtenerValorAleatorio()
    {
        List<Integer> valores = new ArrayList<Integer>();

        return valores;
    }

    private void Boton_ModificarProyecto(Proyecto miProyecto)
    {
        if(!et_nombre.getText().toString().equals("") &&
                !et_cantMuestreosP.getText().toString().equals("") && !et_tiempoRecorrido.getText().toString().equals(""))
        {
            int cantMuestreosP_int = Integer.parseInt(et_cantMuestreosP.getText().toString());
            if (cantMuestreosP_int >= 30)
            {
                if (!sch_rangoAleatorio.isChecked())
                {
                    if (!et_rangoInicio.getText().toString().equals("") && !et_rangoFinal.getText().toString().equals(""))
                    {
                        int rangoInicio_int = Integer.parseInt(et_rangoInicio.getText().toString());
                        int rangoFinal_int = Integer.parseInt(et_rangoFinal.getText().toString());

                        if (rangoInicio_int < rangoFinal_int)
                        {
                            ModificarProyecto(ClaseGlobal.UPDATE_PROYECTO +
                                    "?idProyecto=" + miProyecto.id +
                                    "&nombre=" + et_nombre.getText().toString() +
                                    "&descripcion=" + et_descripcion.getText().toString() +
                                    "&nivelConfianza=" + Integer.toString(barraNivelConfianza.getProgress()) +
                                    "&rangoInicial=" + et_rangoInicio.getText().toString() +
                                    "&rangoFinal=" + et_rangoFinal.getText().toString() +
                                    "&cantMuestreosP=" + et_cantMuestreosP.getText().toString() +
                                    "&tiempoRecorrido=" + et_tiempoRecorrido.getText().toString() +
                                    "&estado=" + estadoSeleccionado
                            );
                        }
                        else
                        {
                            MessageDialog("El valor inicial de tiempo debe ser mayor al valor final de tiempo!", "Error", "Aceptar");
                        }
                    }
                    else
                    {
                        MessageDialog("Por favor, ingrese los rangos de tiempo!", "Error", "Aceptar");
                    }
                }
                else // rangos aleatorios
                {
                    ModificarProyecto(ClaseGlobal.UPDATE_PROYECTO +
                            "?nombre=" + et_nombre.getText().toString() +
                            "&descripcion=" + et_descripcion.getText().toString() +
                            "&nivelConfianza=" + Integer.toString(barraNivelConfianza.getProgress()) +
                            "&rangoInicial=" + "123123123" +
                            "&rangoFinal=" + "123123123" +
                            "&cantMuestreosP=" + et_cantMuestreosP.getText().toString() +
                            "&tiempoRecorrido=" + et_tiempoRecorrido.getText().toString() +
                            "&estado=" + estadoSeleccionado
                    );
                }
            }
            else
            {
                MessageDialog("La cantidad de muestreos preliminares debe ser igual o mayor a 30!", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, complete todos los campos!", "Error", "Aceptar");
        }

    }

    private void ModificarProyecto(String URL)
    {
        progressDialog.setMessage("Modificando información...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        // MessageDialog("Se ha modificado el usuario!", "Éxito", "Aceptar");
                        Snackbar.make(CrearProyecto.this.findViewById(android.R.id.content),
                                "Se ha modificado el proyecto!", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        MessageDialog("Error al modificar el usuario!", "Error", "Aceptar");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde.","Error de conexión","Aceptar");
            }
        });queue.add(stringRequest);
    }

    /**
     * Establecer los datos del objeto selccionado en los componentes de la pantalla
     * @param miColaborador
     */
    private void SetValoresComponentes(Proyecto miProyecto)
    {
        String nombre = miProyecto.nombre;
        String descripcion = miProyecto.descripcion;
        String nivelConfianza = miProyecto.nivelConfianza;
        String rangoInicial = miProyecto.rangoInicial;
        String rangoFinal = miProyecto.rangoFinal;
        String cantMuestresP = miProyecto.cantMuestreosP;
        String tiempoRecorrido = miProyecto.tiempoRecorrido;
        String estado = miProyecto.estado;

        et_nombre.setText(nombre);
        et_descripcion.setText(descripcion);
        textoNivelConfianza.setText(nivelConfianza);
        barraNivelConfianza.setProgress(Integer.parseInt(nivelConfianza));
        et_rangoInicio.setText(rangoInicial);
        et_rangoFinal.setText(rangoFinal);
        et_cantMuestreosP.setText(cantMuestresP);
        et_tiempoRecorrido.setText(tiempoRecorrido);

        int posicion = adapterSpinner_estado.getPosition(estado);
        sp_estado.setSelection(posicion);
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
