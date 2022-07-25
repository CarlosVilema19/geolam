package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PostProcessor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class ListadoUsuariosAdminControl extends AppCompatActivity {
    //Declarar la lista y el recycler view
    List<ListadoUsuariosAdmin> usuariosList;
    RecyclerView recyclerView;
    ListadoUsuariosAdminAdaptador adaptador;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_admin);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usuariosList = new ArrayList<>();
        //llamar al mostrar resultado
        toolbar.show(this, "Gestión de usuarios", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        MostrarResultado();
    }

    public void MostrarResultado()
    {
        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioListarUsuariosAdmin;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                usuariosList.add(new ListadoUsuariosAdmin(
                                        obj.getString("nombre_usuario"),
                                        obj.getString("email"),
                                        obj.getString("imagen"),
                                        obj.getString("descripcion_tipo_usuario"),
                                        obj.getInt("id_tipo_usuario")
                                ));
                            }
                            ListadoUsuariosAdminAdaptador myadapter = new ListadoUsuariosAdminAdaptador(ListadoUsuariosAdminControl.this, usuariosList,
                                    new ListadoUsuariosAdminAdaptador.OnItemClickListener() {
                                        @Override//llamada al método para llamar a una pantalla cuando se presiona sobre el item
                                        public void onItemClick(ListadoUsuariosAdmin item) {moveToDescription(item);}
                                    }, new ListadoUsuariosAdminAdaptador.OnClickListener() {

                                @Override//llamada al método para borrar presionando sobre el botón
                                public void onClick(ListadoUsuariosAdmin item) {
                                    mensajeConfirmacion(item);
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
    public void moveToDescription(ListadoUsuariosAdmin item)// Método para llamar a una pantalla presionanado sobre el item
    {
    }
    public void moveToEliminar(ListadoUsuariosAdmin button) //Método para eliminar presionando sobre un botón
    {
        String emailUsuarios = button.getEmailUsuarios().toString();
        String url2 = WebService.urlRaiz+WebService.servicioEliminarUsuarios; //URL del web service

        final ProgressDialog loading = ProgressDialog.show(ListadoUsuariosAdminControl.this, "Eliminando...", "Espere por favor");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Oculta el progress dialog de confirmacion
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ListadoUsuariosAdminControl.class));
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
                parametros.put("email", emailUsuarios);
                loading.dismiss();
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void mensajeConfirmacion(ListadoUsuariosAdmin item) { //Método para confirmar la eliminación
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(ListadoUsuariosAdminControl.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Desea Eliminar el item?");
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
}