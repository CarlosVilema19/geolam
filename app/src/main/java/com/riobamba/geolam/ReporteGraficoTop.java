package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.TemplatePDFLugares;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class ReporteGraficoTop extends AppCompatActivity {


    public GraphView grafica;
    String id, nombre,vistas;
    Bitmap mBitmap;
    RelativeLayout rl;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private ArrayList<String[]> rowsLugares = new ArrayList<>();
    private ArrayList<String[]> rowsL = null;
    private TemplatePDFLugares templatePDFLug;
    private String[] header2 = {"Nombre", "Número de vistas"};
    private String shorText2 = "Top 10 de los lugares de atención médica más visitados";
    Button btnGenerarPdfLugares;
    DataTable dataTable;
    Toolbar toolbar = new Toolbar();
    TextView txRango;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lugares();
        setContentView(R.layout.activity_reporte_grafico_top);
        dataTable = findViewById(R.id.data_table2);
        grafica=findViewById(R.id.grafica2);
        btnGenerarPdfLugares=findViewById(R.id.btnGenerarPDFLugaresVisitados2);
        rl=findViewById(R.id.Rlay2);

        txRango = findViewById(R.id.tvRango);
        txRango.setText(Html.fromHtml(asignarTexto()));

        btnGenerarPdfLugares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lugares() == 1) {

                    if (rowsLugares.size() > 0) {

                        /*new AlertDialog.Builder(ReporteGraficoTop.this).setIcon(
                                        getDrawable(R.drawable.peligro)
                                )

                                .setTitle("Reporte")
                                .setMessage("El reporte se ha guardado con éxito en el almacenamiento interno del dispositivo")
                                .setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {*/
                                                //listener.removeSafezoneFromServer(center, radius, index);
                                                // confirmacion=1;
                                                pdf2();
                                               // dialog.dismiss();
                                           // }
                                       // })
                                //.show();
//
                        //pdf2();
                    }
                }

            }
        });

        toolbar.show(this, "Reporte", true);

    }

    private String asignarTexto(){
        int dia,mes,anio;
        final Calendar calen = Calendar.getInstance();
        dia = calen.get(Calendar.DAY_OF_MONTH);
        mes = calen.get(Calendar.MONTH) +1;
        anio = calen.get(Calendar.YEAR);
        int mesAnt, anioAnt;
        String texto, mesTexto;
        if(mes == 1) {
            mesAnt = 12;
            anioAnt = anio -1;
        } else {
            mesAnt = mes-1;
            anioAnt = anio;
        }
        if(mes<10){mesTexto="0";} else {mesTexto = "";}


        texto = "Desde: 01/"+mesTexto+mesAnt+"/"+anioAnt+"<br>Hasta: 01/"+mes+"/"+anio+"</br>";

        return texto;
    }

    public int lugares() {
        final ProgressDialog loading = ProgressDialog.show(this, "Obteniendo Datos...", "Espere por favor");
        int bandera = 0;
        ArrayList<String[]> rowsLug = new ArrayList<>();
        String url2;
        ArrayList<String[]> rows = new ArrayList<>();

        //  BarGraphSeries<DataPoint> series1;
        // DataPoint[] series;
        // DataPoint[] series;
        url2 = WebService.urlRaiz + WebService.servicioReporteLugaresVistas;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                response -> {
                    try {
                        //TABLA DE USUARIOS

                        DataTableHeader header = new DataTableHeader.Builder()
                                .item("Nombre", 2)
                                .item("Número de vistas", 2)
                                .build();

                        ArrayList<DataTableRow> rows1 = new ArrayList<>();
//        }
                        dataTable.setTypeface(Typeface.DEFAULT);
                        dataTable.setHeader(header);
                        dataTable.setRows(rows1);
                        dataTable.inflate(this);
                        //
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            id = object.getString("id_lugar");
                            String id2;
                           // id2=object.getString("id_lugar");
                            nombre = object.getString("nombre_lugar");
                            vistas = object.getString("vista");
                            rowsLug.add(new String[]{nombre, vistas});
                            graf(i, nombre, vistas);

                            DataTableRow row = new DataTableRow.Builder()
                                    .value(nombre)
                                    .value(vistas)
                                    .build();
                            rows1.add(row);
                          /*  if(id.equals(id2)) {
                                graf(id, nombre, vistas);
                            }*/
                            //id2=id;

                        }
                        dataTable.setTypeface(Typeface.DEFAULT);
                        dataTable.setHeader(header);
                        dataTable.setRows(rows1);


                        dataTable.inflate(this);
                        lug(rowsLug);
                        loading.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(this, "Error del servidor" + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        stringRequest.setTag("REQUEST");
        queue.add(stringRequest);
        bandera = 1;

        return bandera;
    }
    private void graf(int id, String nombre, String vistas) {

        id=id+1;
        DataPoint[] series = new DataPoint[]
                {
                        new DataPoint(id, Double.parseDouble(vistas)),

                };
        BarGraphSeries<DataPoint> series1 = new BarGraphSeries<>(series);
        grafica.setTitleTextSize(9);
//Añade los datos
        grafica.addSeries(series1);
        //grafica.getViewport().setScalable(true);
        //grafica.getViewport().setScalableY(true);
        // grafica.getViewport().setYAxisBoundsManual(true);
        // grafica.getViewport().setMinY(-150);
        // grafica.getViewport().setMaxY();

        //grafica.getViewport().setXAxisBoundsManual(true);
        grafica.getViewport().setMinX(0);
        grafica.getViewport().setMaxX(12);
        // grafica.getViewport().setScalable(false);
        //grafica.getViewport().setScalableY(false);
        grafica.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        grafica.getViewport().setScrollable(true);  // activate horizontal scrolling
        grafica.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        grafica.getViewport().setScrollableY(true);
        grafica.getDrawingCache();

        ///grafica.scrollBy(200,0);
        //Estilo a las barras
        series1.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) data.getY()*255/6, 100);
            }
        });

        //ESPACIO ENTRE LAS BARRAS
        series1.setSpacing(2);

        //Dibujar la gráfica
        series1.setDrawValuesOnTop(true);
        series1.setValuesOnTopColor(Color.BLUE);


    }
    private void lug(ArrayList<String[]> rowsLug) {
        rowsLugares = rowsLug;
    }

    public ArrayList<String[]> retornar2() {
        return rowsLugares;
    }


    public void pdf2(){


        new AlertDialog.Builder(ReporteGraficoTop.this).setIcon(
                        getDrawable(R.drawable.ic_baseline_verified_24)
                )
                // android.R.drawable.ic_dialog_alert)
                .setTitle("Reporte")
                .setMessage("El archivo se ha guardado con éxito en el almacenamiento interno del dispositivo.")
                .setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                templatePDFLug = new TemplatePDFLugares(getApplicationContext());
                                templatePDFLug.abrirDocumento();
                                templatePDFLug.addMetada("Lista de Lugares de atención médica", "Lugares con más vistas", "Geolam");
                                templatePDFLug.addTitles("Reporte - GEOLAM", "Lugares con más vistas", fecha());
                                templatePDFLug.addParagraph(shorText2);
                                templatePDFLug.addImgName(grafi());
                                templatePDFLug.crearTabla(header2, retornar2());
                                templatePDFLug.cerrarDocumento();
                                templatePDFLug.viewPDF();

                            }
                        })
                .show();

        /*templatePDFLug = new TemplatePDFLugares(getApplicationContext());
        templatePDFLug.abrirDocumento();
        templatePDFLug.addMetada("Lista de Lugares de atención médica", "Lugares con más vistas", "Geolam");
        templatePDFLug.addTitles("Reporte - GEOLAM", "Lugares con más vistas", fecha());
        templatePDFLug.addParagraph(shorText2);
        templatePDFLug.addImgName(grafi());
        templatePDFLug.crearTabla(header2, retornar2());
        templatePDFLug.cerrarDocumento();
        templatePDFLug.viewPDF();*/
        grafica.removeAllSeries();
    }
    public Bitmap grafi () {
        rl.setDrawingCacheEnabled(true);
        mBitmap =Bitmap.createBitmap(rl.getDrawingCache());
        rl.setDrawingCacheEnabled(false);
       // grafica.removeAllSeries();
        return mBitmap;
    }
    private String fecha() {
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy '|' HH:mm:ss");
        Date todayDate = new Date();
        String thisDateTime = currentDate.format(todayDate);
        return thisDateTime;
    }



//OTROS

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Funcion para ejecutar las instrucciones de los items -- proviene de la clase Toolbar

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        toolbar.getContexto(this);
        toolbar.ejecutarItemSelected(item, this);
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}