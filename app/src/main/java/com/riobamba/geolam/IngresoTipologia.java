package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.android.material.textfield.TextInputLayout;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IngresoTipologia extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    EditText txtTipologia;
    TextInputLayout errorTipologia;
    Button btnAgregar, btnTipologiaAgregada,btnCancelar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_tipologia);

        txtTipologia = findViewById(R.id.etTipologia);
        btnAgregar = findViewById(R.id.btnAgregarTipologia);
        btnTipologiaAgregada = findViewById(R.id.btnTipologiaAgregada);
        errorTipologia=findViewById(R.id.txTipo);
        btnCancelar=findViewById(R.id.btnCancelarTipo);

        toolbar.show(this, "Gestión de lugares", false); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validarTipologia();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtTipologia.getText().toString().equals(""))
                {
                    finish();
                }
                else{
                    int icon  = R.drawable.peligro;
                    AlertDialog.Builder builder = new AlertDialog.Builder(IngresoTipologia.this);
                    builder.setIcon(icon)
                            .setTitle("Cancelar")
                            .setMessage("Se perderán todos los cambios realizados ¿Desea continuar?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }

            }
        });

        btnTipologiaAgregada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngresoTipologia.this, Tipologia.class);
                startActivity(intent);
            }
        });

    }
    private void validarTipologia(){
        if(validarCamposVacios()==1) {
            String url = WebService.urlRaiz + WebService.servicioValidarExistenciaTipologia;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response ->
                    {

                        try {

                            JSONObject object = new JSONObject( URLDecoder.decode( response, "UTF-8" ));
                            String existencia = object.getString("valida");

                            if (existencia.equals("existe")) {

                               txtTipologia.setError("¡Esta tipología ya existe!");
                                txtTipologia.requestFocus();

                            } else {

                                    insertarTipologia();


                            }
                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    },error ->{
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();

            })


            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("descripcion_tipo_lugar",txtTipologia.getText().toString().toUpperCase().trim());

                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }
    private int validarCamposVacios() {
       // errorTipologia.setError(null);
        int camposVacios=0;
        if(txtTipologia.getText().toString().length()<=80) {
            if (txtTipologia.getText().toString().equals("")) {
                txtTipologia.setError("¡Ingrese una Tipología!");
                txtTipologia.requestFocus();
            } else if (Pattern.compile(" {2,}").matcher(txtTipologia.getText().toString()).find()) {
                txtTipologia.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtTipologia.requestFocus();
            } else if(txtTipologia.getText().toString().length()<5 && txtTipologia.getText().toString().length()>0)
            {
                txtTipologia.setError("Nombre demasiado corto. (Mínimo 5 caracteres)");
                txtTipologia.requestFocus();
            }else {
                camposVacios = 1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Tipología", Toast.LENGTH_SHORT).show();
            txtTipologia.setError("Nombre demasiado largo. (Máximo 80 caracteres)");
            txtTipologia.requestFocus();
        }
        return camposVacios;
    }


    private void insertarTipologia() {
        String url = WebService.urlRaiz + WebService.servicioAgregarTipologia;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Se ha agregado con éxito", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(IngresoTipologia.this, IngresoTipologia.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("descripcion_tipo_lugar",txtTipologia.getText().toString().toUpperCase().trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

    @Override
    public void onBackPressed() {}


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