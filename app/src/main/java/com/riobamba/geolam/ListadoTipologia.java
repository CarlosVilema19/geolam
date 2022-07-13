package com.riobamba.geolam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.riobamba.geolam.modelo.ListadoLugarAdminAdaptador;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListadoTipologia extends AppCompatActivity {
    //Declarar la lista y el recycler view
    List<ListadoLugarAdmin> lugarList;
    RecyclerView recyclerView;
    ListadoLugarAdminAdaptador adaptador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_items);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lugarList = new ArrayList<>();
        //llamar al mostrar resultado
        MostrarResultado();
    }

    public void MostrarResultado()
    {
        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioListarTipologiaAdmin;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                lugarList.add(new ListadoLugarAdmin(
                                        obj.getString("descripcion_tipo_lugar"),
                                        obj.getInt("id_tipologia_lugar")
                                ));
                            }
                            ListadoLugarAdminAdaptador myadapter = new ListadoLugarAdminAdaptador(ListadoTipologia.this, lugarList,
                                    new ListadoLugarAdminAdaptador.OnItemClickListener() {
                                        @Override//llamada al método para llamar a una pantalla cuando se presiona sobre el item
                                        public void onItemClick(ListadoLugarAdmin item) {moveToDescription(item);}
                                    }, new ListadoLugarAdminAdaptador.OnClickListener() {

                                @Override//llamada al método para borrar presionando sobre el botón
                                public void onClick(ListadoLugarAdmin item) {
                                    mensajeConfirmacion(item);
                                }
                            }, new ListadoLugarAdminAdaptador.OnClickActListener() {

                                @Override//llamada al método para borrar presionando sobre el botón
                                public void onClick(ListadoLugarAdmin item) {
                                    moveToActualizar(item);
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
        });

        Volley.newRequestQueue(this).add(stringRequest);

    }
    public void moveToDescription(ListadoLugarAdmin item)// Método para llamar a una pantalla presionanado sobre el item
    {
    }
    public void moveToActualizar(ListadoLugarAdmin item)// Método para llamar a una pantalla presionanado sobre el item
    {
        Intent intent = new Intent(this,actualizar_lugar_medico.class);
        intent.putExtra("ListadoLugarAdmin",item);
        startActivity(intent);
    }

    public void moveToEliminar(ListadoLugarAdmin button) //Método para eliminar presionando sobre un botón
    {
        String idLugar = button.getId().toString();
        String url2 = WebService.urlRaiz+WebService.servicioEliminarTipologia; //URL del web service

        final ProgressDialog loading = ProgressDialog.show(ListadoTipologia.this, "Eliminando...", "Espere por favor");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Oculta el progress dialog de confirmacion
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ListadoTipologia.class));
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
                parametros.put("id_tipologia_lugar", idLugar);
                loading.dismiss();
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void mensajeConfirmacion(ListadoLugarAdmin item) { //Método para confirmar la eliminación
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ListadoTipologia.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("Se eliminaran todos los lugares pertenecientes a esta tipología ¿Desea Continuar?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                moveToEliminar(item);
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }
}