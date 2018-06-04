package reque.proyecto2.jose_.proindust_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import reque.proyecto2.jose_.proindust_app.modelo.Proyecto;

public class GraficosActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private PieChart pieChart;
    private BarChart barChart;
    private LineChart lineChart;
    private String[] valores = new String[]{"Productivas", "Contributivas", "Improductivas"};
    private int[] cantidades = new int[]{10, 2, 30};
    private int[] colores = new int[]{Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.LTGRAY};
    private Spinner sp_proyecto, sp_muestreoProyecto;
    private ArrayAdapter<String> adapterSpinner_proyecto, adapterSpinner_muestreoProyecto;

    private String proyectoSeleccionado, muestreoProyectoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sp_proyecto = (Spinner) findViewById(R.id.sp_proyecto_ID);
        sp_muestreoProyecto = (Spinner) findViewById(R.id.sp_muestreos_ID);

        adapterSpinner_proyecto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
        adapterSpinner_proyecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_proyecto.setAdapter(adapterSpinner_proyecto);
        sp_proyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                proyectoSeleccionado = sp_proyecto.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        adapterSpinner_muestreoProyecto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
        adapterSpinner_muestreoProyecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_muestreoProyecto.setAdapter(adapterSpinner_muestreoProyecto);
        sp_muestreoProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                muestreoProyectoSeleccionado = sp_muestreoProyecto.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        // List<Integer> arrayList = new ArrayList<>(); //El problema es aqui, este arreglo no agarra los datos
        // arrayList = GetTareaType("22");

//        valores = new String[]{"Productivas", "Contributivas", "Improductivas"};

        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
        lineChart = findViewById(R.id.lineChart);

        crearGraficas();

    }

    public void crearGraficas(){

        lineChart = (LineChart) getSameChart(lineChart, "", Color.BLUE, Color.WHITE, 3000,true);
        lineChart.setData(getLineData());
        lineChart.invalidate();
        ejeX(lineChart.getXAxis());
        ejeY_izq(lineChart.getAxisLeft());
        ejeY_der(lineChart.getAxisRight());

        barChart = (BarChart) getSameChart(barChart, "", Color.WHITE, Color.WHITE, 3000, true);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(true);
        barChart.setData(getBarData());
        barChart.invalidate();
        ejeX(barChart.getXAxis());
        ejeY_izq(barChart.getAxisLeft());
        ejeY_der(barChart.getAxisRight());
        barChart.getLegend().setEnabled(false);

        pieChart = (PieChart) getSameChart(pieChart, "Actividades Productivas", Color.BLACK, Color.WHITE, 3000, true);
        pieChart.setHoleRadius(10);
        pieChart.setTransparentCircleRadius(12);
        pieChart.setData(getPieData());
        pieChart.invalidate();
        pieChart.setDrawHoleEnabled(false);
    }

    private List<Integer> GetTareaType(String idOperacion)
    {
        progressDialog.setMessage("Construyendo gráfica...");
        progressDialog.show();

        String URL = ClaseGlobal.GET_TAREAS_DATA + "?idOperacion=" + idOperacion;
        final List<Integer> array = new ArrayList<Integer>();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { // response -> {"status":"false"} o true
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("value");

                    int productiva = 0;
                    int colaborativa = 0;
                    int improductiva = 0;
                    int actividad;

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
//                        MessageDialog(jsonArray.getJSONObject(i).get("idActividad").toString(), "", "Aceptar");
                        actividad = Integer.parseInt(jsonArray.getJSONObject(i).get("idActividad").toString());

                        if(actividad == 1){
                            productiva++;
                        } else if(actividad == 2){
                            colaborativa++;
                        } else{
                            improductiva++;
                        }
                        Log.d("PUTA", Integer.toString(productiva));

                    }
                    array.add(productiva);
                    array.add(colaborativa);
                    array.add(improductiva);
                    //cantidades[0] = productiva;
                    //cantidades[1] = colaborativa;
                    //cantidades[2] = improductiva;
//                    MessageDialog(array.get(0).toString() + " index 0", "", "Aceptar");
//                    MessageDialog(array.get(1).toString() + " index 1", "", "Aceptar");
//                    MessageDialog(array.get(2).toString() + " index 2", "", "Aceptar");

                }catch (JSONException e){
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                MessageDialog("Error al construir la grafica solicitada.\nIntente mas tarde!.",
                        "Error", "Aceptar");
            }
        });queue.add(stringRequest);

        return array;
    }

    private Chart getSameChart(Chart chart, String descripcion, int color_texto, int bg, int animacion, Boolean legend){
        chart.getDescription().setText(descripcion);
        chart.getDescription().setTextSize(15);
        chart.getDescription().setTextColor(color_texto);
        chart.setBackgroundColor(bg);
        chart.animateY(animacion);

        if(legend) {
            leyenda(chart);
        }

        return chart;
    }

    private void leyenda(Chart chart){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for(int i = 0; i < valores.length; i++){
            LegendEntry entry = new LegendEntry();
            entry.formColor = colores[i];
            entry.label = valores[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<BarEntry> getBarEntries(){
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i< cantidades.length; i++){
            entries.add(new BarEntry(i, cantidades[i]));
        }

        return entries;
    }

    private ArrayList<PieEntry> getPieEntries(){
        ArrayList<PieEntry> entries = new ArrayList<>();

        for(int i = 0; i< cantidades.length; i++){
            entries.add(new PieEntry(cantidades[i]));
        }

        return entries;
    }

    private void ejeX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(valores));
//        axis.setEnabled(false);
    }

    private void ejeY_izq(YAxis axis){
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
    }

    private void ejeY_der(YAxis axis){
        axis.setEnabled(false);
    }

    private ArrayList<Entry> getLineEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < cantidades.length; i++)
            entries.add(new Entry(i, cantidades[i]));
        return entries;
    }


    private DataSet getData(DataSet dataSet, int color){
        dataSet.setColors(colores);
        dataSet.setValueTextColor(color);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getBarData(){
        BarDataSet barDataSet = (BarDataSet) getData(new BarDataSet(getBarEntries(), ""), Color.BLACK);
//        barDataSet.setBarShadowColor(Color.GRAY);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.45f);

        return barData;
    }

    private LineData getLineData() {
        LineDataSet lineDataSet = (LineDataSet) getDataSame(new LineDataSet(getLineEntries(), ""));
        lineDataSet.setLineWidth(2.5f);
        //Color de los circulos de la grafica
        lineDataSet.setCircleColors(colores);
        //Tamaño de los circulos de la grafica
        lineDataSet.setCircleRadius(5f);
        //Sombra grafica
        lineDataSet.setDrawFilled(true);
        //Estilo de la linea picos(linear) o curveada(cubic) cuadrada(Stepped)
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        return new LineData(lineDataSet);
    }

    private PieData getPieData(){
        PieDataSet pieDataSet = (PieDataSet) getData(new PieDataSet(getPieEntries(), ""), Color.WHITE);

        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueFormatter(new PercentFormatter());

        return new PieData(pieDataSet);
    }

    private DataSet getDataSame(DataSet dataSet){
        dataSet.setColors(colores);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }


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
