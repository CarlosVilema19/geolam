package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.riobamba.geolam.modelo.ListadoLugar;
import com.riobamba.geolam.modelo.ListadoLugarAdaptador;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.ListadoLugarUsuario;
import com.riobamba.geolam.modelo.ListadoLugarUsuarioAdaptador;
import com.riobamba.geolam.modelo.ListadoMapa;
import com.riobamba.geolam.modelo.LugarMapaAdaptador;
import com.riobamba.geolam.modelo.Proceso;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarException;

public class MapaLugarCerca extends AppCompatActivity implements  SearchView.OnQueryTextListener {

    List<ListadoLugar> lugarList;
    RecyclerView recyclerView;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    SearchView txtBuscar;
    LugarMapaAdaptador myadapter;
    String ruta;
    String urlImagenLugar;
    String urlSinEspacios;
    Proceso proceso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_items);
        SharedPreferences preferences = getSharedPreferences("tituloRefe", Context.MODE_PRIVATE);
        String text = preferences.getString("tituloRefe","");

        String[] listadoLugar = (String[]) getIntent().getSerializableExtra("MapaLugarCerca");

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtBuscar = findViewById(R.id.svBuscar);
        lugarList = new ArrayList<>();
        txtBuscar.setOnQueryTextListener(this);
        toolbar.show(this, text, true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        MostrarResultado(listadoLugar);
    }
    public void MostrarResultado(String[] listadoLugar)
    {
        String url = WebService.urlRaiz + WebService.servicioListarLugares;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        //Proceso para ordenar los lugares de acuerdo a la distancia
                        proceso = new Proceso();
                        Integer [] numOrden = new Integer[array.length()];
                        for(int k = 0 ; k< array.length() ;k++ )
                        {
                            numOrden[k] = k;
                        }
                        proceso.ordenarDistancia(listadoLugar, numOrden);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(numOrden[i]);
                            lugarList.add(new ListadoLugar(
                                    obj.getString("nombre_lugar"),
                                    obj.getString("direccion"),
                                    obj.getString("telefono"),
                                    imagenReturn(obj.getString("imagen_lugar")),
                                    obj.getInt("id_lugar"),
                                    listadoLugar[i] + " Km",
                                    obj.getString("descripcion_categoria")
                            ));
                        }

                        myadapter = new LugarMapaAdaptador(MapaLugarCerca.this, lugarList, this::moveToDescription);
                        recyclerView.setAdapter(myadapter);


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);

    }
    public void moveToDescription(ListadoLugar item)
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Cargando...", "Espere por favor");
        Intent intent = new Intent(MapaLugarCerca.this,ListarLugarUsuario.class);
        intent.putExtra("ListadoLugar",item);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                loading.dismiss();
            }
        },1200);

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
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        myadapter.filtrado(newText);
        return false;
    }

    //Obtener la url real
    private String imagenReturn(String url) {
        if(url.contains(WebService.imagenRaiz)) {
            urlSinEspacios = url.replace(" ", "%20");
            String data = urlSinEspacios;
            String[] split = data.split(WebService.imagenRaiz);
            ruta = null;
            for (int i = 0; i < split.length; i++) {
                ruta = split[1];
            }
            urlImagenLugar= WebService.urlRaiz+ruta;
        }
        else{
            urlImagenLugar=urlSinEspacios;
        }
        return  urlImagenLugar;
    }
}