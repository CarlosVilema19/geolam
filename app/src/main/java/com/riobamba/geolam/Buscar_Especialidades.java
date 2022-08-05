package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.HashMap;
import java.util.Map;

public class Buscar_Especialidades extends AppCompatActivity {
    private BuscarEspecialidades_LM_Adaptador adaptador;
    AutoCompleteTextView actv;
    BuscarEspecialidades_LM espeLug;
    // private BuscarEspecialidades_LM espeLug=new BuscarEspecialidades_LM(null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_especialidades);
        adaptador = new BuscarEspecialidades_LM_Adaptador(this);



        //Autocomplete

        actv = findViewById(R.id.autoCompleteLugar);
        actv.setAdapter(adaptador);

        //Cambio del Adaptador
        actv.setThreshold(2);

        //Oyente al cambio de texto
        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//actv.showDropDown();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Peición al Servidor
                   //  actv.setAdapter(null);
                if (!s.toString().equals("")) {
                    makeRequest(s.toString());
                }else{
                  //  Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
               // actv.showDropDown();
               // makeRequest(s.toString());
                adaptador.clear();
            }
        });


    }

    private void makeRequest(String text) {

        String url2;



       // URLConnection jc= url2.openConnection();
        // String  url2= "https://wfycwgpk.lucusvirtual.es/Buscar_EspeLM.php?text="+text;

         //Toast.makeText(getApplicationContext(), "text: " + text, Toast.LENGTH_SHORT).show();
        // Toast.makeText(getApplicationContext(), "url2: " + url2, Toast.LENGTH_SHORT).show();
         //Toast.makeText(getApplicationContext(), "sin es: " + urlSinEspacios, Toast.LENGTH_SHORT).show();


        url2=WebService.urlRaiz+WebService.servicioBuscarEspeLM;
       // url2=url2.replace(" ", "%20");
        adaptador.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url2,
                response -> {

                    try {

                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            //object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                           espeLug = new BuscarEspecialidades_LM(object);

                            adaptador.add(espeLug);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } ,error -> {
        Toast.makeText(this,"Error del servidor"+ error.getMessage(),Toast.LENGTH_SHORT).show();

    }
                )
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> enviarParametros4 = new HashMap<String, String>();
                enviarParametros4.put("caracteres",text.toString().trim());
                return enviarParametros4;
            }

        };
        RequestQueue queue= Volley.newRequestQueue(this);
        stringRequest.setTag("REQUEST");
        queue.add(stringRequest);

    }

}