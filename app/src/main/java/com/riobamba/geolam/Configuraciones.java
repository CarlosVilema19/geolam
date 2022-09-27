package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ConexionMapa;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Configuraciones extends AppCompatActivity {

    Toolbar toolbar = new Toolbar();
    Button btnMisDatos,btnContrasenia, btnEliminarCuenta,btnAcercaApp, btnTerminos;
    TextView txtFecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciones);

        toolbar.show(this, "Configuración", true);

        btnMisDatos=findViewById(R.id.btnCuenta_);
        btnContrasenia=findViewById(R.id.btnContrasenia);
        btnEliminarCuenta=findViewById(R.id.btnEliminarCuenta);
        btnAcercaApp=findViewById(R.id.btnAcercaApp);
        btnTerminos=findViewById(R.id.btnTerminosCondiciones);
        txtFecha = findViewById(R.id.tvFecha);



        btnMisDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        btnContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Configuraciones.this, CambiarContrasenia.class);
                startActivity(intent);
            }
        });
        btnEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(Configuraciones.this, EliminarCuenta.class);
              startActivity(intent);
            }
        });
        btnAcercaApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Configuraciones.this, InfoApp.class);
                startActivity(intent);
            }
        });
        btnTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Configuraciones.this, TerminosCondiciones.class);
                startActivity(intent);
            }
        });
    }

    public void obtenerFecha()
    {
        final ProgressDialog loading2 = ProgressDialog.show(this, "Obteniendo información...", "Espere por favor");
        //obtener el correo del usuario logueado
        SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
        String email = preferences.getString("estado_correo","");
        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioObtenerFechaNac;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                txtFecha.setText(obj.getString("fecha_nacimiento"));

                            }
                            String fec = txtFecha.getText().toString();
                            Intent intent = new Intent(Configuraciones.this, DatosPersonalesUsuario.class);
                            intent.putExtra("Configuraciones",fec);
                            startActivity(intent);
                            loading2.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading2.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                loading2.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", email);
                return parametros;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
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
}