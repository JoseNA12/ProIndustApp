package reque.proyecto2.jose_.proindust_app.fragmentsCRUDS;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import reque.proyecto2.jose_.proindust_app.ClaseGlobal;
import reque.proyecto2.jose_.proindust_app.CRUDS.CrearUsuario;
import reque.proyecto2.jose_.proindust_app.R;
import reque.proyecto2.jose_.proindust_app.modelo.Usuario;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUsuario extends Fragment implements Serializable {

    private View view;

    private ListView lv_listaComponente;
    private ListAdapter theAdapter;
    private FloatingActionButton fab_crear;

    private List<Usuario> listaDatosUsuarios;

    private ProgressDialog progressDialog;

    public FragmentUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_usuario, container, false);

        listaDatosUsuarios = new ArrayList<Usuario>();

        lv_listaComponente = (ListView) view.findViewById(R.id.dy_lista_ID);

        lv_listaComponente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final int pos = position;

                PopupMenu popup = new PopupMenu(getActivity(), view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(CRUDS.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        if (item.getTitle().equals("Información"))
                        {
                            String nombreUsuario = parent.getItemAtPosition(position).toString();
                            MessageDialog(GetInformacionUsuario(nombreUsuario),
                                    "Información", "Aceptar");
                        }
                        else
                        {
                            if (item.getTitle().equals("Modificar"))
                            {
                                Intent intent_ = new Intent(getActivity(), CrearUsuario.class); // reusar codigo y utilizar la pantalla de crear
                                intent_.putExtra("ACCION", "MODIFICAR");

                                String nombreUsuario = parent.getItemAtPosition(position).toString();
                                intent_.putExtra("OBJETO", GetObjetoUsuario(nombreUsuario));

                                startActivity(intent_);
                            }
                            else // Eliminar
                            {
                                String nombreUsuario = parent.getItemAtPosition(position).toString();
                                EliminarUsuario(nombreUsuario);
                            }
                        }

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        // Boton flotante, lado derecha abajo (simbolo de una llave)
        fab_crear = (FloatingActionButton) view.findViewById(R.id.fab_crear_ID);
        fab_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_ = new Intent(getActivity(), CrearUsuario.class); // reusar codigo y utilizar la pantalla de crear
                intent_.putExtra("ACCION", "CREAR");

                startActivity(intent_);
            }
        });

        // Mensaje de carga
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargado información...");
        progressDialog.setCancelable(false);

        ConsultarDatosTabla(ClaseGlobal.SELECT_USUARIOS_ALL);

        return view;
    }

    /***
     * Permite actualizar los datos que se despliegan del componente ListaView
     * @param lista: Lista con los elementos a refrescar
     */
    private void ActualizarListView(ArrayList<String> lista)
    {
        theAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item, lista);
        lv_listaComponente.setAdapter(theAdapter);
    }

    /***
     * Funcion que obtiene las consultas hechas mediante los SELECT_*****_ALL.php
     * SOLO SE CONSULTA UNA ETIQUETA!
     * @param URL: Direccion web del archivo php de la consulta
     * @param pEtiquetaPHP: Etiqueta del php, se obtendra el nombre por ejemplo, entonces es 'nombre'
     */
    private void ConsultarDatosTabla(String URL)
    {
        progressDialog.show();
        listaDatosUsuarios = new ArrayList<Usuario>();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    ArrayList<String> listaElementos = new ArrayList<String>();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String nombreUsuario = jsonArray.getJSONObject(i).get("nombreUsuario").toString();
                        listaElementos.add(nombreUsuario);

                        String idUsuario = jsonArray.getJSONObject(i).get("idUsuario").toString();
                        String nombre = jsonArray.getJSONObject(i).get("nombre").toString();
                        String apellidos = jsonArray.getJSONObject(i).get("apellidos").toString();
                        String idRolUsuario = jsonArray.getJSONObject(i).get("idRolUsuario").toString();
                        String correoElectronico = jsonArray.getJSONObject(i).get("correoElectronico").toString();
                        String contrasenia = jsonArray.getJSONObject(i).get("contrasenia").toString();

                        AgregarUsuario(new
                                Usuario(idUsuario, nombre, apellidos, idRolUsuario, nombreUsuario, correoElectronico, contrasenia));

                    }

                    if (listaElementos.size() != 0)
                    {
                        ActualizarListView(listaElementos);
                    }
                }
                catch (JSONException e )
                {
                    Toast.makeText(getActivity(),"Sin datos de usuarios!", Toast.LENGTH_SHORT).show();
                };

                progressDialog.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al solicitar los datos.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

    }

    private void AgregarUsuario(Usuario pUser)
    {
        listaDatosUsuarios.add(pUser);
    }

    private void EliminarUsuario(final String pNombreUsuario)
    {
        final AlertDialog.Builder builderEliminar = new AlertDialog.Builder(getActivity());
        builderEliminar.setTitle("Atención!");
        builderEliminar.setMessage("¿Desea eliminar el usuario " + pNombreUsuario + "?");

        builderEliminar.setPositiveButton("PROCEDER", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                EliminarUsuarioRequest(ClaseGlobal.DELECT_USUARIO +
                        "?nombreUsuario=" + pNombreUsuario);

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

    private void EliminarUsuarioRequest(String URL)
    {
        progressDialog.setMessage("Eliminando usuario...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getString("status").equals("false"))
                    {
                        MessageDialog("Se ha eliminado el usuario!", "Éxito", "Aceptar");

                        ConsultarDatosTabla(ClaseGlobal.SELECT_USUARIOS_ALL);
                    }
                    else
                    {
                        MessageDialog("Error al eliminar el usuario!", "Error", "Aceptar");
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

    private String GetInformacionUsuario(String pNombreUsuario)
    {
        String info = "";
        for(int i = 0; i < listaDatosUsuarios.size(); i++)
        {
            if (pNombreUsuario.equals(listaDatosUsuarios.get(i).nombreUsuario))
            {
                info = listaDatosUsuarios.get(i).toString();
                break;
            }
        }
        return info;
    }

    private Usuario GetObjetoUsuario(String pNombreUsuario)
    {
        Usuario obj = null;
        for(int i = 0; i < listaDatosUsuarios.size(); i++)
        {
            if (pNombreUsuario.equals(listaDatosUsuarios.get(i).nombreUsuario))
            {
                obj = listaDatosUsuarios.get(i);
                break;
            }
        }
        return obj;
    }

    /**
     * Actualizar los datos de la lista cuando se devuelve a esta pantalla despues
     * de una creación
     */
    @Override
    public void onResume()
    {
        super.onResume();
        ConsultarDatosTabla(ClaseGlobal.SELECT_USUARIOS_ALL);
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
