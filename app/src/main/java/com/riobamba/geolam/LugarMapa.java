package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.ListadoLugar;
import com.riobamba.geolam.modelo.ListadoLugarAdaptador;
import com.riobamba.geolam.modelo.ListadoLugarUsuario;
import com.riobamba.geolam.modelo.ListadoLugarUsuarioAdaptador;
import com.riobamba.geolam.modelo.LugarMapaAdaptador;
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

public class LugarMapa extends AppCompatActivity implements  SearchView.OnQueryTextListener {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    List<ListadoLugar> lugarList;
    RecyclerView recyclerView;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    SearchView txtBuscar;
    LugarMapaAdaptador myadapter;
    String ruta;
    String urlImagenLugar;
    String urlSinEspacios;
    LinearLayout referencia;
    TextView textoReferencia;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_items);

       /* referencia = findViewById(R.id.llReferencia);
        textoReferencia = findViewById(R.id.tvReferencia);
        referencia.setVisibility(View.VISIBLE);*/
        SharedPreferences preferences = getSharedPreferences("tituloRefe", Context.MODE_PRIVATE);
        String text = preferences.getString("tituloRefe","");
        //textoReferencia.setText(text);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtBuscar = findViewById(R.id.svBuscar);
        lugarList = new ArrayList<>();
        txtBuscar.setOnQueryTextListener(this);
        toolbar.show(this, text, true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        MostrarResultado();
    }
    public void MostrarResultado()
    {
        /*SharedPreferences preferences = getSharedPreferences("distanciaMapa", Context.MODE_PRIVATE);
        preferences.getString("distancia_mapa","50 Km" );*/


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = WebService.urlRaiz + WebService.servicioListarLugares;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            lugarList.add(new ListadoLugar(
                                    obj.getString("nombre_lugar"),
                                    obj.getString("direccion"),
                                    obj.getString("telefono"),
                                    imagenReturn(obj.getString("imagen_lugar")),
                                    obj.getInt("id_lugar"),
                                    "",
                                    obj.getString("descripcion_categoria"),
                                    0F,
                                    0F
                            ));
                        }
                        myadapter = new LugarMapaAdaptador(LugarMapa.this, lugarList, item -> moveToDescription(item));
                        recyclerView.setAdapter(myadapter);


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);

    }
    public void moveToDescription(ListadoLugar item)
    {
        /*Intent intent = new Intent(this,ListarLugarUsuario.class);
        intent.putExtra("ListadoLugar",item);
        startActivity(intent);*/

        final ProgressDialog loading = ProgressDialog.show(this, "Cargando...", "Espere por favor");
        Intent intent = new Intent(LugarMapa.this,ListarLugarUsuario.class);
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



