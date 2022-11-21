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
import android.view.View;
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
import com.riobamba.geolam.modelo.AsignacionMedico;
import com.riobamba.geolam.modelo.AsignacionMedicoAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicoAsignacion extends AppCompatActivity implements SearchView.OnQueryTextListener{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    //Declarar la lista y el recycler view
    List<AsignacionMedico> lugarList;
    RecyclerView recyclerView;

    AsignacionMedicoAdaptador myadapter;
    SearchView txtBuscar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    Integer tipo = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_admin);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lugarList = new ArrayList<>();
        txtBuscar = findViewById(R.id.svBuscar);
        txtBuscar.setOnQueryTextListener(this);
        //txtBuscar.setVisibility(View.GONE);

        //llamar al mostrar resultado
        toolbar.show(this, "Asignaciones", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        MostrarResultado();
    }

    public void MostrarResultado()
    {
        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioAsignacionMedicoTrabaja;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                lugarList.add(new AsignacionMedico(
                                        obj.getString("nombre_lugar"),
                                        obj.getString("especialidad"),
                                        obj.getString("nombre_medico"),
                                        obj.getInt("id_lugar"),
                                        obj.getInt("id_especialidad"),
                                        obj.getInt("id_medico")
                                ));
                            }
                            myadapter = new AsignacionMedicoAdaptador(MedicoAsignacion.this, lugarList,
                                    new AsignacionMedicoAdaptador.OnItemClickListener() {
                                        @Override//llamada al método para llamar a una pantalla cuando se presiona sobre el item
                                        public void onItemClick(AsignacionMedico item) {moveToDescription(item);}
                                    }, new AsignacionMedicoAdaptador.OnClickListener() {

                                @Override//llamada al método para borrar presionando sobre el botón
                                public void onClick(AsignacionMedico item) {
                                    mensajeConfirmacion(item);
                                }
                            }, new AsignacionMedicoAdaptador.OnClickActListener() {

                                @Override//llamada al método para borrar presionando sobre el botón
                                public void onClick(AsignacionMedico item) {
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
    public void moveToDescription(AsignacionMedico item)// Método para llamar a una pantalla presionanado sobre el item
    {

    }

    public void moveToActualizar(AsignacionMedico item)// Método para llamar a una pantalla presionanado sobre el item
    {

    }
    public void moveToEliminar(AsignacionMedico button) //Método para eliminar presionando sobre un botón
    {
        String idLugar = button.getIdLugar().toString();
        String idEspecialidad = button.getIdEspecialidad().toString();
        String idMedico = button.getIdMedico().toString();
        String url2 = WebService.urlRaiz+WebService.servicioEliminarAsignacionMedtrab; //URL del web service

        final ProgressDialog loading = ProgressDialog.show(MedicoAsignacion.this, "Eliminando...", "Espere por favor");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Oculta el progress dialog de confirmacion
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MedicoAsignacion.class));
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
                parametros.put("id_lugar", idLugar);
                parametros.put("id_especialidad", idEspecialidad);
                parametros.put("id_medico", idMedico);
                loading.dismiss();
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void mensajeConfirmacion(AsignacionMedico item) { //Método para confirmar la eliminación
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MedicoAsignacion.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Desea eliminar esta asignación?");
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