package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IngresoMedico extends AppCompatActivity {
    EditText txtNombreMedico,txtApellidoMedico,txtDescripcionMedico;
    Button btnAgregarMedico,btnMostrarAgregado,btnCancelar;
    TextView tvIdMedico;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_medico);
        txtNombreMedico = findViewById(R.id.ednombreMedico);
        txtApellidoMedico = findViewById(R.id.edapellidoMedico);
        txtDescripcionMedico = findViewById(R.id.eddescripcionMedico);
        btnCancelar=findViewById(R.id.btnCancelarMedi);

        btnAgregarMedico = findViewById(R.id.btnAgregarMedico);
        btnMostrarAgregado = findViewById(R.id.btnVerAgregados);
        tvIdMedico = (TextView) findViewById(R.id.TextViewIDMedico_);

        toolbar.show(this, "Gestión de lugares", false); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        btnAgregarMedico.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertarMedico();

            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnMostrarAgregado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngresoMedico.this, MedicoAdmin.class);
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
        if(validarMedico() == 1){
            String url = WebService.urlRaiz + WebService.servicioAgregarMedico;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "Se ha agregado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(IngresoMedico.this, IngresoMedico.class);
                    startActivity(intent);
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
                    parametros.put("nombre_medico", txtNombreMedico.getText().toString().trim().toUpperCase());
                    parametros.put("apellido_medico", txtApellidoMedico.getText().toString().trim().toUpperCase());
                    parametros.put("descripcion_medico", txtDescripcionMedico.getText().toString().trim());

                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
    private int validarNombre(){
        int datCorrecto=0;
        if(txtNombreMedico.getText().toString().length()<=30){
            if(Pattern.compile(" {2,}").matcher(txtNombreMedico.getText().toString()).find())
            {
                txtNombreMedico.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtNombreMedico.requestFocus();
            }else if(txtNombreMedico.getText().toString().equals("")){
                txtNombreMedico.setError("¡Ingrese un nombre!");
                txtNombreMedico.requestFocus();
            }else if(txtNombreMedico.getText().toString().length()<2 && txtNombreMedico.getText().toString().length()>0)
            {
                txtNombreMedico.setError("Nombre demasiado corto. (Mínimo 2 caracteres)");
                txtNombreMedico.requestFocus();
            }
            else {
                datCorrecto=1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Nombre del médico", Toast.LENGTH_SHORT).show();
            txtNombreMedico.setError("Nombre demasiado largo. (Máximo 30 caracteres)");
            txtNombreMedico.requestFocus();
        }

        return datCorrecto;
    }
    private int validarApellido(){
        int datCorrecto=0;
        if(txtApellidoMedico.getText().toString().length()<=30){
            if(Pattern.compile(" {2,}").matcher(txtApellidoMedico.getText().toString()).find())
            {
                txtApellidoMedico.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtApellidoMedico.requestFocus();
            }else if(txtApellidoMedico.getText().toString().equals("")){
                txtApellidoMedico.setError("¡Ingrese un apellido!");
                txtApellidoMedico.requestFocus();
            }else if(txtApellidoMedico.getText().toString().length()<2 && txtApellidoMedico.getText().toString().length()>0)
            {
                txtApellidoMedico.setError("Apellido demasiado corto. (Mínimo 2 caracteres)");
                txtApellidoMedico.requestFocus();
            }
            else {
                datCorrecto=1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Apellido del médico", Toast.LENGTH_SHORT).show();
            txtApellidoMedico.setError("Apellido demasiado largo. (Máximo 30 caracteres)");
            txtApellidoMedico.requestFocus();
        }

        return datCorrecto;
    }

    private int validarDescripcion(){
        int datCorrecto=0;
        if(txtDescripcionMedico.getText().toString().length()<=150){
            if(Pattern.compile(" {2,}").matcher(txtDescripcionMedico.getText().toString()).find())
            {
                txtDescripcionMedico.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtDescripcionMedico.requestFocus();
            }else if(txtDescripcionMedico.getText().toString().length()<5 && txtDescripcionMedico.getText().toString().length()>0)
            {
                txtDescripcionMedico.setError("Descripción demasiada corta. (Mínimo 5 caracteres)");
                txtDescripcionMedico.requestFocus();
            }
            else {
                datCorrecto=1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Apellido del médico", Toast.LENGTH_SHORT).show();
            txtApellidoMedico.setError("Apellido demasiado largo. (Máximo 150 caracteres)");
            txtApellidoMedico.requestFocus();
        }

        return datCorrecto;
    }




    public int validarMedico()
    {
        int valApe = validarApellido();
        int valDes = validarDescripcion();
        int valNom = validarNombre();

        if(valNom == 1 && valApe == 1 && valDes==1)
        {
            return 1;
        }
        else
            return 0;
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
    @Override
    public void onBackPressed() {}

}
