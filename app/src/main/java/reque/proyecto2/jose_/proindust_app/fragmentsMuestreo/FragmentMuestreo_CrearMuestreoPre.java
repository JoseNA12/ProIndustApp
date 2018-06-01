package reque.proyecto2.jose_.proindust_app.fragmentsMuestreo;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reque.proyecto2.jose_.proindust_app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMuestreo_CrearMuestreoPre extends Fragment {

    private boolean isBackFromB;

    public FragmentMuestreo_CrearMuestreoPre() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_muestreo__crear_muestreo_pre, container, false);
    }

    /**
     * Utilizado para recargar los datos, al momento de hacer un cambio en la bd
     */
    private void RecargarFragmento()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        isBackFromB = true;
    }

    /**
     * Recargar el fragmento cuando se presiona el boton de atras en la pantalla de Crear
     */
    @Override
    public void onResume() {
        super.onResume();

        if (isBackFromB) { RecargarFragmento(); } //Do something
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
