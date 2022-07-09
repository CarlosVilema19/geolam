package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RecuperacionContrasenia extends AppCompatActivity {
EditText txtEmailUsuario;
    String email;
Button btnEnviarEmail;
ProgressDialog progressdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacion_contrasenia);
        txtEmailUsuario=findViewById(R.id.etRecuperacion);
        btnEnviarEmail=findViewById(R.id.btnEnviarEmail);
        progressdialog=new ProgressDialog(this);
        progressdialog.setMessage("Cargando...");
        progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        btnEnviarEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressdialog.show();
             email = txtEmailUsuario.getText().toString().trim();
                if(email.isEmpty())
                {
                    progressdialog.dismiss();
                    txtEmailUsuario.setError("Ingrese un correo electrónico");
                    txtEmailUsuario.requestFocus();

                }
                else{
                    String url = WebService.urlRaiz+WebService.servicioOlvideLaContrasenia;

                    StringRequest stringRequest= new StringRequest(Request.Method.POST, url,
                            //new Response.Listener<String>() {
                                //@Override
                                //public void onResponse(String response) {
                            response ->
                            {
                                    try {

                                        // parser_obj = new JSONParser();
                                       //JSONArray array= (JSONArray) parser_obj.parse(response);
                                       //JSONArray array = new JSONArray(response);
                                       // JSONObject object =array.toJSONObject(array);
                                        JSONObject object = new JSONObject( URLDecoder.decode( response, "UTF-8" ));
                                        String mail=object.getString("mail");

                                        if(mail.equals("send"))
                                        {
                                            progressdialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Se ha enviado un correo de recuperación", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            progressdialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Correo inválido", Toast.LENGTH_SHORT).show();
                                        }

                                    }catch (JSONException | UnsupportedEncodingException e){
                                       // e.printStackTrace();
                                        progressdialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Correo inválido", Toast.LENGTH_SHORT).show();
                                    }
                                }, error -> {
                        //Toast.makeText(RecuperacionContrasenia.this, "Error -->" + error.toString(), Toast.LENGTH_SHORT).show();


                    //new Response.ErrorListener() {

                       // @Override
                        //public void onErrorResponse(VolleyError error) {
                          //  Toast.makeText(getApplicationContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
                        //}
                    })

                    {
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError{
                            Map<String,String> enviarParametros=new HashMap<String, String>();
                            enviarParametros.put("email",email);
                            return  enviarParametros;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(RecuperacionContrasenia.this);
                    requestQueue.add(stringRequest);
                }

            }
        });
    }
}