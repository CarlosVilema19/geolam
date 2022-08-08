package com.riobamba.geolam;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.TemplatePDF;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportesAdmin extends AppCompatActivity {
   // private  String [] header={"ID","Nombre del Lugar"
    private  String [] header={"Tipo de usuario","Nombre"
    };
    private  String shorText="Hola";
    private String longText="Nunca consideres el estudio como una obligación, sino como una oportunidad de vida";
    private  TemplatePDF templatePDF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_admin);

        templatePDF= new TemplatePDF(getApplicationContext());
        templatePDF.abrirDocumento();
        //templatePDF.addMetada("Lugares de atención médica", "Los más buscados","Geolam");
        //templatePDF.addTitles("GEOLAM","Lugares de atencipon médica","05/08/2022");
        templatePDF.addMetada("Listado de usuarios", "Administradores-Usuarios","Geolam");
        templatePDF.addTitles("GEOLAM","Administradores-Usuarios","05/08/2022");

        templatePDF.addParagraph(shorText);
        templatePDF.addParagraph(longText);
        //templatePDF.crearTabla(header,getLugares());
        templatePDF.crearTabla(header,getUsuarios());
        templatePDF.cerrarDocumento();


    }

    //getActivity()

    public  void pdfView(View view){
        templatePDF.viewPDF();

    }
    /*
    private ArrayList<String[]>getLugares(){
        ArrayList<String[]>rows=new ArrayList<>();
        rows.add(new String[]{"1","San Juan"});
        rows.add(new String[]{"2","Metro"});
        rows.add(new String[]{"3","ESPOCH"});
        return  rows;

    }
*/
    private ArrayList<String[]>getUsuarios() {
        ArrayList<String[]>rows=new ArrayList<>();

            //  text.replaceAll("^\\s*","");
            //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            String url2;

            url2 = WebService.urlRaiz + WebService.servicioReporteUsuarios;
            // url2=url2.replace(" ", "%20");

            // actv.setAdapter(adaptador);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                    response -> {

                        try {

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                // if (array.length() > 0) {
                                JSONObject object = array.getJSONObject(i);
                                //object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                               // rows.add(new String[] { object.getString("id_tipo_usuario").toString(), object.getString("nombre_usuario").toString()});
                                //rows.add(new String[]{"1","San Juan"});

                            };



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
        return rows;


    }
    }
