package reque.proyecto2.jose_.proindust_app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

import java.util.ArrayList;

public class GraficosActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private LineChart lineChart;
    private String[] meses = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo"};
    private int[] ventas = new int[]{25, 30, 38, 10, 15};
    private int[] colores = new int[]{Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.LTGRAY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
        lineChart = findViewById(R.id.lineChart);

        crearGraficas();

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
        for(int i=0; i<meses.length; i++){
            LegendEntry entry = new LegendEntry();
            entry.formColor = colores[i];
            entry.label = meses[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<BarEntry> getBarEntries(){
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i=0; i<ventas.length; i++){
            entries.add(new BarEntry(i, ventas[i]));
        }

        return entries;
    }

    private ArrayList<PieEntry> getPieEntries(){
        ArrayList<PieEntry> entries = new ArrayList<>();

        for(int i=0; i<ventas.length; i++){
            entries.add(new PieEntry(ventas[i]));
        }

        return entries;
    }

    private void ejeX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(meses));
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
        for (int i = 0; i < ventas.length; i++)
            entries.add(new Entry(i, ventas[i]));
        return entries;
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
}
