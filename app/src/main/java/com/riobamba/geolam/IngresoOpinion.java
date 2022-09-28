package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.ListadoLugar;
import com.riobamba.geolam.modelo.ListadoLugarUsuario;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import java.util.HashMap;
import java.util.Map;

public class IngresoOpinion extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    EditText ingresarOpinion;
    Button btnEnviarOpinion, btnCancelarOpinion;
    RatingBar calficacionLugar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion);

        ingresarOpinion = findViewById(R.id.etIngresarOpinion);
        btnEnviarOpinion = findViewById(R.id.btnEnviarOpinion);
        btnCancelarOpinion = findViewById(R.id.btnCancelarOpinion);
        calficacionLugar = findViewById(R.id.rbCalificacion);

        //ListadoLugarUsuario listadoLugarUsuario = (ListadoLugarUsuario) getIntent().getSerializableExtra("ListadoLugar");

        ListadoLugar listadoLugarUsuario = (ListadoLugar) getIntent().getSerializableExtra("ListadoLugar");

        toolbar.show(this, "Calificación", false); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        btnEnviarOpinion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(calficacionLugar.getRating() == 0)
                {
                    Toast.makeText(IngresoOpinion.this, "No ha ingresado una calificación", Toast.LENGTH_SHORT).show();
                }
                else{insertarOpinion(listadoLugarUsuario);}
            }
        });

        btnCancelarOpinion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelarOpinion(listadoLugarUsuario);
            }
        });


    }
    private void cancelarOpinion(ListadoLugar listadoLugarUsuario) {
        Intent intent = new Intent(IngresoOpinion.this,ListarLugarUsuario.class);
        intent.putExtra("ListadoLugar",listadoLugarUsuario);
        startActivity(intent);
        finish();
    }

    private void insertarOpinion(ListadoLugar listadoLugarUsuario) {

        SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
        String email = preferences.getString("estado_correo","");

        String idLugar = listadoLugarUsuario.getId().toString();

        String url = WebService.urlRaiz + WebService.servicioInsertarOpinion;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "Gracias por calificar", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(IngresoOpinion.this, ListarLugarUsuario.class);
                    intent.putExtra("ListadoLugar", listadoLugarUsuario);
                    startActivity(intent);
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
                parametros.put("comentario", ingresarOpinion.getText().toString().trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Metodos para la barra inferior
    public void moverInicio(View view) //dirige al Inicio
    {
        toolbar.getActividad(this,this);
        toolbar.retornarInicio();
    }
    public void moverMapa(View view)    //dirige al mapa
    {
        toolbar.getActividad(this,this);
        toolbar.retornarMapa();
    }
    public void moverEspe(View view)    //dirige a la especialidad
    {
        toolbar.getActividad(this,this);
        toolbar.retornarEspecialidad();
    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


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
    @Override
    public void onBackPressed() {}

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
