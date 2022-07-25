package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class IngresoTipologia extends AppCompatActivity {

    EditText txtTipologia;
    TextInputLayout errorTipologia;
    Button btnAgregar, btnTipologiaAgregada;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_tipologia);

        txtTipologia = findViewById(R.id.etTipologia);
        btnAgregar = findViewById(R.id.btnAgregarTipologia);
        btnTipologiaAgregada = findViewById(R.id.btnTipologiaAgregada);
        errorTipologia=findViewById(R.id.txTipo);

        toolbar.show(this, "Gestión de lugares", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*ConexionTipologia conexionTipologia = new ConexionTipologia();
                conexionTipologia.insertarUsusario(txtTipologia);*/
                validarTipologia();
            }
        });

        btnTipologiaAgregada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngresoTipologia.this, ListadoTipologia.class);
                startActivity(intent);
            }
        });




    }
    private void validarTipologia(){
        if(validarCamposVacios()==1) {
            String url = WebService.urlRaiz + WebService.servicioValidarExistenciaTipologia;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response ->
                    {

                        try {

                            JSONObject object = new JSONObject( URLDecoder.decode( response, "UTF-8" ));
                            String existencia = object.getString("valida");

                            if (existencia.equals("existe")) {

                               txtTipologia.setError("¡Esta tipología ya existe!");
                                txtTipologia.requestFocus();

                            } else {

                                    insertarTipologia();


                            }
                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    },error ->{
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();

            })


            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("descripcion_tipo_lugar",txtTipologia.getText().toString().toUpperCase().trim());

                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }
    private int validarCamposVacios() {
       // errorTipologia.setError(null);
        int camposVacios=0;
        if (!txtTipologia.getText().toString().equals("")) {
            camposVacios=1;
        }
        else {
            txtTipologia.setError("¡Ingrese una Tipología!");
            txtTipologia.requestFocus();
        }
        return camposVacios;
    }


    private void insertarTipologia() {
        String url = WebService.urlRaiz + WebService.servicioAgregarTipologia;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Se ha agregado con éxito", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("descripcion_tipo_lugar",txtTipologia.getText().toString().toUpperCase().trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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


}