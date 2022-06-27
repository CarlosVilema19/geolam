package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarException;

public class Listado extends AppCompatActivity {

    List<ListadoLugar> lugarList;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_items);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lugarList = new ArrayList<>();
        //Inicio inicio = (Inicio) getIntent().getSerializableExtra("Inicio");

        MostrarResultado();
    }
    public void MostrarResultado()
    {
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
                                    obj.getString("imagen_lugar"),
                                    obj.getInt("id_lugar")
                            ));
                        }
                        ListadoLugarAdaptador myadapter = new ListadoLugarAdaptador(Listado.this, lugarList, item -> moveToDescription(item));
                        recyclerView.setAdapter(myadapter);


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);

    }
    public void moveToDescription(ListadoLugar item)
    {
        Intent intent = new Intent(this,ListarLugarUsuario.class);
        intent.putExtra("ListadoLugar",item);
       // intent.putExtra("ListadoLugar2", (Parcelable) inicio);
        startActivity(intent);
    }
}











