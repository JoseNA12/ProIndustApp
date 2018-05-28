package reque.proyecto2.jose_.proindust_app.fragmentsEnlaces;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
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

import java.util.ArrayList;
import java.util.List;

import reque.proyecto2.jose_.proindust_app.ClaseGlobal;
import reque.proyecto2.jose_.proindust_app.enlacesDatos.CrearEnlace_Operaciones;
import reque.proyecto2.jose_.proindust_app.R;
import reque.proyecto2.jose_.proindust_app.enlacesDatos.CrearEnlace_Usuarios;
import reque.proyecto2.jose_.proindust_app.modelo.Proyecto;
import reque.proyecto2.jose_.proindust_app.modelo.ProyectoOperacion;
import reque.proyecto2.jose_.proindust_app.modelo.Operacion;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEnlace_Operaciones extends Fragment {

    private View view;

    private ListView lv_lista;
    private ListAdapter theAdapter;
    private FloatingActionButton fab_crear_enlace;
    private Spinner sp_proyecto_enlace;
    private ArrayAdapter<String> adapterSpinner_proyecto;

    private ProgressDialog progressDialog;

    private String msgProyecto = "Seleccione un proyecto...";

    private List<String> listaProyectos;
    private List<String> listaOperaciones;

    private List<Proyecto> listaDatosProyectos;
    private List<Operacion> listaDatosOperaciones;
    private List<ProyectoOperacion> listaProyectosOperaciones;

    public FragmentEnlace_Operaciones() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_enlace__operaciones, container, false);

        // Mensaje de carga
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargado información...");
        progressDialog.setCancelable(false);

        listaDatosProyectos = new ArrayList<Proyecto>();
        listaDatosOperaciones = new ArrayList<Operacion>();
        listaProyectosOperaciones = new ArrayList<ProyectoOperacion>();

        listaProyectos = GetProyectos();
        listaOperaciones = GetOperaciones();
        GetProyectosOperaciones();

        sp_proyecto_enlace = (Spinner) view.findViewById(R.id.sp_proyecto_enlace_ID);
        adapterSpinner_proyecto = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listaProyectos);
        adapterSpinner_proyecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_proyecto_enlace.setAdapter(adapterSpinner_proyecto);
        sp_proyecto_enlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String proyectoSeleccionado = sp_proyecto_enlace.getSelectedItem().toString();

                ActualizarListView(GetOperaciones_de_Proyecto(proyectoSeleccionado));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        lv_lista = (ListView) view.findViewById(R.id.dy_lista_enlaces_ID_operaciones);

        lv_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final int pos = position;

                PopupMenu popup = new PopupMenu(getActivity(), view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_enlaces, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(CRUDS.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        if (item.getTitle().equals("Eliminar"))
                        {
                            String nombreOperacion = parent.getItemAtPosition(position).toString();
                            String nombreProyecto = sp_proyecto_enlace.getSelectedItem().toString();

                            EliminarOperacion_de_Proyecto(nombreOperacion, nombreProyecto);
                        }

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        // Boton flotante, lado derecha abajo (simbolo de una llave)
        fab_crear_enlace = (FloatingActionButton) view.findViewById(R.id.fab_crearEnlace_ID_operaciones);
        fab_crear_enlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), CrearEnlace_Operaciones.class);
                startActivity(myIntent);
            }
        });

        // ConsultarDatosTabla(ClaseGlobal.SELECT_, "nombre");

        return view;
    }

    /***
     * Permite actualizar los datos que se despliegan del componente ListaView
     * @param lista: Lista con los elementos a refrescar
     */
    private void ActualizarListView(List<String> lista)
    {
        theAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item, lista);
        lv_lista.setAdapter(theAdapter);
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
                // progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                MessageDialog("Error al solicitar proyectos.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    private List<String> GetOperaciones()
    {
        //progressDialog.setMessage("Solicitando información...");
        //progressDialog.show();

        String URL = ClaseGlobal.SELECT_OPERACIONES_ALL;
        final List<String> arraySpinner = new ArrayList<String>();

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
                        String id = jsonArray.getJSONObject(i).get("idOperacion").toString();
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();

                        arraySpinner.add(nombre);

                        listaDatosOperaciones.add(new Operacion(id, nombre, ""));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                // progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                MessageDialog("Error al solicitar operaciones.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return arraySpinner;
    }

    private void GetProyectosOperaciones()
    {
        String URL = ClaseGlobal.SELECT_PROYECTOOPERACION_ALL;

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
                        String idProyecto = jsonArray.getJSONObject(i).get("idProyecto").toString();
                        String idOperacion = jsonArray.getJSONObject(i).get("idOperacion").toString();

                        listaProyectosOperaciones.add(new ProyectoOperacion(idProyecto, idOperacion));
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

    private List<String> GetOperaciones_de_Proyecto(String pNombreProyecto)
    {
        return GetListaNombreOperaciones(GetIdProyecto(pNombreProyecto));
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

    private List<String> GetListaNombreOperaciones(String pIdProyecto)
    {
        List<String> idProyectos = new ArrayList<String>();

        for (int i = 0; i < listaProyectosOperaciones.size(); i++)
        {
            if (pIdProyecto.equals(listaProyectosOperaciones.get(i).idProyecto))
            {
                idProyectos.add(GetNombreOperacion(listaProyectosOperaciones.get(i).idOperacion));
            }
        }
        return idProyectos;
    }

    private String GetNombreOperacion(String pIdOperacion)
    {
        String nombre = "error";
        for (int i = 0; i < listaDatosOperaciones.size(); i++)
        {
            if (pIdOperacion.equals(listaDatosOperaciones.get(i).id))
            {
                nombre = listaDatosOperaciones.get(i).nombre;
                break;
            }
        }
        return nombre;
    }

    private String GetIdOperacion(String pNombre)
    {
        String id = "error";
        for (int i = 0; i < listaDatosOperaciones.size(); i++)
        {
            if (pNombre.equals(listaDatosOperaciones.get(i).nombre))
            {
                id = listaDatosOperaciones.get(i).id;
                break;
            }
        }
        return id;
    }

    private void EliminarOperacion_de_Proyecto(final String pNombreOperacion, final String pNombreProyecto)
    {
        final AlertDialog.Builder builderEliminar = new AlertDialog.Builder(getActivity());
        builderEliminar.setTitle("Atención!");
        builderEliminar.setMessage("¿Desea eliminar la operación " + pNombreOperacion +
                " del proyecto " + pNombreProyecto + "?");

        builderEliminar.setPositiveButton("PROCEDER", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                EliminarEnlace(ClaseGlobal.DELECT_PROYECTOOPERACION_ID_ID +
                        "?idProyecto=" + GetIdProyecto(pNombreProyecto) +
                        "&idOperacion=" + GetIdOperacion(pNombreOperacion));

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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        //MessageDialog("Se ha eliminado el enlace!", "Éxito", "Aceptar");
                        progressDialog.dismiss();

                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Se ha eliminado el enlace!", Snackbar.LENGTH_SHORT).show();
                        RecargarFragmento();
                    }
                    else
                    {

                        // MessageDialog("Error al eliminar el enlace!", "Error", "Aceptar");
                        progressDialog.dismiss();

                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Error al eliminar el enlace!", Snackbar.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                // progressDialog.dismiss();

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
