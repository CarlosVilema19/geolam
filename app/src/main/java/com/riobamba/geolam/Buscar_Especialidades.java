package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.BuscarEspecialidades_LM_Adaptador;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Buscar_Especialidades extends AppCompatActivity {
          BuscarEspecialidades_LM_Adaptador adaptador;
          AutoCompleteTextView actv;
    //BuscarEspecialidades_LM espeLug;
    // private BuscarEspecialidades_LM espeLug=new BuscarEspecialidades_LM(null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_especialidades);
        adaptador = new BuscarEspecialidades_LM_Adaptador(this);



        //Autocomplete

        actv = findViewById(R.id.autoCompleteLugar);


        //Cambio del Adaptador


        //Oyente al cambio de texto
        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//actv.showDropDown();
             //actv.showDropDown();
                //s.toString().replaceAll("^\\s*","");
               // adaptador.clear();
               // actv.setAdapter(adaptador);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Peición al Servidor
                //  actv.setAdapter(null);
                String a;

                a= s.toString().replaceAll("^\\s*","");;
                Toast.makeText(getApplicationContext(),a, Toast.LENGTH_SHORT).show();
                    makeRequest(s.toString());

               // }else{
                    // Toast.makeText(getApplicationContext(),"Espacios",Toast.LENGTH_SHORT).show();
               // }


            }

            @Override
            public void afterTextChanged(Editable s) {
                // actv.showDropDown();
                // makeRequest(s.toString());
                //s.toString().replaceAll("^\\s*","");
               //adaptador.clear();
              //  actv.setAdapter(adaptador);

            }
        });


    }

    private void makeRequest(String text) {
        actv.setThreshold(1);
        text.replaceAll("\\s*$","");
        if (!text.equals("")) {
            adaptador.clear();
            //  text.replaceAll("^\\s*","");
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            String url2;

            // URLConnection jc= url2.openConnection();
            // String  url2= "https://wfycwgpk.lucusvirtual.es/Buscar_EspeLM.php?text="+text;

            //Toast.makeText(getApplicationContext(), "text: " + text, Toast.LENGTH_SHORT).show();
            // Toast.makeText(getApplicationContext(), "url2: " + url2, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "sin es: " + urlSinEspacios, Toast.LENGTH_SHORT).show();


            url2 = WebService.urlRaiz + WebService.servicioBuscarEspeLM;
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
                                    BuscarEspecialidades_LM espeLug = new BuscarEspecialidades_LM(object);

                                    adaptador.add(espeLug);
                                    actv.setAdapter(adaptador);

                               // } else {
                                 //   Toast.makeText(getApplicationContext(), "No existe coincidencias", Toast.LENGTH_SHORT).show();
                                   // adaptador.clear();

                                //}

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                Toast.makeText(this, "Error del servidor" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
            ) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> enviarParametros4 = new HashMap<String, String>();
                    enviarParametros4.put("caracteres", text);
                    return enviarParametros4;
                }

            };
            RequestQueue queue = Volley.newRequestQueue(this);
            stringRequest.setTag("REQUEST");
            queue.add(stringRequest);


        }
    }
}