package reque.proyecto2.jose_.proindust_app.fragmentsMuestreo;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import reque.proyecto2.jose_.proindust_app.ClaseGlobal;
import reque.proyecto2.jose_.proindust_app.R;
import reque.proyecto2.jose_.proindust_app.modelo.Muestreo;
import reque.proyecto2.jose_.proindust_app.modelo.Proyecto;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMuestreo_Crear extends Fragment {

    private View view;

    private boolean isBackFromB;
    private Spinner sp_proyecto;
    private String proyectoSeleccionado;
    private EditText et_fecha_inicio; // año-mes-dia
    private EditText et_lapso_inicial, et_lapso_final, et_tiempo_recorrido, et_descripcion; // hora:minutos:segundos
    private Button bt_registrar;

    private ArrayAdapter<String> adapterSpinner_proyecto;
    private ProgressDialog progressDialog;
    private String msgProyecto = "Seleccione un proyecto...";

    private List<String> listaProyectos;
    private List<Proyecto> listaDatosProyectos;

    private List<Muestreo> listaDatosMuestreos;

    private DatePickerDialog datePickerDialog;


    public FragmentMuestreo_Crear() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_muestreo__crear, container, false);

        // Mensaje de carga
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargado información...");
        progressDialog.setCancelable(false);

        // GetMuestreos();

        listaDatosProyectos = new ArrayList<Proyecto>();
        listaDatosMuestreos = new ArrayList<Muestreo>();

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {   }
        });

        et_fecha_inicio = (EditText) view.findViewById(R.id.et_fecha_inicio_ID);
        et_fecha_inicio.setInputType(InputType.TYPE_NULL); // no mostrar el teclado
        et_fecha_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int calendarYear = calendar.get(Calendar.YEAR);
                int calendarMonth = calendar.get(Calendar.MONTH);
                int calendarDay = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strMonth = String.valueOf(monthOfYear + 1);
                        String strDay = String.valueOf(dayOfMonth);
                        if ((monthOfYear + 1) < 10) strMonth = "0" + strMonth;
                        if (dayOfMonth < 10) strDay = "0" + strDay;
                        et_fecha_inicio.setText(year + "-" + strMonth + "-" + strDay);
                    }
                }, calendarYear, calendarMonth, calendarDay);
                datePickerDialog.show();
            }
        });

        et_lapso_inicial = (EditText) view.findViewById(R.id.et_lapso_inicial_ID);

        et_lapso_final = (EditText) view.findViewById(R.id.et_lapso_final_ID);

        et_tiempo_recorrido = (EditText) view.findViewById(R.id.et_tiempo_recorrido_ID);

        et_descripcion = (EditText) view.findViewById(R.id.et_descripcion_ID);

        bt_registrar = (Button) view.findViewById(R.id.bt_registrar_ID);
        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_CrearMuestreo();
            }
        });

        return view;
    }

    private void ActualizarSpinner(List<String> lista)
    {
        adapterSpinner_proyecto = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, lista);
        adapterSpinner_proyecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_proyecto.setAdapter(adapterSpinner_proyecto);
    }

    private void Prueba()
    {
        String e = ObtenerHoraMuestreo(Integer.parseInt(et_tiempo_recorrido.getText().toString()));
        Log.d("PUTA", e);
    }

    private void Boton_CrearMuestreo()
    {
        String fechaInicio = et_fecha_inicio.getText().toString();
        String lapsoInicial = et_lapso_inicial.getText().toString();
        String lapsoFinal = et_lapso_final.getText().toString();
        String tiempoExtra = et_tiempo_recorrido.getText().toString();
        String descripcion = et_descripcion.getText().toString();

        if (!proyectoSeleccionado.equals(msgProyecto))
        {
            if (!fechaInicio.equals("") && !lapsoInicial.equals("") && !lapsoFinal.equals("") &&
                    !tiempoExtra.equals(""))
            {
                int lapsoInicial_int = Integer.parseInt(lapsoInicial);
                int lapsoFinal_int = Integer.parseInt(lapsoFinal);

                if (lapsoFinal_int > lapsoInicial_int)
                {
                    if (ValidarFecha(fechaInicio))
                    {
                        int tiempoExtra_int = Integer.parseInt(tiempoExtra);

                        CrearMuestreo(ClaseGlobal.INSERT_MUESTREO +
                                "?idProyecto=" + GetIdProyecto(proyectoSeleccionado) +
                                "&fechaInicio=" + fechaInicio +
                                "&lapsoInicial=" + lapsoInicial +
                                "&lapsoFinal=" + lapsoFinal +
                                "&horaObservacion=" +
                                    ObtenerHoraMuestreo(
                                            GetTiempoAleatorio(lapsoInicial_int, lapsoFinal_int)) +
                                "&estado=" + "EN CURSO" +
                                "&descripcion=" + et_descripcion.getText().toString() +
                                "&cantObservRegistradas=" + "0" +
                                "&cantObservRequeridas=" + ClaseGlobal.cantObservRequeridas_default
                        );
                    }
                    else
                    {
                        MessageDialog("La fecha de inicio debe ser superior a la del día de hoy!", "Error", "Aceptar");
                    }
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

    private void CrearMuestreo(String URL)
    {
        progressDialog.setMessage("Creando muestreo...");
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
                                "Se ha creado el muestreo!", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        MessageDialog("Error al crear el muestreo!", "Error", "Aceptar");
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
     * Método que valida si la fecha ingresada es superior a la actual.
     * @param date
     * @return
     */
    private boolean ValidarFecha(String date)
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date progDate = simpleDateFormat.parse(date);

            /* Obtiene la fecha actual */
            Calendar calendar = Calendar.getInstance();
            int calendarYear = calendar.get(Calendar.YEAR);
            int calendarMonth = calendar.get(Calendar.MONTH);
            int calendarDay = calendar.get(Calendar.DAY_OF_MONTH) - 1; // -1 para contar el dia actual
            String strMonth = String.valueOf(calendarMonth + 1);
            String strDay = String.valueOf(calendarDay);

            if ((calendarDay + 1) < 10) { strMonth = "0" + strMonth; }
            if (calendarDay < 10) { strDay = "0" + strDay; }

            Date nowDate = simpleDateFormat.parse(calendarYear + "-" + strMonth + "-" + strDay);

            return progDate.after(nowDate);
        }
        catch (ParseException e) { e.printStackTrace(); return false; }
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

    private List<String> GetProyectos()
    {
        progressDialog.setMessage("Solicitando información...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_PROYECTOS_ALL;
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
                        String estado = jsonArray.getJSONObject(i).get("estado").toString();
                        String descripcion = jsonArray.getJSONObject(i).get("descripcion").toString();
                        String cantObservRegistradas = jsonArray.getJSONObject(i).get("cantObservRegistradas").toString();
                        String cantObservRequeridas = jsonArray.getJSONObject(i).get("cantObservRequeridas").toString();

                        listaDatosMuestreos.add(new
                                Muestreo(idMuestreo, idProyecto, fechaInicio, lapsoInicial, lapsoFinal, horaObservacion,
                                estado, descripcion, cantObservRegistradas, cantObservRequeridas));
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
