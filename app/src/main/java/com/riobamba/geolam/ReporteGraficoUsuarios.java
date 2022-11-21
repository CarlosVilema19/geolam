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
import android.os.AsyncTask;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.TemplatePDF;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class ReporteGraficoUsuarios extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    //VARIABLES PARA PDF DE LISTADO DE USUARIOS
    private String[] header = {"Tipo de usuario", "Nombre"};
    private String shorText = "Lista de usuarios registrados en la aplicación Geolam";
    //  private String longText = "Nunca consideres el estudio como una obligación, sino como una oportunidad de vida";
    private TemplatePDF templatePDF;

    private ArrayList<String[]> rowsUsuarios = new ArrayList<>();
    private ArrayList<String[]> rows = null;
    PieDataSet pieDataSet;

    DataTable dataTable;
    Button btnGenerarPdfUsu;

    Bitmap mBitmap;
    //PieDataSet pieDataSet;
    //PieData pieData;
    int contarAdministrador=0;
    int contarUsuario=0;
    int cA=0;
    int cU=0;
   // ArrayList<PieEntry> pieEntries= new ArrayList<>();
    RelativeLayout rl;
    PieChart pieChart;
    int bandera = 0;
   // Description description= new Description();
    //ArrayList<PieEntry> pieEntries= new ArrayList<>();
   ArrayList<PieEntry> pieEntries = new ArrayList<>();
    ArrayList<PieEntry> pieEntries2  = new ArrayList<>();
    Toolbar toolbar = new Toolbar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
