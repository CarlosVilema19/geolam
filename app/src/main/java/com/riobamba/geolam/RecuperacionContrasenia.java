package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecuperacionContrasenia extends AppCompatActivity  {
    EditText txtEmailUsuario;
    String email;
    Button btnEnviarEmail;
    ProgressDialog progressdialog;

    int validarCorreo = 0;
    int validarCorreo2 = 0;
    TextInputLayout errorEmail;

    String usuario2;
    String admin2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacion_contrasenia);
        // setContentView(R.layout.activity_recuperacion_contrasenia);
        Intent recibirUsuario = getIntent();
        usuario2 = recibirUsuario.getStringExtra("usuario");

        Intent recibirAdmin = getIntent();
        admin2 = recibirAdmin.getStringExtra("admin");
        //AlertDialog dialog = builder.create();


        txtEmailUsuario = findViewById(R.id.etRecuperacion);
        btnEnviarEmail = findViewById(R.id.btnEnviarEmail);
        errorEmail = findViewById(R.id.txrecuperar);
        progressdialog = new ProgressDialog(this);


        progressdialog.setMessage("Cargando...");
        progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);




        btnEnviarEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                errorEmail.setErrorIconDrawable(null);
                errorEmail.setError(null);
                email = txtEmailUsuario.getText().toString().trim();
                progressdialog.show();

                if (email.isEmpty()) {

                    progressdialog.dismiss();
                    //txtEmailUsuario.setError("Ingrese un correo electrónico");
                    //txtEmailUsuario.requestFocus();
                    errorEmail.setError("Ingrese un correo electrónico");
                    errorEmail.requestFocus();
                    errorEmail.setErrorIconDrawable(null);

                } else {
                    progressdialog.dismiss();
                    //Toast.makeText(getApplicationContext(), admin2+usuario2, Toast.LENGTH_SHORT).show();
                    if (admin2 != null) {
                        validarCorreoBDAdmin();
                        // progressdialog.dismiss();
                        //Toast.makeText(getApplicationContext(), "éxito ad", Toast.LENGTH_SHORT).show();

                    } else {


                        if (usuario2 != null) {
                            validarCorreoBDUsuario();
                            // progressdialog.dismiss();
                            //restablecerContraseniaUsuario();
                            //Toast.makeText(getApplicationContext(), "éxito", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void validarCorreoBDUsuario() {
        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url2 = WebService.urlRaiz + WebService.servicioExistenciaCorreoRCUsuario;
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,

                response ->
                {
                    try {

                        JSONObject object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                        String existencia = object.getString("valida");
                        //Toast.makeText(getApplicationContext(), existencia, Toast.LENGTH_SHORT).show();
                        if (existencia.equals("existe")) {
                            //Toast.makeText(getApplicationContext(), "Existe", Toast.LENGTH_SHORT).show();
                            // validarCorreo2=1;
                            restablecerContraseniaUsuario();

                        } else {
                            if (existencia.equals("admin")) {
                                //Toast.makeText(getApplicationContext(), "¡Cuenta de administrador!", Toast.LENGTH_SHORT).show();
                                errorEmail.setError("¡Cuenta de administrador!");
                                errorEmail.requestFocus();


                            } else {

                                if (existencia.equals("no_hay_registro")) {
                                    errorEmail.setError("¡No existen coincidencias!");
                                    errorEmail.requestFocus();
                                    // Toast.makeText(getApplicationContext(), "Usuario no registrado", Toast.LENGTH_SHORT).show();

                                }


                            }

                        }
                        //  }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, error -> {
            Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show();

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> enviarParametros2 = new HashMap<String, String>();
                enviarParametros2.put("email", txtEmailUsuario.getText().toString());
                return enviarParametros2;
            }
        };

        stringRequest2.setTag("REQUEST");
        queue2.add(stringRequest2);
        // return validarCorreo2;
    }

    private void validarCorreoBDAdmin() {
        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url2 = WebService.urlRaiz + WebService.servicioExistenciaCorreoRC;
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,

                response ->
                {
                    try {

                        JSONObject object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                        String existencia = object.getString("valida");
                        //Toast.makeText(getApplicationContext(), existencia, Toast.LENGTH_SHORT).show();
                        if (existencia.equals("existe")) {
                            //Toast.makeText(getApplicationContext(), "Existe", Toast.LENGTH_SHORT).show();
                            //validarCorreo=1;
                            restablecerContraseniaAdmin();
                        } else {
                            if (existencia.equals("usuario")) {
                                //Toast.makeText(getApplicationContext(), "¡Cuenta de usuario!", Toast.LENGTH_SHORT).show();
                                errorEmail.setError("¡Cuenta de usuario!");
                                errorEmail.requestFocus();

                            } else {

                                if (existencia.equals("no_hay_registro")) {
                                    errorEmail.setError("¡Administrador no registrado!");
                                    errorEmail.requestFocus();
                                    //Toast.makeText(getApplicationContext(), "Administrador no registrado", Toast.LENGTH_SHORT).show();

                                }


                            }

                        }
                        // }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, error -> {
            Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show();

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> enviarParametros = new HashMap<String, String>();
                enviarParametros.put("email", txtEmailUsuario.getText().toString());
                return enviarParametros;
            }
        };
        stringRequest2.setTag("REQUEST");
        queue2.add(stringRequest2);
        //return validarCorreo;
    }

    private void restablecerContraseniaAdmin() {
        progressdialog.show();
        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url = WebService.urlRaiz + WebService.servicioOlvideLaContraseniaAdmin;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                response ->
                {
                    try {

                        JSONObject object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                        String mail = object.getString("mail");

                        if (mail.equals("send")) {
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Se ha enviado un correo de recuperación", Toast.LENGTH_SHORT).show();


                        } else {
                            progressdialog.dismiss();
                            errorEmail.setError("¡Correo inválido!");
                            errorEmail.requestFocus();
                            // Toast.makeText(getApplicationContext(), "Correo inválido", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException | UnsupportedEncodingException e) {
                        // e.printStackTrace();
                        progressdialog.dismiss();
                        errorEmail.setError("¡Error en el servidor!");
                        errorEmail.requestFocus();
                        //Toast.makeText(getApplicationContext(), "Correo inválido", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> enviarParametros = new HashMap<String, String>();
                enviarParametros.put("email", txtEmailUsuario.getText().toString());
                return enviarParametros;
            }

        };

        stringRequest.setTag("REQUEST");
        queue2.add(stringRequest);
    }

    private void restablecerContraseniaUsuario() {
        progressdialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = WebService.urlRaiz + WebService.servicioOlvideLaContrasenia;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                response ->
                {
                    try {

                        JSONObject object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                        String mail = object.getString("mail");

                        if (mail.equals("send")) {
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Se ha enviado un correo de recuperación", Toast.LENGTH_SHORT).show();
                        } else {
                            progressdialog.dismiss();
                            errorEmail.setError("¡Correo inválido!");
                            errorEmail.requestFocus();
                            //Toast.makeText(getApplicationContext(), "Correo inválido", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException | UnsupportedEncodingException e) {
                        // e.printStackTrace();
                        progressdialog.dismiss();
                        errorEmail.setError("¡Correo inválido!");
                        errorEmail.requestFocus();
                        //Toast.makeText(getApplicationContext(), "Correo inválido", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> enviarParametros4 = new HashMap<String, String>();
                enviarParametros4.put("email", txtEmailUsuario.getText().toString());
                return enviarParametros4;
            }

        };

        stringRequest.setTag("REQUEST");
        requestQueue.add(stringRequest);
    }

}