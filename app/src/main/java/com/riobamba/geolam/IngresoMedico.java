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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoTipologia;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IngresoMedico extends AppCompatActivity {
    EditText txtNombreMedico,txtApellidoMedico,txtDescripcionMedico;
    Button btnAgregarMedico,btnMostrarAgregado;
    TextView tvIdMedico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_medico);
        txtNombreMedico = findViewById(R.id.ednombreMedico);
        txtApellidoMedico = findViewById(R.id.edapellidoMedico);
        txtDescripcionMedico = findViewById(R.id.eddescripcionMedico);

        btnAgregarMedico = findViewById(R.id.btnAgregarMedico);
        btnMostrarAgregado = findViewById(R.id.btnVerAgregados);
        tvIdMedico = (TextView) findViewById(R.id.TextViewIDMedico_);

        btnAgregarMedico.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertarMedico();
            }
        });

        btnMostrarAgregado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngresoMedico.this, MedicoListado.class);
                startActivity(intent);}
        });


        //Conexión al Servidor- Consulta Id Médico
        RequestQueue queue= Volley.newRequestQueue(this);
        String url=WebService.urlRaiz+WebService.servicioIDMedico;
        //adaptadorTipo.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url,
                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);

                            //Carga de datos
                            CharSequence p= object.getString("AUTO_INCREMENT");
                            String p2=p.toString();
                            tvIdMedico.setText(p2);

                            // String descripcionTipo=object.getString("DESCRIPCION_TIPO_LUGAR"); //jsonArray.getString();
                            // String IDTipo=object.getString("DESCRIPCION_TIPO_LUGAR");
                            //adaptadorTipo.add(descripcionTipo);




                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        stringRequest.setTag("REQUEST");
        queue.add(stringRequest);


    }

    private void insertarMedico() {
        String url = WebService.urlRaiz + WebService.servicioAgregarMedico;

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
                parametros.put("nombre_medico", txtNombreMedico.getText().toString());
                parametros.put("apellido_medico", txtApellidoMedico.getText().toString());
                parametros.put("descripcion_medico", txtDescripcionMedico.getText().toString());

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
