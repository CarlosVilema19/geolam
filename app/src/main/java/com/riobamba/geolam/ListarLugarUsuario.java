package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
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

public class ListarLugarUsuario extends AppCompatActivity
{

    List<ListadoLugarUsuario> lugarList;
    RecyclerView recyclerView;
    Integer item;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recyclerView = findViewById(R.id.rvUsuario);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lugarList = new ArrayList<>();
        ListadoLugar listadoLugar = (ListadoLugar) getIntent().getSerializableExtra("ListadoLugar");

        toolbar.show(this, "Geolam", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        MostrarResultado(listadoLugar);

    }


    public void MostrarResultado(ListadoLugar listadoLugar)
    {
        String idLugar = listadoLugar.getId().toString();
        String url2 = WebService.urlRaiz+WebService.servicioListarLugaresUsuario; //URL del web service

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                guardarId(idLugar);//llamado a la funcion para guaradar el id del lugar pulsado
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
                                obj.getString("tipologia"),
                                obj.getInt("id_lugar")
                        ));

                    }

                    ListadoLugarUsuarioAdaptador myadapter = new ListadoLugarUsuarioAdaptador(ListarLugarUsuario.this, lugarList, new ListadoLugarUsuarioAdaptador.OnItemClickListener() {
                        @Override
                        public void onItemClick(ListadoLugarUsuario item) {
                            moveToMedico(item);
                        }
                    },new ListadoLugarUsuarioAdaptador.OnClickEspeListener() {
                        @Override
                        public void onClick2(ListadoLugarUsuario item) {
                            moveToEspecialidad(item);

                        }
                    },new ListadoLugarUsuarioAdaptador.OnClickListener() {
                        @Override
                        public void onClick(ListadoLugarUsuario item) {
                            moveToOpinion(item);
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
                parametros.put("id_lugar", idLugar);
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

    public void moveToEspecialidad(ListadoLugarUsuario item)
    {
        Intent intent = new Intent(this,ListadoEspecialidad.class);
        intent.putExtra("ListadoLugar",item);
        startActivity(intent);}
    public void moveToOpinion(ListadoLugarUsuario item)
    {
        Intent intent = new Intent(this,IngresoOpinion.class);
        intent.putExtra("ListadoLugar",item);
        startActivity(intent);}


    public void guardarId(String id)//guardar el id pulsado para filtrar medicos y especialidades
    {
        SharedPreferences preferences = getSharedPreferences("id_lugar_med", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("estado_id", id);
        editor.commit();

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

}
