package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.ListadoLugarAdminAdaptador;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tipologia extends AppCompatActivity implements SearchView.OnQueryTextListener {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    //Declarar la lista y el recycler view
    List<ListadoLugarAdmin> lugarList;
    RecyclerView recyclerView;
    SearchView txtBuscar;
    ListadoLugarAdminAdaptador adaptador;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    ListadoLugarAdminAdaptador myadapter;
    Integer tipo = 3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_admin);
        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lugarList = new ArrayList<>();
        //llamar al mostrar resultado
        toolbar.show(this, "Gestión de lugares", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)
        txtBuscar = findViewById(R.id.svBuscar);
        txtBuscar.setOnQueryTextListener(this);
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
                                        obj.getInt("id_tipologia_lugar"),
                                        ""
                                ));
                            }
                            myadapter = new ListadoLugarAdminAdaptador(Tipologia.this, lugarList,
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
                            }, tipo);
                            recyclerView.setAdapter(myadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);

    }
    public void moveToDescription(ListadoLugarAdmin item)// Método para llamar a una pantalla presionanado sobre el item
    {
    }
    public void moveToActualizar(ListadoLugarAdmin item)// Método para llamar a una pantalla presionanado sobre el item
    {
        finish();
        Intent intent = new Intent(this,ActualizarTipologia.class);
        intent.putExtra("ActualizarTipo",item);
        startActivity(intent);
    }

    public void moveToEliminar(ListadoLugarAdmin button) //Método para eliminar presionando sobre un botón
    {
        String idLugar = button.getId().toString();
        String url2 = WebService.urlRaiz+WebService.servicioEliminarTipologia; //URL del web service

        final ProgressDialog loading = ProgressDialog.show(Tipologia.this, "Eliminando...", "Espere por favor");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Oculta el progress dialog de confirmacion
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Tipologia.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Tipologia.this);
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
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        myadapter.filtrado(newText);
        return true;
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