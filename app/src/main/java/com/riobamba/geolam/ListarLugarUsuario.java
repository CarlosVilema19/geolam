package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.riobamba.geolam.modelo.ListadoLugarUsuario;
import com.riobamba.geolam.modelo.ListadoLugarUsuarioAdaptador;
import com.riobamba.geolam.modelo.ListadoUsuariosAdmin;
import com.riobamba.geolam.modelo.ListadoUsuariosAdminAdaptador;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarException;

public class ListarLugarUsuario extends AppCompatActivity
{

    List<ListadoLugarUsuario> lugarList;
    RecyclerView recyclerView;
    Integer item;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recyclerView = findViewById(R.id.rvUsuario);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lugarList = new ArrayList<>();
        ListadoLugar listadoLugar = (ListadoLugar) getIntent().getSerializableExtra("ListadoLugar");
        MostrarResultado(listadoLugar);


    }


    public void MostrarResultado(ListadoLugar listadoLugar)
    {
        String idLugar = listadoLugar.getId().toString();
        String url2 = WebService.urlRaiz+WebService.servicioListarLugaresUsuario; //URL del web service

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        lugarList.add(new ListadoLugarUsuario(
                                obj.getString("nombre_lugar"),
                                obj.getString("direccion"),
                                obj.getString("telefono"),
                                obj.getString("imagen_lugar"),
                                obj.getString("informacion"),
                                obj.getString("categoria"),
                                obj.getString("tipologia")
                        ));

                    }

                    ListadoLugarUsuarioAdaptador myadapter = new ListadoLugarUsuarioAdaptador(ListarLugarUsuario.this, lugarList, new ListadoLugarUsuarioAdaptador.OnItemClickListener() {
                        @Override
                        public void onItemClick(ListadoLugarUsuario item) {
                            moveToMedico(item);
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
                parametros.put("id_lugar", idLugar/*item.toString()*/);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void moveToMedico(ListadoLugarUsuario item)
    {
        Intent intent = new Intent(this,MedicoListado.class);
        intent.putExtra("ListadoLugar",item);
        startActivity(intent);}

}
