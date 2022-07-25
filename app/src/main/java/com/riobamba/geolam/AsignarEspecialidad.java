package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoAsignarEspecialidadAdaptador;
import com.riobamba.geolam.modelo.ListadoAsignarLugarMedico;
import com.riobamba.geolam.modelo.ListadoAsignarLugarMedicoAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AsignarEspecialidad extends AppCompatActivity {

    TextView tvIdEspecialidad ;
    TextView tvIdLugarMedico ;

    String idEspecialidad;
    String idLugarMedico;

    String listIDEspecialidadID;
    String listIDEspecialidadNombres;
    String listIDLugarMedicoID;
    String listIDLugarMedicoNombres;
    Button btnGuardarAsig;

    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar


    //Especialidades - Lugares Medicos
    ArrayList<String> opcionesEspecialidadID = new ArrayList<>();
    ArrayList<String> opcionesEspecialidadNombres = new ArrayList<>();
    ArrayList<String> opcionesLugaresMedicosID = new ArrayList<>();
    ArrayList<String> opcionesLugaresMedicosNombres = new ArrayList<>();

    AutoCompleteTextView autoCompleteOpcionesEspecialidad;
    AutoCompleteTextView autoCompleteOpcionesLugarMedico;
    //Autocomplete Especialidad, Lugar
   // private ListadoAsignarEspecialidadAdaptador adaptadorEspecialidad;
   // private ListadoAsignarLugarMedicoAdaptador adaptadorLugarMedico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_especialidad);
        tvIdEspecialidad = findViewById(R.id.TextViewIDEspecialidad2);
        tvIdLugarMedico = findViewById(R.id.TextViewIDLugarMedico2);
        btnGuardarAsig = findViewById(R.id.btn_guardarAsigEspecialidad);

        toolbar.show(this, "Gestión de lugares", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)


       // adaptadorEspecialidad = new ListadoAsignarEspecialidadAdaptador(this);
        autoCompleteOpcionesEspecialidad = findViewById(R.id.autoEspecialidad2);
        //autoCompleteOpcionesEspecialidad.setAdapter(adaptadorEspecialidad);

        //adaptadorLugarMedico = new ListadoAsignarLugarMedicoAdaptador(this);
        autoCompleteOpcionesLugarMedico = findViewById(R.id.autoLugarMedico2);
        //autoCompleteOpcionesLugarMedico.setAdapter(adaptadorLugarMedico);



        //Conexión al Servidor- Consulta AutoComplete Especialidad

        String url2 = WebService.urlRaiz + WebService.servicioAsignarEspecialidad;
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,

                response ->
                {
                    try {


                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            //ListadoAsignarEspecialidad esp = new ListadoAsignarEspecialidad(object);
                            //Carga de datos
                            //adaptadorEspecialidad.add(esp);
                            listIDEspecialidadNombres = (object.getString("DESCRIPCION_ESPECIALIDAD"));
                            opcionesEspecialidadNombres.add(listIDEspecialidadNombres);
                            listIDEspecialidadID = (object2.getString("ID_ESPECIALIDAD"));
                            opcionesEspecialidadID.add(listIDEspecialidadID);
                            ArrayAdapter adapter;
                            adapter=new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, opcionesEspecialidadNombres);
                            autoCompleteOpcionesEspecialidad.setAdapter(adapter);

                        }

                        autoCompleteOpcionesEspecialidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();

                                String c = opcionesEspecialidadID.get(position);
                                idEspecialidad = c;
                                retornaIdEspecialidad(idEspecialidad);


                                //Toast.makeText(getApplicationContext(), "Item23: " + idEspecialidad, Toast.LENGTH_SHORT).show();

                            }

                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(this, "Error -->" + error.toString(), Toast.LENGTH_SHORT).show();

        });
        RequestQueue queue2 = Volley.newRequestQueue(this);
        stringRequest2.setTag("REQUEST");
        queue2.add(stringRequest2);


        //Conexión al Servidor- Consulta AutoComplete Lugar Médico

        String url3 = WebService.urlRaiz + WebService.servicioAsignarLugarMedico;
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3,

                response ->
                {
                    try {


                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {

                           // JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            //ListadoAsignarLugarMedico lugarMedic = new ListadoAsignarLugarMedico(object);
                            //Carga de datos
                            //adaptadorLugarMedico.add(lugarMedic);
                            listIDLugarMedicoID = (object2.getString("ID_LUGAR"));
                            opcionesLugaresMedicosID.add(listIDLugarMedicoID);
                            listIDLugarMedicoNombres = (object2.getString("NOMBRE_LUGAR"));
                            opcionesLugaresMedicosNombres.add(listIDLugarMedicoNombres);
                            ArrayAdapter adapter;
                            adapter=new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, opcionesLugaresMedicosNombres);
                            autoCompleteOpcionesLugarMedico.setAdapter(adapter);

                        }

                        autoCompleteOpcionesLugarMedico.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();

                                String c = opcionesLugaresMedicosID.get(position);

                                idLugarMedico = c;


                                retornaIdLugarMedico(idLugarMedico);


                                //Toast.makeText(getApplicationContext(), "Item23: " + idLugarMedico, Toast.LENGTH_SHORT).show();

                            }

                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(this, "Error -->" + error.toString(), Toast.LENGTH_SHORT).show();

        });
        RequestQueue queue3 = Volley.newRequestQueue(this);
        stringRequest3.setTag("REQUEST");
        queue3.add(stringRequest3);


        btnGuardarAsig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarLugar();
            }
        });

    }

    private void retornaIdLugarMedico(String idLugarMedico) {
        CharSequence p=idLugarMedico;
        String p2=p.toString();
        tvIdLugarMedico.setText(p2);

        //Carga de datos
    }

    private void retornaIdEspecialidad(String idEspecialidad) {
        CharSequence p=idEspecialidad;
        String p2=p.toString();
        tvIdEspecialidad.setText(p2);
        //Carga de datos
    }
    private void insertarLugar() {

        String url = WebService.urlRaiz +WebService.servicioIngresarEspecialidadLugar;
        final ProgressDialog loading = ProgressDialog.show(this, "Guardando la información...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Descartar el diálogo de progreso
                loading.dismiss();

                //Mostrando el mensaje de la respuesta
                Toast.makeText(getApplicationContext(), "Se ha registrado el lugar correctamente", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(), Login.class));
                //finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                //Showing toast
                Toast.makeText(getApplicationContext(), "ERROR" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id_lugar",tvIdLugarMedico.getText().toString());
                parametros.put("id_especialidad",tvIdEspecialidad.getText().toString());




                return parametros;
            }
        };
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
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