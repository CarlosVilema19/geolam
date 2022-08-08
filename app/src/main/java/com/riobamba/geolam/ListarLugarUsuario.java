package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoLugar;
import com.riobamba.geolam.modelo.ListadoLugarUsuario;
import com.riobamba.geolam.modelo.ListadoLugarUsuarioAdaptador;
import com.riobamba.geolam.modelo.ListadoOpinion;
import com.riobamba.geolam.modelo.ListadoOpinionAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarException;

public class ListarLugarUsuario extends AppCompatActivity
{

    List<ListadoLugarUsuario> lugarList;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    List<ListadoOpinion> opinionList;
    Integer item;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    LinearLayout paginaWeb;
    String nombreLugar = "Lugar de atención médica";
    String ruta;
    String urlImagenLugar;
    String urlSinEspacios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recyclerView = findViewById(R.id.rvUsuario);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView2 = findViewById(R.id.rvOpinion);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        lugarList = new ArrayList<>();
        ListadoLugar listadoLugar = (ListadoLugar) getIntent().getSerializableExtra("ListadoLugar");


        MostrarResultado(listadoLugar);

        paginaWeb = findViewById(R.id.llPaginaWeb);

        opinionList = new ArrayList<>();
        MostrarResultadoOpi(listadoLugar);
        toolbar.show(this, nombreLugar, true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)


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
                                imagenReturn(obj.getString("imagen_lugar")),
                                obj.getString("informacion"),
                                obj.getString("categoria"),
                                obj.getString("tipologia"),
                                obj.getInt("id_lugar"),
                                obj.getString("whatsapp"),
                                obj.getString("pagina_web"),
                                (float)obj.getDouble("CALIFICACION")
                        ));

                    }

                    ListadoLugarUsuarioAdaptador myadapter = new ListadoLugarUsuarioAdaptador(ListarLugarUsuario.this, lugarList, Collections.singletonList(listadoLugar), new ListadoLugarUsuarioAdaptador.OnItemClickListener() {
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
                        public void onClick(ListadoLugar item) {
                            moveToOpinion(item);
                        }
                    },new ListadoLugarUsuarioAdaptador.OnClickComenListener() {
                        @Override
                        public void onClick3(ListadoLugarUsuario item) {
                            moveToVerComentario(item);

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


    public void MostrarResultadoOpi(ListadoLugar listadoLugar)
    {
        //URL del web service
        String idLugar = listadoLugar.getId().toString();

        SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
        String email = preferences.getString("estado_correo","");

        String url = WebService.urlRaiz + WebService.servicioListarOpinion;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                opinionList.add(new ListadoOpinion(
                                        obj.getString("nombre_usuario"),
                                        obj.getString("fecha_ingreso"),
                                        obj.getString("comentario"),
                                        obj.getString("imagen"),
                                        obj.getInt("id_opinion"),
                                        (float) obj.getDouble("calificacion"),
                                        obj.getString("email")
                                ));
                            }
                            ListadoOpinionAdaptador myadapter = new ListadoOpinionAdaptador(ListarLugarUsuario.this, opinionList,
                                    new ListadoOpinionAdaptador.OnClickListener() {

                                        @Override//llamada al método para borrar presionando sobre el botón
                                        public void onClick(ListadoOpinion item) {
                                            mensajeConfirmacion(item, listadoLugar);
                                        }
                                    });
                            recyclerView2.setAdapter(myadapter);

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
                parametros.put("email", email);
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void moveToEliminar(ListadoOpinion button, ListadoLugar listadoLugar) //Método para eliminar presionando sobre un botón
    {
        String idOpinion = button.getIdOpinion().toString();
        String url2 = WebService.urlRaiz+WebService.servicioEliminarOpinion; //URL del web service

        final ProgressDialog loading = ProgressDialog.show(ListarLugarUsuario.this, "Eliminando...", "Espere por favor");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Oculta el progress dialog de confirmacion
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Se eliminó correctamente", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ListarLugarUsuario.this,ListarLugarUsuario.class);
                intent.putExtra("ListadoLugar",listadoLugar);
                startActivity(intent);
                finish();
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
                parametros.put("id_opinion", idOpinion);
                loading.dismiss();
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void mensajeConfirmacion(ListadoOpinion item, ListadoLugar listadoLugar) { //Método para confirmar la eliminación
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ListarLugarUsuario.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Desea eliminar su opinión?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                moveToEliminar(item, listadoLugar);
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
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
    public void moveToOpinion(ListadoLugar item)
    {
        Intent intent = new Intent(this,IngresoOpinion.class);
        intent.putExtra("ListadoLugar",item);
        startActivity(intent);}
    public void moveToVerComentario(ListadoLugarUsuario item)
    {
        Intent intent = new Intent(this, OpinionListado.class);
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
