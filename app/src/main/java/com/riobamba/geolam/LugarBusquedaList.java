package com.riobamba.geolam;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
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
        import android.widget.SearchView;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.riobamba.geolam.modelo.ListadoLugar;
        import com.riobamba.geolam.modelo.ListadoLugarAdaptador;
        import com.riobamba.geolam.modelo.Toolbar;
        import com.riobamba.geolam.modelo.WebService;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class LugarBusquedaList extends AppCompatActivity implements SearchView.OnQueryTextListener{

    List<ListadoLugar> lugarList;
    RecyclerView recyclerView;
    Toolbar toolbar = new Toolbar();
    SearchView txtBuscar;
    ListadoLugarAdaptador myadapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_items);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lugarList = new ArrayList<>();


        txtBuscar = findViewById(R.id.svBuscar);
        myadapter = new ListadoLugarAdaptador(LugarBusquedaList.this, lugarList,this::moveToDescription);
        txtBuscar.setOnQueryTextListener(this);

        toolbar.show(this, "Lugares", true);

        //LugarBusqueda lugarBusqueda = (LugarBusqueda) getIntent().getSerializableExtra("LugarBusqueda");


        MostrarResultado();
    }


    public void MostrarResultado()
    {

        SharedPreferences preferences = getSharedPreferences("cate", Context.MODE_PRIVATE);
        SharedPreferences preferences1 = getSharedPreferences("tipo", Context.MODE_PRIVATE);
        SharedPreferences preferences2 = getSharedPreferences("espe", Context.MODE_PRIVATE);

        String cate = preferences.getString("cate","");
        String tipo = preferences1.getString("tipo","");
        String espe = preferences2.getString("espe","");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = WebService.urlRaiz + WebService.servicioBusquedaLugar;


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
                                    obj.getString("imagen_lugar"),
                                    obj.getInt("id_lugar"),
                                    "",
                                    obj.getString("descripcion_categoria")
                            ));
                        }
                        myadapter = new ListadoLugarAdaptador(LugarBusquedaList.this, lugarList, this::moveToDescription);
                        recyclerView.setAdapter(myadapter);


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("categoria", cate);
                parametros.put("tipologia",tipo );
                parametros.put("especialidad", espe);
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }
    public void moveToDescription(ListadoLugar item){

        final ProgressDialog loading = ProgressDialog.show(this, "Cargando...", "Espere por favor");
        Intent intent = new Intent(LugarBusquedaList.this,ListarLugarUsuario.class);
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

    //Funcion para recordar el inicio de sesion
    public void guardarEstadoButton()
    {
        SharedPreferences preferences = getSharedPreferences("omitir_log", Context.MODE_PRIVATE);
        boolean estado = false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("estado_inicio",estado);
        editor.commit();

        SharedPreferences preferences1 = getSharedPreferences("omitir_log_admin", Context.MODE_PRIVATE);
        boolean estado1 = false;
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putBoolean("estado_inicio_admin",estado1);
        editor1.commit();
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
}



