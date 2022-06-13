package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IngresoMedico extends AppCompatActivity {
    EditText txtNombreMedico,txtApellidoMedico,txtDescripcionMedico;
    Button btnAgregarMedico,btnMostrarAgregado;
    String[] items = {"Hospital San Juan", "Hospital General Clínica Metropolitana"};
    String[] items2 = {"Cardiología", "Neurología"};

    //Items Lugar y especialidad
    AutoCompleteTextView autoTxtLugar, autoTxtEspecialidad;
    ArrayAdapter<String> adapterItems, adapterItems2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_medico);
        txtNombreMedico = findViewById(R.id.ednombreMedico);
        txtApellidoMedico = findViewById(R.id.edapellidoMedico);
        txtDescripcionMedico = findViewById(R.id.eddescripcionMedico);
        autoTxtEspecialidad = findViewById(R.id.edEspecialidad);
        autoTxtLugar = findViewById(R.id.edLugarMedico);

        btnAgregarMedico = findViewById(R.id.btnAgregarMedico);

        btnAgregarMedico.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertarUsusario();
            }
        });


        //Items Lugar Autocomplete
        adapterItems = new ArrayAdapter<String>(this, R.layout.lista_items, items);
        autoTxtLugar.setAdapter(adapterItems);
        autoTxtLugar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();

            }

        });

        //Items Especilidad Autocomplete
        adapterItems2 = new ArrayAdapter<String>(this, R.layout.lista_items, items2);
        autoTxtEspecialidad.setAdapter(adapterItems2);
        autoTxtEspecialidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void insertarUsusario() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tvcpdudx.lucusvirtual.es/agregar_medico.php", new Response.Listener<String>() {
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
