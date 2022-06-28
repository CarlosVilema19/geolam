package com.riobamba.geolam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PostProcessor;
import android.os.Bundle;
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
import com.riobamba.geolam.modelo.ListadoMedico;
import com.riobamba.geolam.modelo.ListadoMedicoAdaptador;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicoListado extends AppCompatActivity implements SearchView.OnQueryTextListener{
    //Declarar la lista y el recycler view
    List<ListadoMedico> lugarList;
    RecyclerView recyclerView;
    SearchView txtBuscar;
    ListadoMedicoAdaptador myadapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_items);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lugarList = new ArrayList<>();

        txtBuscar = findViewById(R.id.svBuscar);
        myadapter = new ListadoMedicoAdaptador(MedicoListado.this, lugarList);


        //llamar al mostrar resultado
        MostrarResultado();

        txtBuscar.setOnQueryTextListener(this);


    }

    public void MostrarResultado()
    {
        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioListarMedico;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                lugarList.add(new ListadoMedico(
                                        obj.getString("nombre_medico"),
                                        obj.getString("especialidad"),
                                        obj.getString("descripcion_medico"),
                                        obj.getInt("id_medico")
                                ));
                            }
                            ListadoMedicoAdaptador myadapter = new ListadoMedicoAdaptador(MedicoListado.this, lugarList);
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

        Volley.newRequestQueue(this).add(stringRequest);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        myadapter.filtrado(newText);
        recyclerView.setAdapter(myadapter);
        return false;
    }
}