package com.riobamba.geolam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.WebService;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText edtUsuario, edtPassword;
    Button btnLogin, btnRecuperar, btnRegistro, btnAdmin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = findViewById(R.id.edusuario);
        edtPassword = findViewById(R.id.edcontrasenia);
        btnLogin = findViewById(R.id.btniniciosesion);
        btnRecuperar = findViewById(R.id.btnolvidarcontrasenia);
        btnRegistro = findViewById(R.id.btnRegistroUsu);
        btnAdmin = findViewById(R.id.btnAdmin);

        btnLogin.setOnClickListener(view -> {
            validarUsuario();
        });

        btnRecuperar.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, RecuperacionContrasenia.class);
            startActivity(intent);
        });

        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, registrar.class);
            startActivity(intent);
        });

        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, InicioAdmin.class);
            startActivity(intent);
        });

    }

    private void validarUsuario(){
        String url = WebService.urlRaiz+WebService.servicioValidarUsuario;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(edtUsuario.getText().toString().equals("")){
                    Toast.makeText(Login.this, "Ingrese un email", Toast.LENGTH_SHORT).show();
                }
                else if(edtPassword.getText().toString().equals("")){
                    Toast.makeText(Login.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                }
                else{if (!response.isEmpty()){
                    guardarEstadoButton();
                    guardarEmail(edtUsuario.getText().toString());
                    Intent intent = new Intent(Login.this,Inicio.class);
                        startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Email o contraseña incorrecta", Toast.LENGTH_LONG).show();
                }}

            }
        }, error -> Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", edtUsuario.getText().toString());
                parametros.put("contrasenia", edtPassword.getText().toString());

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void guardarEstadoButton()
    {
        SharedPreferences preferences = getSharedPreferences("omitir_log",Context.MODE_PRIVATE);
        boolean estado = true;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("estado_inicio",estado);
        editor.commit();

    }

    public void guardarEmail(String email)
    {
        //SharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        SharedPreferences preferences = getSharedPreferences("correo_email",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("estado_correo", email);
        editor.commit();

    }



}

