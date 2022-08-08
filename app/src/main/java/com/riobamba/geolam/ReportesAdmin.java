package com.riobamba.geolam;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoAsignarEspecialidadAdaptador;
import com.riobamba.geolam.modelo.TemplatePDF;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportesAdmin<array1> extends AppCompatActivity {
    // private  String [] header={"ID","Nombre del Lugar"
    private String[] header = {"Tipo de usuario", "Nombre"
    };
    private String shorText = "Hola";
    private String longText = "Nunca consideres el estudio como una obligación, sino como una oportunidad de vida";
    private TemplatePDF templatePDF;
    private ArrayList<String[]> rows3 = new ArrayList<>();

    // ArrayList<String[]>rows=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_admin);
      //  usuarios();
        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.abrirDocumento();
        //templatePDF.addMetada("Lugares de atención médica", "Los más buscados","Geolam");
        //templatePDF.addTitles("GEOLAM","Lugares de atencipon médica","05/08/2022");
        templatePDF.addMetada("Listado de Lugares", "Lugares", "Geolam");
        templatePDF.addTitles("GEOLAM", "Lugares", "08/08/2022");
        //usuarios();
        templatePDF.addParagraph(shorText);
        templatePDF.addParagraph(longText);
        templatePDF.crearTabla(header, getLugares());
        // templatePDF.crearTabla(header, );
        templatePDF.cerrarDocumento();


    }


    //getActivity()

    public void pdfView(View view) {
        templatePDF.viewPDF();

    }


    private ArrayList<String[]> getLugares() {
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"1", "San Juan"});
        rows.add(new String[]{"2", "Metro"});
        rows.add(new String[]{"3", "ESPOCH"});
        // recorrer(rows);
        return rows;

    }
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

   /* private void  usuarios() {
        // ArrayList<String[]> rows=new ArrayList<>();

        String url2;

        url2 = WebService.urlRaiz + WebService.servicioReporteUsuarios;

        ArrayList<String[]> rows3 = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                response -> {

                    try {


                        JSONArray array = new JSONArray(response);
                        leer(array);
                        /*
                        for (int i = 0; i < array.length(); i++) {
                            // if (array.length() > 0) {
                            ArrayList<String[]> a1 = new ArrayList<>();
                            JSONObject object = array.getJSONObject(i);
                            String id = object.getString("id_tipo_usuario");
                            String nombre = object.getString("nombre_usuario");
*/
                            //String[] varArr = new String[]{id.trim(),nombre.trim()};
                            //rows.add(varArr);
                            //rows3.add(new String[]{id, nombre});
                            // usu(rows3);
                            //rows.addAll(varArr);
                            // System.out.println(Arrays.toString(varArr));

                            // ArrayList<String[]>rows=new ArrayList<>();
                            //rows.add(new String[]{"1","San Juan"});
                            // System.out.println(Arrays.toString(new ArrayList[]{rows}));

                            //String id=object.getString("id_tipo_usuario");
                            //String nombre=object.getString("nombre_usuario");
                            // rows.add(new String[]{id,nombre});
                            //rows.add(new String[]{"1","San Juan"});
                            //object.getString("id_tipo_usuario").trim(),object.getString("nombre_usuario").trim()
                            //rows.add(varArr);
                            //Collections.addAll(rows, varArr);

                                /*
                                Iterator it = rows.iterator();
                                while(it.hasNext()) {
                                    System.out.println(it.next());
                                }
                                */

                            // System.out.println(Arrays.toString(varArr));
                            // recorrer(varArr);

                            // getUsuarios(rows);
                            //object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                            // rows.add(i,new String[] { object.getString("id_tipo_usuario").toString(), object.getString("nombre_usuario").toString()});
                            //  rows.add(i,new String[] { object.getString("id_tipo_usuario").toString(), object.getString("nombre_usuario").toString()});
                            // Toast.makeText(getApplicationContext(), "oooo " + object.getString("id_tipo_usuario").toString(), Toast.LENGTH_SHORT).show();
                            //rows.add(new String[]{"1","San Juan"});
                            // Toast.makeText(getApplicationContext(), "Filas: " + rows.get(0).toString(), Toast.LENGTH_SHORT).show();
                            // rows.add(a1);

                    //    }

/*

                            Iterator it = rows.iterator();
                            while(it.hasNext()) {
                                System.out.println(it.next());
                            }
                            */
/*
                        ArrayList<String[]> rows1 = rows3;
                        // return rows3;
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
        //return rows3;
    }
*/
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
    /*
        private void usu(ArrayList<String[]> rows) {
            for (int i=0; i<rows.size();i++) {
                rows2.add(i, rows.get(i));
            }

        }
    */
        /*
    private void recorrer(String[] rows) {
       // System.out.println(rows);

       // for (int j = 0; j < rows.size(); j++) {

            //rows2.add(rows);
        //}

    }
    */



/*
    private JSONArray parse() {

        //  text.replaceAll("^\\s*","");
        //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        String url2;
        //ArrayList<String[]>rows=new ArrayList<>();
        url2 = WebService.urlRaiz + WebService.servicioReporteUsuarios;
        // url2=url2.replace(" ", "%20");
        //ArrayList<String[]>rows=new ArrayList<>();
        // actv.setAdapter(adaptador);
        JSONArray array = null;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                response -> {

                    try {


                       array = new JSONArray(response);


                       /* for (int i = 0; i < array.length(); i++) {
                            // if (array.length() > 0) {
                            JSONObject object = array.getJSONObject(i);
                            String[] varArr = new String[]{
                                    object.getString("id_tipo_usuario").toString(),
                                    object.getString("nombre_usuario").toString()
                            };

                            rows.add(varArr);
                            System.out.println(Arrays.toString(varArr));
                            //object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                            // rows.add(i,new String[] { object.getString("id_tipo_usuario").toString(), object.getString("nombre_usuario").toString()});
                            //  rows.add(i,new String[] { object.getString("id_tipo_usuario").toString(), object.getString("nombre_usuario").toString()});
                            // Toast.makeText(getApplicationContext(), "oooo " + object.getString("id_tipo_usuario").toString(), Toast.LENGTH_SHORT).show();
                            //rows.add(new String[]{"1","San Juan"});
                            // Toast.makeText(getApplicationContext(), "Filas: " + rows.get(0).toString(), Toast.LENGTH_SHORT).show();

                        }


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

        return array;
    }
*/

}
