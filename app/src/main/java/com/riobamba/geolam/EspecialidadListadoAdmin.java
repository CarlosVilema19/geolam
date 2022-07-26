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
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EspecialidadListadoAdmin extends AppCompatActivity implements SearchView.OnQueryTextListener{
    //Declarar la lista y el recycler view
    List<ListadoLugarAdmin> lugarList;
    RecyclerView recyclerView;
    ListadoEspecialidadAdaptador myadapter;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    SearchView txtBuscar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_admin);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lugarList = new ArrayList<>();
        txtBuscar = findViewById(R.id.svBuscar);
        txtBuscar.setOnQueryTextListener(this);
        //llamar al mostrar resultado
        toolbar.show(this, "Gestión de lugares", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)


        MostrarResultado();
    }

    public void MostrarResultado()
    {
        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioAsignarEspecialidad;

        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                lugarList.add(new ListadoLugarAdmin(
                                        obj.getString("DESCRIPCION_ESPECIALIDAD"),
                                        obj.getInt("ID_ESPECIALIDAD")
                                ));
                            }
                           myadapter = new ListadoEspecialidadAdaptador(EspecialidadListadoAdmin.this, lugarList,
                                    new ListadoEspecialidadAdaptador.OnItemClickListener() {
                                        @Override//llamada al método para llamar a una pantalla cuando se presiona sobre el item
                                        public void onItemClick(ListadoLugarAdmin item) {moveToDescription(item);}
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
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public void moveToDescription(ListadoLugarAdmin item)// Método para llamar a una pantalla presionanado sobre el item
    {

    }

    //Metodos para la barra inferior
    public void moverInicio(View view) //dirige al Inicio
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarInicio());
    }
    public void moverMapa(View view)    //dirige al mapa
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarMapa());
    }
    public void moverEspe(View view)    //dirige a la especialidad
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarEspecialidad());
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
    public boolean onQueryTextSubmit(String query) {
        myadapter.filtrado(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        myadapter.filtrado(newText);
        return true;
    }
}