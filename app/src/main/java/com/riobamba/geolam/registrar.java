package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import java.util.HashMap;
import java.util.Map;

public class registrar extends AppCompatActivity {
    EditText txtName,txtEmail,pass,txtApe,txtEdad,txtSexo;
    Button btn_insert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        txtEmail    = findViewById(R.id.etemail);
        txtName     = findViewById(R.id.ednombre);
        txtApe    = findViewById(R.id.edapellido);
        txtEdad     = findViewById(R.id.ededad);
        txtSexo    = findViewById(R.id.edsexo);
        pass = findViewById(R.id.etcontraseña);
        btn_insert = findViewById(R.id.btn_register);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertarUsusario();
            }
        });
    }
    private void insertarUsusario(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://qcqjfcit.lucusvirtual.es/insertar.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operación Exitosa", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("email",txtEmail.getText().toString());
                parametros.put("nombre_usuario",txtName.getText().toString());
                parametros.put("apellido_usuario",txtApe.getText().toString());
                parametros.put("edad",txtEdad.getText().toString());
                parametros.put("sexo",txtSexo.getText().toString());
                parametros.put("contrasenia",pass.getText().toString());

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

    public  void  login(View v){
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}