package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.BuscarEspecialidadesLMAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Buscar_Especialidades extends AppCompatActivity {
          BuscarEspecialidadesLMAdaptador adaptador;
          AutoCompleteTextView actv;
    //BuscarEspecialidades_LM espeLug;
    // private BuscarEspecialidades_LM espeLug=new BuscarEspecialidades_LM(null);
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_especialidades);
        adaptador = new BuscarEspecialidadesLMAdaptador(this);

        toolbar.show(this, "Búsqueda Avanzada", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)



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

    //Funcion para rellenar el menu contextual en la parte superior -- proviene de la clase Toolbar
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

    //Metodos para la barra inferior
    public void moverInicio(View view) //dirige al Inicio
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarInicio());
    }
    public void moverMapa(View view)    //dirige al mapa
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarMapa());
    }
    public void moverEspe(View view)    //dirige a la especialidad
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarEspecialidad());
    }

}