package com.riobamba.geolam;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.riobamba.geolam.modelo.TemplatePDF;
import com.riobamba.geolam.modelo.TemplatePDFLugares;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportesAdmin extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    //VARIABLES PARA PDF DE LISTADO DE USUARIOS
    private String[] header = {"Tipo de usuario", "Nombre"};
    private String shorText = "Lista de usuarios registrados en la aplicación Geolam";
    //  private String longText = "Nunca consideres el estudio como una obligación, sino como una oportunidad de vida";
    private TemplatePDF templatePDF;
    private ArrayList<String[]> rowsUsuarios = new ArrayList<>();
    private ArrayList<String[]> rows = null;
    Button btnGenerarPdfUsu,prueba;
    Bitmap mBitmap;
    RelativeLayout rl;

    //VARIABLES PARA PDF DE LUGARES DE ATENCIÓN MÉDICA MÁS VISITADOS
    private ArrayList<String[]> rowsLugares = new ArrayList<>();
    private ArrayList<String[]> rowsL = null;
    private TemplatePDFLugares templatePDFLug;
    private String[] header2 = {"Nombre", "Número de vistas"};
    private String shorText2 = "Top 10 de los lugares de atención médica más visitados";
    Button btnGenerarPdfLugares;

    Toolbar toolbar = new Toolbar();

    //Gráfica
    public GraphView grafica;
    String id,nombre,vistas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuarios();
        //lugares();

        setContentView(R.layout.activity_reportes_admin);
        btnGenerarPdfUsu = findViewById(R.id.btnGenerarPDFUsuarios);
       // btnGenerarPdfLugares = findViewById(R.id.btnGenerarPDFLugaresVisitados);
       // grafica=findViewById(R.id.grafica);
//        rl=findViewById(R.id.Rlay);
//

      // grafi();
        prueba = findViewById(R.id.btnprueba);


        btnGenerarPdfUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                if (usuarios() == 1) {
                    if (rowsUsuarios.size() > 0) {

                        pdf();
                    }
                }
*/
                Intent intent = new Intent(ReportesAdmin.this, ReporteGraficoUsuarios.class);
                startActivity(intent);
            }
        });


        prueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportesAdmin.this, ReporteGraficoTop.class);
                startActivity(intent);
            }
        });

        toolbar.show(this, "", true);
       // grafico();
    }


    //getActivity()

    public void pdfView(View view) {
        templatePDF.viewPDF();

    }


    public void pdf() {

        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.abrirDocumento();
        templatePDF.addMetada("Listado de Usuarios", "Administradores - Usuarios", "Geolam");
        templatePDF.addTitles("Reporte - GEOLAM", "Administradores & Usuarios", fecha());
        templatePDF.addParagraph(shorText);
        templatePDF.crearTabla(header, retornar());
        templatePDF.cerrarDocumento();
        templatePDF.viewPDF();
    }

    private String fecha() {
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy '|' HH:mm:ss");
        Date todayDate = new Date();
        String thisDateTime = currentDate.format(todayDate);
        return thisDateTime;
    }

    public int usuarios() {
        int bandera = 0;
        ArrayList<String[]> rows = new ArrayList<>();
        String url2;
        url2 = WebService.urlRaiz + WebService.servicioReporteUsuarios;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                response -> {

                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            String id = object.getString("descripcion_tipo_usuario");
                            String nombre = object.getString("nombre_usuario").concat(" ").concat(object.getString("apellido_usuario"));
                            rows.add(new String[]{id, nombre});


                        }
                        usu(rows);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(this, "Error del servidor", Toast.LENGTH_SHORT).show();
        }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        stringRequest.setTag("REQUEST");
        queue.add(stringRequest);
        bandera = 1;
        return bandera;
    }


    public void usu(ArrayList<String[]> rows) {
        rowsUsuarios = rows;
    }


    public ArrayList<String[]> retornar() {
        return rowsUsuarios;
    }


    //PDF LUGARES DE ATENCIÓN MÉDICA  MÁS VISITADOS

    public int lugares() {
        int bandera = 0;
        ArrayList<String[]> rowsLug = new ArrayList<>();
        String url2;
        //String id, nombre,vistas;
      //  BarGraphSeries<DataPoint> series1;
       // DataPoint[] series;
       // DataPoint[] series;
        url2 = WebService.urlRaiz + WebService.servicioReporteLugaresVistas;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            id = object.getString("id_lugar");
                            String id2;
                            id2=object.getString("id_lugar");
                            nombre = object.getString("nombre_lugar");
                            vistas = object.getString("vista");
                            rowsLug.add(new String[]{nombre, vistas});
                            graf(i, nombre, vistas);
                          /*  if(id.equals(id2)) {
                                graf(id, nombre, vistas);
                            }*/
                            //id2=id;

                        }
                        lug(rowsLug);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(this, "Error del servidor", Toast.LENGTH_SHORT).show();
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
        BarGraphSeries<DataPoint>series1 = new BarGraphSeries<>(series);
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

    public Bitmap grafi () {
        rl.setDrawingCacheEnabled(true);
        mBitmap =Bitmap.createBitmap(rl.getDrawingCache());
        rl.setDrawingCacheEnabled(false);
    return mBitmap;
    }

    private void lug(ArrayList<String[]> rowsLug) {
        rowsLugares = rowsLug;
    }

    public ArrayList<String[]> retornar2() {
        return rowsLugares;
    }

    public void pdf2(){

        templatePDFLug = new TemplatePDFLugares(getApplicationContext());
        templatePDFLug.abrirDocumento();
        templatePDFLug.addMetada("Lista de Lugares de atención médica", "Lugares con más vistas", "Geolam");
        templatePDFLug.addTitles("Reporte - GEOLAM", "Lugares con más vistas", fecha());
        templatePDFLug.addParagraph(shorText2);
        ReporteGraficoTop repor=new ReporteGraficoTop();

        templatePDFLug.addImgName(repor.mBitmap);
        templatePDFLug.crearTabla(header2, retornar2());
        templatePDFLug.cerrarDocumento();
        templatePDFLug.viewPDF();
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
