package com.riobamba.geolam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText edtUsuario, edtPassword;
    TextInputLayout errorPass, errorEmail;
    Button btnLogin, btnRecuperar, btnRegistro, btnAdmin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        errorPass = findViewById(R.id.txcontrasenia);
        errorEmail=findViewById(R.id.txusuario);
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
            Intent intent2 = new Intent(Login.this, RecuperacionContrasenia.class);
            intent2.putExtra("usuario","2");
            startActivity(intent2);

        });

        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, registrar.class);
            startActivity(intent);
        });

        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, login_admin.class);
            startActivity(intent);
        });

    }
    @Override public void onBackPressed() { }

    private void validarUsuario(){
        if(validarCamposVacios()==1) {
            String url = WebService.urlRaiz + WebService.servicioValidarUsuario;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response ->
                    {

                        try {
                            //JSONArray array = new JSONArray(response);
                            //JSONObject object = array.toJSONObject(array);
                            JSONObject object = new JSONObject( URLDecoder.decode( response, "UTF-8" ));
                            String existencia = object.getString("valida");
                            //Toast.makeText(getApplicationContext(), existencia, Toast.LENGTH_SHORT).show();
                            if (existencia.equals("existe")) {

                                Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                                guardarEstadoButton();
                                guardarEmail(edtUsuario.getText().toString());
                                // Intent intent = new Intent(Login.this,Inicio.class);
                                Intent intent = new Intent(Login.this, Listado.class);
                                startActivity(intent);
                            } else {
                                if (existencia.equals("administrador"))
                                {
                                    Toast.makeText(getApplicationContext(), "Inicie sesión como administrador", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    if(existencia.equals("error_contrasenia")){
                                        //Toast.makeText(getApplicationContext(), "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                                        errorPass.setErrorIconDrawable(null);
                                        errorPass.setError("La contraseña es incorrecta");
                                        edtPassword.requestFocus();
                                    }
                                    else{
                                        if(existencia.equals("no_hay_registro")){
                                            Toast.makeText(getApplicationContext(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                            }
                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    },error ->{


                    })


           {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("email",edtUsuario.getText().toString());
                    parametros.put("contrasenia",getSHA256(edtPassword.getText().toString()));

                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }

    private int validarCamposVacios() {
        errorEmail.setError(null);
        errorPass.setError(null);
        int camposVacios=0;
        if (!edtUsuario.getText().toString().equals("") && !edtPassword.getText().toString().equals("")) {
            camposVacios=1;
        }
        else {
            if (edtUsuario.getText().toString().equals("") && edtPassword.getText().toString().equals("")) {

                //edtUsuario.setError("Ingrese un correo electrónico");
                //edtUsuario.requestFocus();

                errorEmail.setError("Ingrese un correo electrónico");
                errorEmail.requestFocus();
                errorPass.setErrorIconDrawable(null);
                errorPass.setError("Ingrese una contraseña");
                //errorPass.requestFocus();
            } else {
                if (edtUsuario.getText().toString().equals("")) {
                    errorEmail.setError("Ingrese un correo electrónico");
                    errorEmail.requestFocus();
                } else {
                    if (edtPassword.getText().toString().equals("")) {
                        errorPass.setErrorIconDrawable(null);
                        errorPass.setError("Ingrese una contraseña");
                        errorPass.requestFocus();
                    }
                }
            }
        }
     return camposVacios;
    }


    public void guardarEstadoButton()
    {
        SharedPreferences preferences = getSharedPreferences("omitir_log",Context.MODE_PRIVATE);
        boolean estado = true;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("estado_inicio",estado);
        editor.commit();

        SharedPreferences preferences1 = getSharedPreferences("omitir_log_admin", Context.MODE_PRIVATE);
        boolean estado1 = false;
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putBoolean("estado_inicio_admin",estado1);
        editor1.commit();

    }

    public String getSHA256(String data) {
        return Hashing.sha256().hashString(data, StandardCharsets.UTF_8).toString();
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

