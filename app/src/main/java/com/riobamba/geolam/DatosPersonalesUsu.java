package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PostProcessor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.DatosPersonales;
import com.riobamba.geolam.modelo.DatosPersonalesAdaptador;
import com.riobamba.geolam.modelo.ListadoUsuariosAdmin;
import com.riobamba.geolam.modelo.ListadoUsuariosAdminAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatosPersonalesUsu extends AppCompatActivity {
    //Declarar la lista y el recycler view
    List<DatosPersonales> datosList;
    RecyclerView recyclerView;
    DatosPersonalesAdaptador adaptador;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_item);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datosList = new ArrayList<>();
        toolbar.show(this, "Mis datos", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)


        //llamar al mostrar resultado
        MostrarResultado();
    }

    public void MostrarResultado()
    {
        //obtener el correo del usuario logueado
        SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
        String email = preferences.getString("estado_correo","");

        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioDatosPersonales;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                datosList.add(new DatosPersonales(
                                        obj.getString("nombre_usuario"),
                                        obj.getString("apellido_usuario"),
                                        obj.getString("email"),
                                        obj.getString("imagen"),
                                        obj.getString("descripcion_tipo_usuario"),
                                        obj.getInt("id_tipo_usuario"),
                                        obj.getInt("edad")
                                ));
                            }
                            DatosPersonalesAdaptador myadapter = new DatosPersonalesAdaptador(DatosPersonalesUsu.this, datosList,
                                    new DatosPersonalesAdaptador.OnItemClickListener() {
                                        @Override//llamada al método para llamar a una pantalla cuando se presiona sobre el item
                                        public void onItemClick(DatosPersonales item) {moveToGuardar(item);}
                                    }, new DatosPersonalesAdaptador.OnClickListener() {

                                @Override//llamada al método para borrar presionando sobre el botón
                                public void onClick(DatosPersonales item) {
                                    moveToCancelar(item);
                                }
                            });
                            recyclerView.setAdapter(myadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
    public void moveToGuardar(DatosPersonales item)// Método para Guardar
    {
        Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
    }

    public void moveToCancelar(DatosPersonales item) //Método para Cancelar
    {
        finish();
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