package com.riobamba.geolam;

import androidx.annotation.NonNull;
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
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IngresoEspecialidad extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    EditText txtEspecialidad;
    EditText txtUrlEspecialidad;
    Button btnAgregar, btnVerAgregados,btnCancelar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_especialidad);
        txtEspecialidad = findViewById(R.id.etEspecialidad);
        btnAgregar = findViewById(R.id.btnAgregarEspecialidad);
        btnVerAgregados = findViewById(R.id.btnEspecialidadAgregada);
        txtUrlEspecialidad= findViewById(R.id.urlEspecialidad);
        btnCancelar=findViewById(R.id.btnCancelarEspe);

        toolbar.show(this, "Gestión de lugares", false); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ConexionTipologia conexionTipologia = new ConexionTipologia();
                validarEspecialidad();

            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEspecialidad.getText().toString().equals(""))
                {
                    finish();
                }
                else{
                    int icon  = R.drawable.peligro;
                    AlertDialog.Builder builder = new AlertDialog.Builder(IngresoEspecialidad.this);
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
        btnVerAgregados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngresoEspecialidad.this, EspecialidadListadoAdmin.class);
                startActivity(intent);
            }
        });
    }
    private void validarEspecialidad(){
        if(validarCamposVacios()==2) {
            String url = WebService.urlRaiz + WebService.servicioValidarExistenciaEspecialidad;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response ->
                    {
                        try {
                            JSONObject object = new JSONObject( URLDecoder.decode( response, "UTF-8" ));
                            String existencia = object.getString("valida");

                            if (existencia.equals("existe")) {
                                txtEspecialidad.setError("¡Esta especialidad ya existe!");
                                txtEspecialidad.requestFocus();
                            } else {
                                    insertarEspecialidad();
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
                    parametros.put("descripcion_especialidad",txtEspecialidad.getText().toString().toUpperCase().trim());

                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
    private int validarCamposVacios() {

        int camposVacios=0;
        if(txtEspecialidad.getText().toString().equals("") && txtUrlEspecialidad.getText().toString().equals("") )
        {
            txtEspecialidad.setError("¡Ingrese una Especialidad!");
            txtEspecialidad.requestFocus();

            txtUrlEspecialidad.setError("¡Ingrese la URL de la imagen!");
        }
        else {
            if (txtEspecialidad.getText().toString().length() <= 50) {
                if (txtEspecialidad.getText().toString().equals("")) {
                    txtEspecialidad.setError("¡Ingrese una Especialidad!");
                    txtEspecialidad.requestFocus();
                } else if (Pattern.compile(" {2,}").matcher(txtEspecialidad.getText().toString()).find()) {
                    txtEspecialidad.setError("¡Verifique que no haya más de un espacio en blanco!");
                    txtEspecialidad.requestFocus();
                } else if (txtEspecialidad.getText().toString().length() < 5 && txtEspecialidad.getText().toString().length() > 0) {
                    txtEspecialidad.setError("Nombre demasiado corto. (Mínimo 5 caracteres)");
                    txtEspecialidad.requestFocus();
                } else {
                    camposVacios = 1;
                }
            } else {
                Toast.makeText(this, "¡Error! Especialidad", Toast.LENGTH_SHORT).show();
                txtEspecialidad.setError("Nombre demasiado largo. (Máximo 50 caracteres)");
                txtEspecialidad.requestFocus();
            }

            //url
            if (camposVacios == 1) {
                if (txtUrlEspecialidad.getText().toString().length() <= 449) {
                    if (txtUrlEspecialidad.getText().toString().equals("")) {
                        txtUrlEspecialidad.setError("¡Ingrese la URL de la imagen!");
                        txtUrlEspecialidad.requestFocus();
                    } else if (Pattern.compile(" {2,}").matcher(txtUrlEspecialidad.getText().toString()).find()) {
                        txtUrlEspecialidad.setError("¡Verifique que no haya más de un espacio en blanco!");
                        txtUrlEspecialidad.requestFocus();
                    } else if (txtUrlEspecialidad.getText().toString().length() < 5 && txtUrlEspecialidad.getText().toString().length() > 0) {
                        txtUrlEspecialidad.setError("URL demasiada corta");
                        txtUrlEspecialidad.requestFocus();
                    } else {
                        if(verificarURL(txtUrlEspecialidad.getText().toString().trim())==false){

                                txtUrlEspecialidad.setError("Ingrese una URL válida");
                                txtUrlEspecialidad.requestFocus();

                        }
                        else {
                            camposVacios = 2;
                        }
                    }
                } else {
                    Toast.makeText(this, "¡Error! Imagen Especialidad", Toast.LENGTH_SHORT).show();
                    txtUrlEspecialidad.setError("URL demasiada larga. (Máximo 449 caracteres)");
                    txtUrlEspecialidad.requestFocus();
                }
            }
        }
        return camposVacios;
    }
private boolean verificarURL(String url){
        try {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e)
        {
            return  false;
        }
}
    private void insertarEspecialidad() {
        String url = WebService.urlRaiz + WebService.servicioAgregarEspecialidad;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Se ha agregado con éxito", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(IngresoEspecialidad.this, IngresoEspecialidad.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("descripcion_especialidad",txtEspecialidad.getText().toString().toUpperCase().trim());
                parametros.put("imagen", txtUrlEspecialidad.getText().toString().trim());
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