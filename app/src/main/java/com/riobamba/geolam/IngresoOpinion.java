package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoLugarUsuario;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import java.util.HashMap;
import java.util.Map;

public class IngresoOpinion extends AppCompatActivity {
    EditText ingresarOpinion;
    Button btnEnviarOpinion;
    RatingBar calficacionLugar;
    String email;
    Login log;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion);

        ingresarOpinion = findViewById(R.id.etIngresarOpinion);
        btnEnviarOpinion = findViewById(R.id.btnEnviarOpinion);
        calficacionLugar = findViewById(R.id.rbCalificacion);
        ListadoLugarUsuario listadoLugarUsuario = (ListadoLugarUsuario) getIntent().getSerializableExtra("ListadoLugar");
        //DatosOpinion datosOpinion = (DatosOpinion) getIntent().getSerializableExtra("ListadoLugar");
        //ListarLugarUsuario listarLugarUsuario = (ListarLugarUsuario) getIntent().getSerializableExtra("ListadoLugar2");

        toolbar.show(this, "Geolam", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        btnEnviarOpinion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertarOpinion(listadoLugarUsuario);
            }
        });


    }

    private void insertarOpinion(ListadoLugarUsuario listadoLugarUsuario) {

        SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
        String email = preferences.getString("estado_correo","");

        String idLugar = listadoLugarUsuario.getIdLugar().toString();

        String url = WebService.urlRaiz + WebService.servicioInsertarOpinion;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Se ha enviado con éxito", Toast.LENGTH_SHORT).show();
                finish();
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
                parametros.put("id_lugar", idLugar);
                parametros.put("email", email);
                parametros.put("calificacion", String.valueOf(calficacionLugar.getRating()));
                parametros.put("comentario", ingresarOpinion.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
