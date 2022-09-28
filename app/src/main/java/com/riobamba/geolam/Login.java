package com.riobamba.geolam;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
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
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Login extends AppCompatActivity {
    EditText edtUsuario, edtPassword;
    TextInputLayout errorPass, errorEmail;
    Button btnLogin, btnRecuperar, btnRegistro, btnAdmin;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
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

        //conexionInternet();

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
    /*
    private void ejecutar(){
        final Handler handler= new Handler() {
            @Override
            public void publish(LogRecord record) {
                
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                metodoEjecutar();//llamamos nuestro metodo
                handler.postDelayed(this,10000);//se ejecutara cada 10 segundos
            }
        },5000);//empezara a ejecutarse después de 5 milisegundos
    }
*/
    /*
    private void ejecutar(){
        final Handler handler= new Handler() {
            final long EXECUTION_TIME = 60000; // 1 minuto

    handler.postDelayed(new

            Runnable() {

                @Override
                public void run () {

                    ObtDatos();

                    handler.postDelayed(this, EXECUTION_TIME);
                }
            },EXECUTION_TIME);
        }*/
    /*private void conexionInternet() {
        //Internet
        Log.e("netHabilitada",Boolean.toString(isNetDisponible() ));
        String Habilitada, acceso;
        Habilitada=Boolean.toString(isNetDisponible());
        Log.e("accInternet",   Boolean.toString(isOnlineNet()));
        String Acceso=Boolean.toString(isOnlineNet());
        if( Habilitada.equals("true") && Acceso.equals("true")){
            // Intent intent2 = new Intent(Login.this, Login.class);
            //startActivity(intent2);
        }else {
            if( Habilitada.equals("true") && Acceso.equals("false"))
            {
                Intent intent2 = new Intent(Login.this, conexion_internet.class);
                startActivity(intent2);
                finish();
            }else
            {
                if( Habilitada.equals("false") && Acceso.equals("true")) {
                    Intent intent2 = new Intent(Login.this, conexion_internet.class);
                    startActivity(intent2);
                    finish();
                }
                else{
                    if ( Habilitada.equals("false") && Acceso.equals("false")){
                        Intent intent2 = new Intent(Login.this, conexion_internet.class);
                        startActivity(intent2);
                        finish();
                    }

                }
            }
        }
        // Toast.makeText(getApplicationContext(), "habili"+Boolean.toString(isNetDisponible()).toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "scc"+Boolean.toString(isOnlineNet()).toString(), Toast.LENGTH_SHORT).show();

    }
    */


   /* private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
*/
    /*
    public boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    */

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

