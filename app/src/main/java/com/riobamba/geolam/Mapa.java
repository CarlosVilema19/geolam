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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ConexionMapa;
import com.riobamba.geolam.modelo.ListadoMapa;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapa extends AppCompatActivity {
    //Declarar la lista y el recycler view
    List<ListadoMapa> mapaList;
    ConexionMapa adaptador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        //llamar al mostrar resultado
        MostrarResultado();
    }

    public void MostrarResultado()
    {
        mapaList = new ArrayList<>();

        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioListarLugaresMapa;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                mapaList.add(new ListadoMapa(
                                        obj.getDouble("latitud"),
                                        obj.getDouble("longitud"),
                                        obj.getString("nombre_lugar"),
                                        obj.getString("direccion")
                                ));
                            }
                            //new ConexionMapa(Mapa.this,mapaList);
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
}