usuarios();

     /*AsyncTask.execute(new Runnable() {
          @Override
         public void run() {


        usuarios();


            }
       }
      );*/

        setContentView(R.layout.activity_reporte_grafico_usuarios);
        dataTable = findViewById(R.id.data_table);
        pieChart=findViewById(R.id.graficoPastel);


        btnGenerarPdfUsu=findViewById(R.id.btnGenerarPDFUsuarios3);
        rl=findViewById(R.id.Rlay3);
        btnGenerarPdfUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  pieData.clearValues();
                //pieChart.clear();

                if (usuarios() == 1) {

                    //crearGrafico();
                    if (rowsUsuarios.size() > 0) {
                       // if(!pieChart.equals("")) {
//
                       /* new AlertDialog.Builder(ReporteGraficoUsuarios.this).setIcon(
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
                                                pdf();
                                               // dialog.cancel();
                                           // }
                                        //})
                                //.show();


                        //}
                    }
                }

            }
        });
        toolbar.show(this, "Reporte", true);


    }


    public int usuarios() {
        final ProgressDialog loading = ProgressDialog.show(this, "Obteniendo Datos...", "Espere por favor");
        bandera = 0;
        ArrayList<String[]> rows = new ArrayList<>();
        String url2;
        url2 = WebService.urlRaiz + WebService.servicioReporteUsuarios;
       // ArrayList<PieEntry> pieEntries= new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                response -> {
                    String id;
                    String nombre;
                    try {
                        //TABLA DE USUARIOS

                        DataTableHeader header = new DataTableHeader.Builder()
                                .item("Tipo", 2)
                                .item("Nombre", 2)
                                .build();

                        ArrayList<DataTableRow> rows1 = new ArrayList<>();
//        }
                        dataTable.setTypeface(Typeface.DEFAULT);
                        dataTable.setHeader(header);
                        dataTable.setRows(rows1);
                        dataTable.inflate(this);
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            id = object.getString("descripcion_tipo_usuario");
                            nombre = object.getString("nombre_usuario").concat(" ").concat(object.getString("apellido_usuario"));

                            if (id.equals("ADMINISTRADOR")) {
                                cA++;
                            } else {
                                cU++;
                            }

                            rows.add(new String[]{id, nombre});
                            DataTableRow row = new DataTableRow.Builder()
                                    .value(id)
                                    .value(nombre)
                                    .build();
                            rows1.add(row);
                            //crearGrafico(id);


                        }
                        ;
                        //crearGrafico();
                        //crearGrafico(contarAdministrador,contarUsuario);
                       /* dataTable.setTypeface(Typeface.DEFAULT);
                        dataTable.setHeader(header);
                        dataTable.setRows(rows1);
*/

                        dataTable.inflate(this);
                        usu(rows);

                        //DIAGRAMA DE PASTEL


                        //
                        // description= new Description();


                       // pieEntries.clear();
                        //pieChart.removeAllViews();
                        //pieChart.destroyDrawingCache();
                        pieEntries.clear();
                       // pieDataSet.setValues(pieEntries);
                        pieChart.removeAllViews();

                        Description description= new Description();
                        description.setText("Usuarios Registrados");
                        description.setTypeface(Typeface.DEFAULT);
                        description.setTextSize(9);
                        pieChart.setDescription(description);
                       /*
                            ArrayList<PieEntry> pieEntries = new ArrayList<>();
                            pieEntries.add(new PieEntry(contAdmin(2)));
                            pieEntries.add(new PieEntry(contUsu(2)));
                        */
                        // pieEntries.add(new PieEntry(6,8));
                        pieEntries.add(new PieEntry(Math.round(contAdmin(cA))));
                        pieEntries.add(new PieEntry(Math.round(contUsu(cU))));
                        pieEntries2.add(new PieEntry(Math.round(contUsu(cU)),""));
                        pieDataSet = new PieDataSet(pieEntries,"Usuario-Administrador");
                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        pieDataSet.setValueTextSize(14);
                        pieDataSet.setValueTextColor(Color.rgb(255,255,255));
                        PieData pieData= new PieData(pieDataSet);

                        pieChart.setData(pieData);
                       // pieChart.setCenterTextSize(14);

                        loading.dismiss();
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

   /* private void tablaUsuarios(String id, String nombre, int length) {




        ArrayList<DataTableRow> rows1 = new ArrayList<>();
        // define 200 fake rows for table
      //  for (int i = 0; i <length; i++) {
        //    Random r = new Random();
          //  int random = r.nextInt(i + 1);
            //int randomDiscount = r.nextInt(20);
            DataTableRow row = new DataTableRow.Builder()
                    //.value("Product #" + i)
                    .value(id)
                    .value(nombre)
                    //.value(String.valueOf(random))
                   // .value(String.valueOf(random * 1000).concat("$"))
                    //.value(String.valueOf(randomDiscount).concat("%"))

                    .build();
            rows1.add(row);
//        }

        dataTable.setTypeface(Typeface.DEFAULT);
        dataTable.setHeader(header);
        dataTable.setRows(rows1);
        dataTable.inflate(this);
    }*/

private int contAdmin(int contAdministrador){
   contarAdministrador=contAdministrador;
   return contarAdministrador;

}

    private int contUsu(int contarUsu){
        contarUsuario=contarUsu;
        return contarUsuario;
    }
    private int cAdmin(){

    return contarAdministrador;
    }
    private int cUser(){

        return contarUsuario;
    }


/*
    private int crearGrafico() {
 int band=0;

        Description description= new Description();
        ArrayList<PieEntry> pieEntries= new ArrayList<>();
        // Description description= new Description();
        description.setText("Usuarios de GEOLAM");
        pieChart.setDescription(description);
        // ArrayList<PieEntry> pieEntries= new ArrayList<>();
        pieEntries.add(new PieEntry(2,2));
        pieEntries.add(new PieEntry(5,2));
        //pieEntries.add(new PieEntry(contUsu(cU),1));

        // pieEntries.add(new PieEntry(6,8));
        pieDataSet= new PieDataSet(pieEntries, "Usuario - Administrador");

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieData= new PieData(pieDataSet);

        pieChart.setData(pieData);
        return 1;
    }
*/
    /*

private void crearGrafico() {



    Description description= new Description();
    description.setText("Gráfico de Pastel Usuarios");
    pieChart.setDescription(description);
    ArrayList<PieEntry> pieEntries= new ArrayList<>();
    pieEntries.add(new PieEntry(cAdmin(),8));
    pieEntries.add(new PieEntry(cUser(),3));

    // pieEntries.add(new PieEntry(6,8));
    PieDataSet pieDataSet= new PieDataSet(pieEntries, "descrip");
    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
    PieData pieData= new PieData(pieDataSet);
    pieChart.setData(pieData);

}
*/


    public void pdf() {
        new AlertDialog.Builder(ReporteGraficoUsuarios.this).setIcon(
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
                                //listener.removeSafezoneFromServer(center, radius, index);
                                // confirmacion=1;
                               // pdf();
                                // dialog.cancel();

                                templatePDF = new TemplatePDF(getApplicationContext());
                                templatePDF.abrirDocumento();
                                templatePDF.addMetada("Listado de Usuarios", "Administradores - Usuarios", "Geolam");
                                templatePDF.addTitles("Reporte - GEOLAM", "Administradores & Usuarios", fecha());
                                templatePDF.addParagraph(shorText);
                                templatePDF.addImgName(grafi());
                                templatePDF.crearTabla(header, retornar());
                                templatePDF.cerrarDocumento();
                                templatePDF.viewPDF();
                            }
                        })
                .show();
        /*templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.abrirDocumento();
        templatePDF.addMetada("Listado de Usuarios", "Administradores - Usuarios", "Geolam");
        templatePDF.addTitles("Reporte - GEOLAM", "Administradores & Usuarios", fecha());
        templatePDF.addParagraph(shorText);
        templatePDF.addImgName(grafi());
        templatePDF.crearTabla(header, retornar());
        templatePDF.cerrarDocumento();
        templatePDF.viewPDF();*/

       // pieEntries.clear();
       // pieChart.clearValues();
        //pieDataSet.clear();
        //pieChart.setData(pi);
        //-----
        pieChart.removeAllViews();
        cA=0;
        cU=0;

        pieEntries.clear();
        pieDataSet.setValues(pieEntries);
        pieChart.removeAllViews();
        //------

    //    pieChart.clearValues();
      //  pieChart.clear();
        //pieChart.refreshDrawableState();
       // pieChart.destroyDrawingCache();
        //pieChart.clear();
        //pieChart.setData(null);
        //rl.clearFocus();
       // pieChart.removeAllViews();
       // pieChart.setData(null);
       // pieEntries.clear();
        //pieDataSet.clear();
      //  pieData.clearValues();
        //pieChart.clear();

        //pieChart.setData(pieData);
       // pieEntries.clear();
        /*pieEntries.clear();
        pieDataSet.setValues(pieEntries);
        pieChart.removeAllViews();
*/
       // pieDataSet.
       // grafica.removeAllSeries();
    }

    private String fecha() {
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy '|' HH:mm:ss");
        Date todayDate = new Date();
        String thisDateTime = currentDate.format(todayDate);
        return thisDateTime;
    }
    public void usu(ArrayList<String[]> rows) {
        rowsUsuarios = rows;
    }


    public ArrayList<String[]> retornar() {
        return rowsUsuarios;
    }

    public Bitmap grafi () {
        rl.setDrawingCacheEnabled(true);
        mBitmap =Bitmap.createBitmap(rl.getDrawingCache());
        rl.setDrawingCacheEnabled(false);
        // grafica.removeAllSeries();
        return mBitmap;
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