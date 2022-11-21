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
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PostProcessor;
import android.net.ConnectivityManager;
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
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.ListadoMedicoAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListadoEspecialidad extends AppCompatActivity implements SearchView.OnQueryTextListener{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    //Declarar la lista y el recycler view
    List<ListadoLugarAdmin> lugarList;
    RecyclerView recyclerView;
    SearchView txtBuscar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    ListadoEspecialidadAdaptador myadapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_items);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lugarList = new ArrayList<>();
        //llamar al mostrar resultado
        toolbar.show(this, "Especialidades", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        txtBuscar = findViewById(R.id.svBuscar);

        myadapter = new ListadoEspecialidadAdaptador(ListadoEspecialidad.this, lugarList,this::moveToDescription);
        txtBuscar.setOnQueryTextListener(this);

        MostrarResultado();
    }

    public void MostrarResultado()
    {
        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioListarEspecialidad;

        //asignar el id_lugar guardado
        SharedPreferences preferences = getSharedPreferences("id_lugar_med", Context.MODE_PRIVATE);
        String id_lugar = preferences.getString("estado_id","");

        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if (array.length() == 0 ){
                                finish();
                                Toast.makeText(ListadoEspecialidad.this, "Todavía no hay información para este campo", Toast.LENGTH_SHORT).show();
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                lugarList.add(new ListadoLugarAdmin(
                                        obj.getString("descripcion_especialidad"),
                                        obj.getInt("id_especialidad"),
                                        obj.getString("imagen")
                                ));
                            }
                             myadapter = new ListadoEspecialidadAdaptador(ListadoEspecialidad.this, lugarList,
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
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id_lugar", id_lugar);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public void moveToDescription(ListadoLugarAdmin item)// Método para llamar a una pantalla presionanado sobre el item
    {

    }

    //Metodos para la barra inferior
    public void moverInicio(View view) //dirige al Inicio
    {
        toolbar.getActividad(this,this);
        toolbar.retornarInicio();
    }
    public void moverMapa(View view)    //dirige al mapa
    {
        toolbar.getActividad(this,this);
        toolbar.retornarMapa();
    }
    public void moverEspe(View view)    //dirige a la especialidad
    {
        toolbar.getActividad(this,this);
        toolbar.retornarEspecialidad();
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