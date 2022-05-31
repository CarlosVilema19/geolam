package com.riobamba.geolam.modelo;

//import static androidx.core.content.ContextCompat.Api16Impl.startActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.riobamba.geolam.Inicio;

import java.util.HashMap;
import java.util.Map;

public class ConectarLogin extends AppCompatActivity{

        EditText usuario, contrasenia;
        String URL;

    public ConectarLogin(EditText usu, EditText con, String urlR)
    {
        this.usuario = usu;
        this.contrasenia = con;
        this.URL = urlR;

    }
    public void validarUsuario(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    Intent intent = new Intent(ConectarLogin.this, Inicio.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConectarLogin.this, "Email or Password Invalid", Toast.LENGTH_LONG).show();
                }
            }
        }, error -> Toast.makeText(ConectarLogin.this, error.toString(), Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", usuario.getText().toString());
                parametros.put("contrasenia", contrasenia.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
