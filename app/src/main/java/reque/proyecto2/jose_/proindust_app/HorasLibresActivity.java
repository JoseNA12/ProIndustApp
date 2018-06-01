package reque.proyecto2.jose_.proindust_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.app.TimePickerDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import reque.proyecto2.jose_.proindust_app.modelo.HorasLibres_C;
import reque.proyecto2.jose_.proindust_app.modelo.Proyecto;

public class HorasLibresActivity extends AppCompatActivity {

    private EditText et_horaInicio;
    private EditText et_horaFinal;

    private Button bt_registrarHora;

    private Spinner sp_proyecto;
    private ArrayAdapter<String> adapterSpinner_proyecto;
    private List<String> listaProyectos;
    private String msgProyecto = "Seleccione un proyecto...";
    private String proyectoSeleccionado;

    private TimePickerDialog timePickerDialog;
    private ProgressDialog progressDialog;

    private ListView lv_listaHoras;
    private ListAdapter theAdapter;

    private List<Proyecto> listaDatosProyectos;
    private List<HorasLibres_C> listaDatosHorasLibres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horas_libres);

        // Mensaje de carga
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargado información...");
        progressDialog.setCancelable(false);

        listaDatosProyectos = new ArrayList<Proyecto>();
        listaDatosHorasLibres = new ArrayList<HorasLibres_C>();

        listaProyectos = GetProyectos();
        GetHorasLibres();

        lv_listaHoras = (ListView) findViewById(R.id.dy_listaHoras_ID);
        lv_listaHoras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final int pos = position;

                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_enlaces, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(CRUDS.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        if (item.getTitle().equals("Eliminar"))
                        {
                            String horaSeleccionada = parent.getItemAtPosition(position).toString();

                            List<String> horas = GetHoras_de_Lista(horaSeleccionada);
                            EliminarHoras_de_Proyecto(proyectoSeleccionado, horas.get(0).toString(), horas.get(1).toString());
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        bt_registrarHora = (Button) findViewById(R.id.bt_registrarHora_ID);
        bt_registrarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boton_RegistrarHoraLibre();
            }
        });

        sp_proyecto = (Spinner) findViewById(R.id.sp_proyecto_ID);
        adapterSpinner_proyecto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaProyectos);
        adapterSpinner_proyecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_proyecto.setAdapter(adapterSpinner_proyecto);
        sp_proyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                proyectoSeleccionado = sp_proyecto.getSelectedItem().toString();

                ActualizarListView(GetHorasLibres_de_Proyecto(proyectoSeleccionado));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        et_horaInicio = (EditText) findViewById(R.id.et_horaInicio_ID);
        et_horaInicio.setInputType(InputType.TYPE_NULL); // no mostrar el teclado
        et_horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(HorasLibresActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute)
                    {
                        String strHour = String.valueOf(hour);
                        String strMin = String.valueOf(minute);
                        if (hour < 10) { strHour = "0" + strHour; }
                        if (minute < 10) { strMin = "0" + strMin; }
                        et_horaInicio.setText(strHour + ":" + strMin + ":00");
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        et_horaFinal = (EditText) findViewById(R.id.et_horaFinal_ID);
        et_horaFinal.setInputType(InputType.TYPE_NULL);
        et_horaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(HorasLibresActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String strHour = String.valueOf(hour);
                        String strMin = String.valueOf(minute);
                        if (hour < 10) strHour = "0" + strHour;
                        if (minute < 10) strMin = "0" + strMin;
                        et_horaFinal.setText(strHour + ":" + strMin + ":00");
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    private void Boton_RegistrarHoraLibre()
    {
        if (!proyectoSeleccionado.equals(msgProyecto))
        {
            if (!et_horaInicio.getText().toString().equals(""))
            {
                if (!et_horaFinal.getText().toString().equals(""))
                {
                    RegistrarHoraLibre(ClaseGlobal.INSERT_HORALIBRE +
                            "?idProyecto=" + GetIdProyecto(proyectoSeleccionado) +
                            "&horaInicio=" + et_horaInicio.getText().toString() +
                            "&horaFinal=" + et_horaFinal.getText().toString());
                }
                else
                {
                    MessageDialog("Por favor, ingrese la hora final!",
                            "Error", "Aceptar");
                }
            }
            else
            {
                MessageDialog("Por favor, ingrese la hora de inicio!",
                        "Error", "Aceptar");
            }
        }
        else
        {
            MessageDialog("Por favor, seleccione un proyecto!",
                    "Error", "Aceptar");
        }
    }

    private void RegistrarHoraLibre(String URL)
    {
        progressDialog.setMessage("Insertando nueva información...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        // MessageDialog("Se ha creado el registro!", "Éxito", "Aceptar");

                        Snackbar.make(HorasLibresActivity.this.findViewById(android.R.id.content),
                                "Se ha creado el lapso de tiempo!", Snackbar.LENGTH_SHORT).show();

                        RefrecarActivity();
                    }
                    else
                    {
                        MessageDialog("Error al crear el lapso de tiempo!", "Error", "Aceptar");
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

    /***
     * Permite actualizar los datos que se despliegan del componente ListaView
     * @param lista: Lista con los elementos a refrescar
     */
    private void ActualizarListView(List<String> lista)
    {
        theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, lista);
        lv_listaHoras.setAdapter(theAdapter);
    }

    private List<String> GetProyectos()
    {
        progressDialog.setMessage("Solicitando información...");
        progressDialog.show();

        String URL = ClaseGlobal.SELECT_PROYECTOS_ALL;
        final List<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(msgProyecto);

        RequestQueue queue = Volley.newRequestQueue(this);
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
        });queue.add(stringRequest);

        return arraySpinner;
    }

    private void EliminarHoras_de_Proyecto(final String pNombreProyecto, final String pHoraInicio, final String pHoraFinal)
    {
        final AlertDialog.Builder builderEliminar = new AlertDialog.Builder(this);
        builderEliminar.setTitle("Atención!");
        builderEliminar.setMessage("¿Desea eliminar las horas " + pHoraInicio + " / " + pHoraFinal +
                " del proyecto " + pNombreProyecto + "?");

        builderEliminar.setPositiveButton("PROCEDER", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                EliminarEnlace(ClaseGlobal.DELECT_HORALIBRE +
                        "?idProyecto=" + GetIdProyecto(pNombreProyecto) +
                        "&horaInicio=" + pHoraInicio +
                        "&horaFinal=" + pHoraFinal);

                dialog.dismiss();
            }
        });

        builderEliminar.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builderEliminar.create();
        alert.show();
    }

    private void EliminarEnlace(String URL)
    {
        progressDialog.setMessage("Eliminando enlace...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        //MessageDialog("Se ha eliminado el enlace!", "Éxito", "Aceptar");
                        progressDialog.dismiss();

                        Snackbar.make(HorasLibresActivity.this.findViewById(android.R.id.content),
                                "Se ha eliminado el lapso de tiempo!", Snackbar.LENGTH_SHORT).show();

                        RefrecarActivity();
                    }
                    else
                    {
                        // MessageDialog("Error al eliminar el enlace!", "Error", "Aceptar");
                        progressDialog.dismiss();

                        Snackbar.make(HorasLibresActivity.this.findViewById(android.R.id.content),
                                "Error al eliminar el lapso de tiempo!", Snackbar.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

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

    private void GetHorasLibres()
    {
        String URL = ClaseGlobal.SELECT_HORASLIBRES_ALL;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String idHorasLibres = jsonArray.getJSONObject(i).get("idHorasLibres").toString();
                        String idProyecto = jsonArray.getJSONObject(i).get("idProyecto").toString();
                        String horaInicio = jsonArray.getJSONObject(i).get("horaInicio").toString();
                        String horaFinal = jsonArray.getJSONObject(i).get("horaFinal").toString();

                        listaDatosHorasLibres.add(new
                                HorasLibres_C(idHorasLibres, idProyecto, horaInicio, horaFinal));
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
                MessageDialog("Error al solicitar datos.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);
    }


    private List<String> GetHorasLibres_de_Proyecto(String pNombreProyecto)
    {
        return GetListaHorasLibres(GetIdProyecto(pNombreProyecto));
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

    private List<String> GetListaHorasLibres(String pIdProyecto)
    {
        List<String> horas = new ArrayList<String>();

        for (int i = 0; i < listaDatosHorasLibres.size(); i++)
        {
            if (pIdProyecto.equals(listaDatosHorasLibres.get(i).idProyecto))
            {
                horas.add(listaDatosHorasLibres.get(i).horaInicio + " / " +
                        listaDatosHorasLibres.get(i).horaFinal);
            }
        }
        return horas;
    }

    private List<String> GetHoras_de_Lista(String HorasJuntas)
    {
        String sinEspacios = HorasJuntas.replace(" ", "");
        List<String> horas = new ArrayList<String>();
        String horaInicio = ""; String horaFinal = "";

        int i = 0;
        for (char c : sinEspacios.toCharArray())
        {
            if (i < 8) { horaInicio += c; }
            else { if (i > 8) { horaFinal += c; } }
            i++;
        }
        horas.add(horaInicio); horas.add(horaFinal);

        return horas;
    }

    /**
     * Refrecar el activity cada vez que ser registre una nueva hora o se
     * elimine alguna
     */
    private void RefrecarActivity()
    {
        listaDatosHorasLibres = new ArrayList<HorasLibres_C>();
        GetHorasLibres();
        ActualizarListView(new ArrayList<String>());

        ArrayAdapter myAdap = (ArrayAdapter) sp_proyecto.getAdapter();
        int spinnerPosition = myAdap.getPosition(msgProyecto);

        sp_proyecto.setSelection(spinnerPosition);
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
