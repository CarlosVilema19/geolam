package com.riobamba.geolam;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.TemplatePDF;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ReportesAdmin extends AppCompatActivity {
    //private  String [] header={"ID","Nombre del Lugar"};
    private String[] header = {"Tipo de usuario", "Nombre"};
    private String shorText = "Lista de usuarios registrados en la aplicación Geolam";
  //  private String longText = "Nunca consideres el estudio como una obligación, sino como una oportunidad de vida";
    private TemplatePDF templatePDF;
    private ArrayList<String[]> rows3 = new ArrayList<>();
    private ArrayList<String[]> rows = null;
    Button btnGenerarPdf;



    // ArrayList<String[]>rows=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuarios();
        setContentView(R.layout.activity_reportes_admin);
        btnGenerarPdf=findViewById(R.id.btnGenerar);

        btnGenerarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (usuarios() == 1) {
                        if (rows3.size() > 0) {
//
                           pdf();
                        }
                    }

            }
        });
    }


    //getActivity()

    public void pdfView(View view) {
        templatePDF.viewPDF();

    }
public void pdf(){

    templatePDF = new TemplatePDF(getApplicationContext());
    templatePDF.abrirDocumento();
    //templatePDF.addMetada("Lugares de atención médica", "Los más buscados","Geolam");
    //templatePDF.addTitles("GEOLAM","Lugares de atencipon médica","05/08/2022");
    templatePDF.addMetada("Listado de Usuarios", "Administradores - Usuarios", "Geolam");
    templatePDF.addTitles("Reporte - GEOLAM", "Administradores & Usuarios", fecha());

    templatePDF.addParagraph(shorText);
   // templatePDF.addParagraph(longText);
    templatePDF.crearTabla(header, retornar());
    // templatePDF.crearTabla(header, );

    templatePDF.cerrarDocumento();
    templatePDF.viewPDF();
}

    private String fecha() {
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy '|' HH:mm:ss");
        ///SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        Date todayDate = new Date();
        String thisDateTime = currentDate.format(todayDate);
        //String thisTime = currentDate .format(new Date());
        return thisDateTime;
    }
/*
    private ArrayList<String[]> getLugares() {

       // for(int i=0; i<3;i++) {
            rows.add(new String[]{"1", "San Juan"});
            rows.add(new String[]{"2", "Metro"});
            rows.add(new String[]{"3", "ESPOCH"});
            */

/*
        for(int i=0; i<rows.size();i++) {

                System.out.println(Arrays.toString(rows.get(i)));

        }*/
        /*

        Iterator it = rows.iterator();
        while(it.hasNext()) {
            System.out.println(it.next()+"hola");
        }
*/
        // recorrer(rows);
       // return rows;

  //  }
        /*private void recorrer(ArrayList<String[]>rows){
            int i;
            for(i=0;i<=rows.size();i++){
                System.out.println(rows.get(i));
            }
        }*/
/*
    private ArrayList<String[]> getUsuarios(ArrayList<String[]> rows) {
        rows2=rows;
        return rows;
    }

*/
    public int usuarios() {
        int bandera=0;
         ArrayList<String[]> rows=new ArrayList<>();

        String url2;

        url2 = WebService.urlRaiz + WebService.servicioReporteUsuarios;

      //  ArrayList<String[]> rows3 = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                response -> {

                    try {


                        JSONArray array = new JSONArray(response);
                        //leer(array);

                        for (int i = 0; i < array.length(); i++) {
                            // if (array.length() > 0) {
                           // ArrayList<String[]> a1 = new ArrayList<>();
                            JSONObject object = array.getJSONObject(i);
                            String id = object.getString("descripcion_tipo_usuario");
                            String nombre = object.getString("nombre_usuario").concat(" ").concat(object.getString("apellido_usuario"));

                            //String[] varArr = new String[]{id.trim(),nombre.trim()};
                            //rows.add(varArr);
                            rows.add(new String[]{id,nombre});


                       }
                        usu(rows);
                        /*
                        for(int j=0; j<rows.size();j++) {

                            System.out.println(Arrays.toString(rows.get(j)));

                        }*/



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
        bandera=1;
        return bandera;
    }

 /*
    private void leer(JSONArray array) throws JSONException {
        //ArrayList<String[]> rows3 = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            // if (array.length() > 0) {
            ArrayList<String[]> a1 = new ArrayList<>();
            JSONObject object = array.getJSONObject(i);
            String id = object.getString("id_tipo_usuario");
            String nombre = object.getString("nombre_usuario");

            //String[] varArr = new String[]{id.trim(),nombre.trim()};
            //rows.add(varArr);

            rows3.add(new String[]{id, nombre});

        }
    }
*/

       public void usu(ArrayList<String[]> rows) {
           rows3= rows;
          /* for(int j=0; j<rows3.size();j++) {

               System.out.println(Arrays.toString(rows3.get(j)));

           }*/

        }



    public ArrayList<String[]>retornar()
    {
        /*
        for(int j=0; j<rows3.size();j++) {

            System.out.println(Arrays.toString(rows3.get(j)));

        }*/
        return rows3;
    }
}
