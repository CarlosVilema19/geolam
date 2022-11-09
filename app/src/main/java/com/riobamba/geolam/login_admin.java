package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class login_admin extends AppCompatActivity {
    EditText edtUsuario, edtPassword;
    TextInputLayout errorPass, errorEmail;
    Button btnLogin, btnRecuperarAdmin, btnUsuario;
    ProgressDialog loading;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);


        errorPass = findViewById(R.id.txcontraseniaAdmin);
        errorEmail=findViewById(R.id.txusuarioAdmin);
        edtUsuario = findViewById(R.id.edusuarioAdmin);
        edtPassword = findViewById(R.id.edcontraseniaAdmin);
        btnLogin = findViewById(R.id.btniniciosesionAdmin);
        btnRecuperarAdmin = findViewById(R.id.btnolvidarcontraseniaAdmin);
        btnUsuario=findViewById(R.id.btnUsuario);


        btnLogin.setOnClickListener(view -> {
            loading = ProgressDialog.show(login_admin.this, "Cargando...", "Espere por favor");
            validarUsuario();
        });

        btnRecuperarAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(login_admin.this, RecuperacionContrasenia.class);
            intent.putExtra("admin","1");
            startActivity(intent);
        });
        btnUsuario.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(login_admin.this, Login.class);
            startActivity(intent);
        });


    }

    @Override public void onBackPressed() { }


    private void validarUsuario(){
        if(validarCamposVacios()==1) {
            String url = WebService.urlRaiz + WebService.servicioValidarAdministrador;
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
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                                guardarEstadoButton();
                                guardarEmail(edtUsuario.getText().toString());
                                // Intent intent = new Intent(Login.this,Inicio.class);
                                Intent intent = new Intent(login_admin.this, InicioAdmin.class);
                                startActivity(intent);
                            } else {
                                loading.dismiss();
                                if (existencia.equals("usuario"))
                                {
                                    Toast.makeText(getApplicationContext(), "Ingrese las credenciales de administrador", Toast.LENGTH_SHORT).show();
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
                            loading.dismiss();
                        }
                    },error ->{
                loading.dismiss();
                Toast.makeText(login_admin.this, "Error del servidor", Toast.LENGTH_SHORT).show();
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
            loading.dismiss();
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
        SharedPreferences preferences1 = getSharedPreferences("omitir_log_admin", Context.MODE_PRIVATE);
        boolean estado1 = true;
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putBoolean("estado_inicio_admin",estado1);
        editor1.commit();

        SharedPreferences preferences = getSharedPreferences("omitir_log",Context.MODE_PRIVATE);
        boolean estado = false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("estado_inicio",estado);
        editor.commit();


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