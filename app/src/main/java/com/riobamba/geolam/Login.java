package com.riobamba.geolam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.riobamba.geolam.modelo.ConectarLogin;
import com.riobamba.geolam.modelo.WebService;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText edtUsuario, edtPassword;
    Button btnLogin, btnRecuperar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUsuario = findViewById(R.id.edusuario);
        edtPassword = findViewById(R.id.edcontrasenia);
        btnLogin = findViewById(R.id.btniniciosesion);
        btnLogin.setOnClickListener(view -> validarUsuario());
        //String URL = "https://qcqjfcit.lucusvirtual.es/validar_usuario.php";
        //ConectarLogin oConectarLogin = new ConectarLogin(edtUsuario,edtPassword, URL);
       // btnLogin.setOnClickListener(view -> oConectarLogin.validarUsuario());
        btnRecuperar = findViewById(R.id.btnolvidarcontrasenia);

        btnRecuperar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RecuperacionContrasenia.class);
                startActivity(intent);
            }
        });


    }

    private void validarUsuario(){
        String url = WebService.urlRaiz+WebService.servicioValidarUsuario;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*if (!response.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), Inicio.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Email or Password Invalid", Toast.LENGTH_LONG).show();
                }*/
                if(edtUsuario.getText().toString().equals("")){
                    Toast.makeText(Login.this, "Ingrese un email", Toast.LENGTH_SHORT).show();
                }
                else if(edtPassword.getText().toString().equals("")){
                    Toast.makeText(Login.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                }
                else{if (!response.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), Inicio.class);
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

    public void moveToRegistro(View view) {
        startActivity(new Intent(getApplicationContext(), registrar.class));
        finish();
    }

    public void moveToRegistro1(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), registrar.class));
        finish();
    }
}

