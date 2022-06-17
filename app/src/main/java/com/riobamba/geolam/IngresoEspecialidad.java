package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.riobamba.geolam.modelo.WebService;

import java.util.HashMap;
import java.util.Map;

public class IngresoEspecialidad extends AppCompatActivity {
    EditText txtEspecialidad;
    Button btnAgregar, btnVerAgregados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_especialidad);
        txtEspecialidad = findViewById(R.id.etEspecialidad);
        btnAgregar = findViewById(R.id.btnAgregarEspecialidad);
        btnVerAgregados = findViewById(R.id.btnEspecialidadAgregada);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ConexionTipologia conexionTipologia = new ConexionTipologia();
                insertarEspecialidad();
            }
        });
        btnVerAgregados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngresoEspecialidad.this, ListadoCrud.class);
                startActivity(intent);
            }
        });
    }

    private void insertarEspecialidad() {
        String url = WebService.urlRaiz + WebService.servicioAgregarEspecialidad;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                parametros.put("descripcion_especialidad", txtEspecialidad.getText().toString());
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
}