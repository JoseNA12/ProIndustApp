package reque.proyecto2.jose_.proindust_app.fragmentsMuestreo;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import reque.proyecto2.jose_.proindust_app.ClaseGlobal;
import reque.proyecto2.jose_.proindust_app.R;
import reque.proyecto2.jose_.proindust_app.modelo.Muestreo;
import reque.proyecto2.jose_.proindust_app.modelo.Proyecto;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMuestreo_Modificar extends Fragment {

    private View view;

    private Spinner sp_proyecto, sp_estado;
    private String proyectoSeleccionado, estadoSeleccionado;
    private int posicionEstadoSeleccionado;
    private EditText et_lapso_inicial, et_lapso_final, et_tiempo_recorrido, et_descripcion; // hora:minutos:segundos
    private Button bt_modificar;

    private ArrayAdapter<String> adapterSpinner_proyecto, adapterSpinner_estado;
    private ProgressDialog progressDialog;
    private String msgProyecto = "Seleccione un proyecto...";

    private List<String> listaProyectos;
    private List<Proyecto> listaDatosProyectos;

    private List<Muestreo> listaDatosMuestreos;

    private DatePickerDialog datePickerDialog;

    private Muestreo muestreoSeleccionado = null;


    public FragmentMuestreo_Modificar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_muestreo__modificar, container, false);

        // Mensaje de carga
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargado información...");
        progressDialog.setCancelable(false);

        listaDatosProyectos = new ArrayList<Proyecto>();
        listaDatosMuestreos = new ArrayList<Muestreo>();

        GetMuestreos();
        listaProyectos = GetProyectos();

        sp_proyecto = (Spinner) view.findViewById(R.id.sp_proyecto_ID);
        adapterSpinner_proyecto = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listaProyectos);
        adapterSpinner_proyecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_proyecto.setAdapter(adapterSpinner_proyecto);

        // ActualizarSpinner(listaProyectos);

        sp_proyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                proyectoSeleccionado = sp_proyecto.getSelectedItem().toString();

                String idProyectoSeleccionado = GetIdProyecto(proyectoSeleccionado);

                if (!idProyectoSeleccionado.equals("error"))
                {
                    muestreoSeleccionado = GetMuestreo_by_idProyecto(idProyectoSeleccionado);
                    SetValoresComponentes(idProyectoSeleccionado);
                }
                else
                {
                    et_lapso_inicial.setText("");
                    et_lapso_final.setText("");
                    et_tiempo_recorrido.setText("");
                    et_descripcion.setText("");
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {   }
        });

        et_lapso_inicial = (EditText) view.findViewById(R.id.et_lapso_inicial_ID);

        et_lapso_final = (EditText) view.findViewById(R.id.et_lapso_final_ID);

        et_tiempo_recorrido = (EditText) view.findViewById(R.id.et_tiempo_recorrido_ID);

        et_descripcion = (EditText) view.findViewById(R.id.et_descripcion_ID);

        sp_estado = (Spinner) view.findViewById(R.id.sp_estado_ID);
        adapterSpinner_estado = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ClaseGlobal.estadosMuestreo);
        adapterSpinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapterSpinner_estado);

        // ActualizarSpinner(listaProyectos);

        sp_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                estadoSeleccionado = sp_estado.getSelectedItem().toString();
                posicionEstadoSeleccionado = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {   }
        });

        bt_modificar = (Button) view.findViewById(R.id.bt_modificar_ID);
        bt_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_ModificarMuestreo();
            }
        });

        return view;
    }

    private void SetValoresComponentes(String pIdProyectoSeleccionado)
    {
        et_lapso_inicial.setText(muestreoSeleccionado.lapsoInicial);
        et_lapso_final.setText(muestreoSeleccionado.lapsoFinal);
        et_tiempo_recorrido.setText(muestreoSeleccionado.tiempoRecorrido);
        et_descripcion.setText(muestreoSeleccionado.descripcion);

        String estado = muestreoSeleccionado.estado;
        if (estado.equals("EN CURSO")) { sp_estado.setSelection(0); }
        else { sp_estado.setSelection(1); } // PAUSADO

        // sp_estado.getSelectedItemPosition();
    }

    private void Boton_ModificarMuestreo()
    {
        String lapsoInicial = et_lapso_inicial.getText().toString();
        String lapsoFinal = et_lapso_final.getText().toString();
        String tiempoRecorrido = et_tiempo_recorrido.getText().toString();
        String descripcion = et_descripcion.getText().toString();

        if (!proyectoSeleccionado.equals(msgProyecto))
        {
            if (!lapsoInicial.equals("") && !lapsoFinal.equals("") &&
                    !tiempoRecorrido.equals(""))
            {
                int lapsoInicial_int = Integer.parseInt(lapsoInicial);
                int lapsoFinal_int = Integer.parseInt(lapsoFinal);

                if (lapsoFinal_int > lapsoInicial_int)
                {
                    int tiempoRecorrido_int = Integer.parseInt(tiempoRecorrido);
                    String horaInicio = ObtenerHoraMuestreo(
                            GetTiempoAleatorio(lapsoInicial_int, lapsoFinal_int) + tiempoRecorrido_int);

                    ModificarMuestreo(ClaseGlobal.UPDATE_MUESTREO +
                            "?idMuestreo=" + muestreoSeleccionado.idMuestreo +
                            "&idProyecto=" + GetIdProyecto(proyectoSeleccionado) +
                            "&lapsoInicial=" + lapsoInicial +
                            "&lapsoFinal=" + lapsoFinal +
                            "&horaObservacion=" + horaInicio +
                            "&tiempoRecorrido=" + tiempoRecorrido +
                            "&estado=" + estadoSeleccionado +
                            "&descripcion=" + descripcion
                    , horaInicio);

                }
                else
                {
                    MessageDialog("El lapso inicial debe ser menor al rango final!", "Error", "Aceptar");
                }
            }
            else
            {
                MessageDialog("Por favor, ingrese todos los datos correspondientes!", "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, seleccione un proyecto!", "Error", "Aceptar");
        }

    }

    private void ModificarMuestreo(String URL, final String pHoraInicio)
    {
        progressDialog.setMessage("Actualizando muestreo...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        RecargarFragmento();
                        // MessageDialog("Se ha creado el enlace!", "Éxito", "Aceptar");
                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Se ha actualizado el muestreo!" +
                                "\nHora de inicio: " + pHoraInicio, Snackbar.LENGTH_LONG).show();

                    }
                    else
                    {
                        MessageDialog("Error al actualizar el muestreo!", "Error", "Aceptar");
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
                MessageDialog("Error al procesar la solicitud.\nIntente mas tarde!.",
                        "Error de conexión", "Aceptar");
            }
        });queue.add(stringRequest);
    }

    /**
     * Dado un rango (valor minimo y maximo) obtener un valor aleatorio dentro de ese rango
     * @param pLapsoInicial
     * @param pLapsoFinal
     * @return
     */
    private Integer GetTiempoAleatorio(int pLapsoInicial, int pLapsoFinal)
    {
        return ThreadLocalRandom.current().nextInt(pLapsoInicial, pLapsoFinal + 1);
    }

    /**
     * Obtener la hora del primer muestreo preliminar segun el valor generado aleatoriamente
     * acorde al rango de minutos ingresado
     * @param pValor
     * @return
     */
    private String ObtenerHoraMuestreo(int pValor)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, pValor);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String nuevaHora = Integer.toString(hour) + ":" + Integer.toString(minute) + ":" + second;

        return nuevaHora;
    }

    private void GetMuestreos()
    {
        progressDialog.setMessage("Solicitando información...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_MUESTREOS_ALL;

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String idMuestreo = jsonArray.getJSONObject(i).get("idMuestreo").toString();
                        String idProyecto = jsonArray.getJSONObject(i).get("idProyecto").toString();
                        String fechaInicio = jsonArray.getJSONObject(i).get("fechaInicio").toString();
                        String lapsoInicial = jsonArray.getJSONObject(i).get("lapsoInicial").toString();
                        String lapsoFinal = jsonArray.getJSONObject(i).get("lapsoFinal").toString();
                        String horaObservacion = jsonArray.getJSONObject(i).get("horaObservacion").toString();
                        String tiempoRecorrido = jsonArray.getJSONObject(i).get("tiempoRecorrido").toString();
                        String estado = jsonArray.getJSONObject(i).get("estado").toString();
                        String descripcion = jsonArray.getJSONObject(i).get("descripcion").toString();
                        String cantObservRegistradas = jsonArray.getJSONObject(i).get("cantObservRegistradas").toString();
                        String cantObservRequeridas = jsonArray.getJSONObject(i).get("cantObservRequeridas").toString();

                        listaDatosMuestreos.add(new
                                Muestreo(idMuestreo, idProyecto, fechaInicio, lapsoInicial, lapsoFinal, horaObservacion,
                                tiempoRecorrido, estado, descripcion, cantObservRegistradas, cantObservRequeridas));

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
                MessageDialog("Error al solicitar proyectos.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);
    }

    private List<String> GetProyectos()
    {
        progressDialog.setMessage("Solicitando información...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_PROYECTOS_CON_MUESTREOS_ACTIVOS_PAUSADOS;
        final List<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(msgProyecto);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String id = jsonArray.getJSONObject(i).get("idProyecto").toString();
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        arraySpinner.add(nombre);

                        listaDatosProyectos.add(new Proyecto(id, nombre));
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
                MessageDialog("Error al solicitar proyectos.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });
        queue.add(stringRequest);

        return arraySpinner;
    }

    private String GetIdProyecto(String pNombre)
    {
        String id = "error";

        for (int i = 0; i < listaDatosProyectos.size(); i++)
        {
            if (pNombre.equals(listaDatosProyectos.get(i).nombre))
            {
                id = listaDatosProyectos.get(i).id;
                break;
            }
        }
        return id;
    }

    /**
     * Dado un idProyecto seleccionar el muestreo que se encuentra activo o en pausa para modificar
     * @param pIdProyecto
     * @return
     */
    private Muestreo GetMuestreo_by_idProyecto(String pIdProyecto)
    {
        Muestreo miMuestreo = null;

        for (int i = 0; i < listaDatosMuestreos.size(); i++)
        {
            if (listaDatosMuestreos.get(i).idProyecto.equals(pIdProyecto)
                    && !listaDatosMuestreos.get(i).estado.equals("CONCLUIDO"))
            {
                return listaDatosMuestreos.get(i);
            }
        }

        return miMuestreo;
    }

    /**
     * Utilizado para recargar los datos, al momento de hacer un cambio en la bd
     */
    private void RecargarFragmento()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    /**
     * Despliega un mensaje emergente en pantalla
     * @param message
     * @param pTitulo
     * @param pLabelBoton
     */
    private void MessageDialog(String message, String pTitulo, String pLabelBoton){ // mostrar mensaje emergente
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage(message).setTitle(pTitulo).setPositiveButton(pLabelBoton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
