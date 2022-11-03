package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.ConexionMapa;
import com.riobamba.geolam.modelo.EspecialidadInicioAdaptador;
import com.riobamba.geolam.modelo.ListadoLugar;
import com.riobamba.geolam.modelo.ListadoLugarAdaptador;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.ListadoLugarUsuario;
import com.riobamba.geolam.modelo.ListadoLugarUsuarioAdaptador;
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
import java.util.jar.JarException;

public class Listado extends AppCompatActivity implements SearchView.OnQueryTextListener{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    List<ListadoLugar> lugarList;
    List<ListadoLugar> lugarList2;
    List<ListadoLugar> lugarList3;
    List<ListadoLugar> lugarList4;
    List<ListadoLugarAdmin> espeList;
    RecyclerView recyclerView, recyclerViewEspe, recyclerViewFav,recyclerViewPub,recyclerViewPri;
    CardView lugar, lugarEspe, lugarFav, lugarPub, lugarPri;
    Toolbar toolbar = new Toolbar();
    SearchView txtBuscar;
    TextView espeVerMas, vistoVerMas, favVerMas, pubVerMas, priVerMas;
    ListadoLugarAdaptador myadapter;
    EspecialidadInicioAdaptador myadapterEspe;
    String ruta;
    String urlImagenLugar;
    String urlSinEspacios;
    Integer elementosInicio = 5;
    public Button btnInicio, btnInicioPul;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_items);

        lugar = findViewById(R.id.cvListado);
        lugarEspe = findViewById(R.id.cvListadoEspe);
        lugarFav = findViewById(R.id.cvListadoFav);
        lugarPub = findViewById(R.id.cvListadoPub);
        lugarPri = findViewById(R.id.cvListadoPri);
        espeVerMas = findViewById(R.id.tvListadoEspeMas);
        vistoVerMas = findViewById(R.id.tvListadoMas);
        favVerMas = findViewById(R.id.tvListadoFavMas);
        pubVerMas = findViewById(R.id.tvListadoPubMas);
        priVerMas = findViewById(R.id.tvListadoPriMas);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        recyclerViewEspe = findViewById(R.id.rvListadoEspe);
        recyclerViewEspe.setHasFixedSize(true);
        recyclerViewEspe.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        recyclerViewFav = findViewById(R.id.rvListadoFav);
        recyclerViewFav.setHasFixedSize(true);
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        recyclerViewPub = findViewById(R.id.rvListadoPub);
        recyclerViewPub.setHasFixedSize(true);
        recyclerViewPub.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        recyclerViewPri = findViewById(R.id.rvListadoPri);
        recyclerViewPri.setHasFixedSize(true);
        recyclerViewPri.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        lugarList = new ArrayList<>();
        lugarList2 = new ArrayList<>();
        lugarList3 = new ArrayList<>();
        lugarList4 = new ArrayList<>();
        espeList = new ArrayList<>();

        txtBuscar = findViewById(R.id.svBuscar);
        myadapter = new ListadoLugarAdaptador(Listado.this, lugarList,this::moveToDescription);
        txtBuscar.setOnQueryTextListener(this);
        txtBuscar.setVisibility(View.GONE);

        toolbar.show(this, "Inicio", false);

        //Senalar el icono donde pulsa en el menu inferior
        btnInicioPul = findViewById(R.id.btnInicio);
        btnInicio = findViewById(R.id.btnInicio2);
        toolbar.obtenerBotIni(btnInicio,btnInicioPul);

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listado.this, Listado.class);
                startActivity(intent);
                finish();
            }
        });

        espeVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listado.this, EspecialidadListadoUsuario.class);
                startActivity(intent);
            }
        });
        vistoVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTitulo("Lugares más vistos");
                Intent intent = new Intent(Listado.this, LugarMapa.class);
                startActivity(intent);
            }
        });
        favVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listado.this, LugarFavorito.class);
                startActivity(intent);
            }
        });
        pubVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTitulo("Lugares públicos");
                guardarCate("1");
                Intent intent = new Intent(Listado.this, LugarPubPri.class);
                startActivity(intent);
            }
        });
        priVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTitulo("Lugares privados");
                guardarCate("2");
                Intent intent = new Intent(Listado.this, LugarPubPri.class);
                startActivity(intent);
            }
        });


        MostrarResultadoEspe();
        MostrarResultado();
        MostrarResultadoFav();
        MostrarResultadoPub();
        MostrarResultadoPri();


    }

    @Override public void onBackPressed() { }  //Anula la flecha de regreso del telefono

    @Override  // Muestra un mensaje para salir de la app
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea salir de Geolam?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }

        return super.onKeyDown(keyCode, event);
    }


    public void MostrarResultado()
    {
        String url = WebService.urlRaiz + WebService.servicioListarLugares;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        int max = Math.min(array.length(), elementosInicio);
                        for (int i = 0; i < max; i++) {
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

                       myadapter = new ListadoLugarAdaptador(Listado.this, lugarList, this::moveToDescription);
                        recyclerView.setAdapter(myadapter);



                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);

    }


    public void MostrarResultadoFav()
    {
        SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
        String email = preferences.getString("estado_correo","");

        String url = WebService.urlRaiz + WebService.servicioLugarFavoritoUsu;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        if(array.length()==0){lugarFav.setVisibility(View.GONE);}
                        else {
                            int max = Math.min(array.length(), elementosInicio);
                            for (int i = 0; i < max; i++) {
                                JSONObject obj = array.getJSONObject(i);
                                lugarList2.add(new ListadoLugar(
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

                                myadapter = new ListadoLugarAdaptador(Listado.this, lugarList2, this::moveToDescription);
                                recyclerViewFav.setAdapter(myadapter);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show()){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", email);
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void MostrarResultadoPub()
    {
        String url = WebService.urlRaiz + WebService.servicioLugarCategoria;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        if(array.length()==0){lugarPub.setVisibility(View.GONE);}
                        else {
                            int max = Math.min(array.length(), elementosInicio);
                            for (int i = 0; i < max; i++) {
                                JSONObject obj = array.getJSONObject(i);
                                lugarList3.add(new ListadoLugar(
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
                            myadapter = new ListadoLugarAdaptador(Listado.this, lugarList3, this::moveToDescription);
                            recyclerViewPub.setAdapter(myadapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show()){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("categoria", "1");
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void MostrarResultadoPri()
    {
        String url = WebService.urlRaiz + WebService.servicioLugarCategoria;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        if(array.length()==0){lugarPri.setVisibility(View.GONE);}
                        else {
                            int max = Math.min(array.length(), elementosInicio);
                            for (int i = 0; i < max; i++) {
                                JSONObject obj = array.getJSONObject(i);
                                lugarList4.add(new ListadoLugar(
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
                            myadapter = new ListadoLugarAdaptador(Listado.this, lugarList4, this::moveToDescription);
                            recyclerViewPri.setAdapter(myadapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), "Error con el servidor", Toast.LENGTH_SHORT).show()){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("categoria", "2");
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }



    public void moveToDescription(ListadoLugar item){

        final ProgressDialog loading = ProgressDialog.show(this, "Cargando...", "Espere por favor");
        Intent intent = new Intent(Listado.this,ListarLugarUsuario.class);
        intent.putExtra("ListadoLugar",item);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                loading.dismiss();
            }
        },1200);

    }



    public void MostrarResultadoEspe()
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
                                espeList.add(new ListadoLugarAdmin(
                                        obj.getString("DESCRIPCION_ESPECIALIDAD"),
                                        Integer.parseInt(obj.getString("ID_ESPECIALIDAD")),
                                        obj.getString("imagen")
                                ));
                            }
                            myadapterEspe = new EspecialidadInicioAdaptador(Listado.this, espeList,
                                    new EspecialidadInicioAdaptador.OnItemClickListener() {
                                        @Override//llamada al método para llamar a una pantalla cuando se presiona sobre el item
                                        public void onItemClick(ListadoLugarAdmin item) {moveToDescription(item);}
                                    });
                            recyclerViewEspe.setAdapter(myadapterEspe);

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
        Intent intent = new Intent(this,EspecialidadLugar.class);
        intent.putExtra("ListadoLugarAdmin",item);
        startActivity(intent);
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

    //Funcion para guardar el titulo de la actividad
   public void guardarTitulo(String titulo)
    {
        SharedPreferences preferences = getSharedPreferences("tituloRefe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tituloRefe",titulo);
        editor.apply();
    }

    //Funcion para guardar la categoria
    public void guardarCate(String cate)
    {
        SharedPreferences preferences = getSharedPreferences("categoriaLug", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("categoriaLug",cate);
        editor.apply();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        myadapter.filtrado2(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        myadapter.filtrado2(newText);
        return true;
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











