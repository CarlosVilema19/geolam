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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoEspecialidadAdaptador;
import com.riobamba.geolam.modelo.ListadoLugar;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.ListadoMedico;
import com.riobamba.geolam.modelo.ListadoMedicoAdaptador;
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

public class MedicoBusquedaList extends AppCompatActivity implements SearchView.OnQueryTextListener{

    List<ListadoMedico> lugarList;
    RecyclerView recyclerView;
    Toolbar toolbar = new Toolbar();
    SearchView txtBuscar;
    ListadoMedicoAdaptador myadapter;
    LinearLayout referencia;
    TextView textoReferencia, tituRefeBus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_items);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lugarList = new ArrayList<>();


        txtBuscar = findViewById(R.id.svBuscar);
        myadapter = new ListadoMedicoAdaptador(MedicoBusquedaList.this, lugarList);
        txtBuscar.setOnQueryTextListener(this);

        toolbar.show(this, "Lugares", true);

        //LugarBusqueda lugarBusqueda = (LugarBusqueda) getIntent().getSerializableExtra("LugarBusqueda");

        referencia = findViewById(R.id.llTituRefe);
        textoReferencia = findViewById(R.id.tvTituRefe);
        referencia.setVisibility(View.VISIBLE);
        tituRefeBus = findViewById(R.id.tvTituRefeBus);
        String titu = "Buscar por: ";
        tituRefeBus.setText(titu);

        MostrarResultado();
    }


    public void MostrarResultado()
    {

        SharedPreferences preferences1 = getSharedPreferences("lugar", Context.MODE_PRIVATE);
        SharedPreferences preferences2 = getSharedPreferences("espeMed", Context.MODE_PRIVATE);

        String lugar = preferences1.getString("lugar","");
        String espe = preferences2.getString("espeMed","");

        String lug1, espe1;
        if(lugar.equals("")) {lug1 = "";}else if(espe.equals("")) {lug1 = lugar;}else{lug1 = lugar +" - ";}
        if(espe.equals("")) {espe1 = "";}else {espe1 = espe;}

        String text = lug1 + espe1;
        textoReferencia.setText(text);

        String url = WebService.urlRaiz + WebService.servicioBusquedaMedico;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            lugarList.add(new ListadoMedico(
                                    obj.getString("nombre_medico"),
                                    obj.getString("especialidad"),
                                    obj.getString("descripcion_medico"),
                                    obj.getInt("id_medico"),
                                    obj.getString("lugar_trabaja")
                            ));
                        }
                        myadapter = new ListadoMedicoAdaptador(MedicoBusquedaList.this, lugarList);
                        recyclerView.setAdapter(myadapter);


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nombre_lugar",lugar.trim() );
                parametros.put("especialidad",espe.trim());
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

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
}

