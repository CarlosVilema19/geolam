package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.riobamba.geolam.modelo.DatosOpinion;
import com.riobamba.geolam.modelo.ListadoLugar;
import com.riobamba.geolam.modelo.ListadoLugarUsuario;
import com.riobamba.geolam.modelo.WebService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IngresoOpinion extends AppCompatActivity {
    EditText ingresarOpinion;
    Button btnEnviarOpinion;
    RatingBar calficacionLugar;
    String email;
    DatosOpinion datosOpinion;
    Login log;


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

        btnEnviarOpinion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertarOpinion(listadoLugarUsuario);
            }
        });


    }

    private void insertarOpinion(ListadoLugarUsuario listadoLugarUsuario) {

        String idLugar = listadoLugarUsuario.getIdLugar().toString();
        String email = "carlos.vilema21@gmail.com";
        String url = WebService.urlRaiz + WebService.servicioInsertarOpinion;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Se ha enviado con éxito", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
