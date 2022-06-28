package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.riobamba.geolam.modelo.ListadoAsignarEspecialidad;
import com.riobamba.geolam.modelo.ListadoAsignarEspecialidadAdaptador;
import com.riobamba.geolam.modelo.ListadoAsignarLugarMedico;
import com.riobamba.geolam.modelo.ListadoAsignarLugarMedicoAdaptador;
import com.riobamba.geolam.modelo.ListadoAsignarMedico;
import com.riobamba.geolam.modelo.ListadoAsignarMedicoAdaptador;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AsignarMedico extends AppCompatActivity {
    TextView tvIdMedico;
    TextView tvIdEspecialidad ;
    TextView tvIdLugarMedico ;

    String idMedico;
    String idEspecialidad;
    String idLugarMedico;
    String listIDMedico;
    String listIDEspecialidad;
    String listIDLugarMedico;
    Button btnGuardarAsignacion;

    //Listado ID Médico - Especialidades - Lugares Medicos
    ArrayList<String> opcionesMedico= new ArrayList<>();
    ArrayList<String> opcionesEspecialidad= new ArrayList<>();
    ArrayList<String> opcionesLugaresMedicos= new ArrayList<>();

    //Autocomplete Medico, Especialidad, Lugar
    private ListadoAsignarMedicoAdaptador adaptadorMedico;
    private ListadoAsignarEspecialidadAdaptador adaptadorEspecialidad;
    private ListadoAsignarLugarMedicoAdaptador adaptadorLugarMedico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_medico);
        tvIdMedico= findViewById(R.id.TextViewIDMedico);
        tvIdEspecialidad=findViewById(R.id.TextViewIDEspecialidad);
        tvIdLugarMedico=findViewById(R.id.TextViewIDLugar);
        btnGuardarAsignacion = findViewById(R.id.btn_guardarAsignarMedic);

        //AutoComplete
        adaptadorMedico=new ListadoAsignarMedicoAdaptador(this);
        AutoCompleteTextView autoCompleteOpcionesMedico=findViewById(R.id.autoMedico);
        autoCompleteOpcionesMedico.setAdapter(adaptadorMedico);

        adaptadorEspecialidad= new ListadoAsignarEspecialidadAdaptador(this);
        AutoCompleteTextView autoCompleteOpcionesEspecialidad=findViewById(R.id.autoEspecialidad);
        autoCompleteOpcionesEspecialidad.setAdapter(adaptadorEspecialidad);

        adaptadorLugarMedico= new ListadoAsignarLugarMedicoAdaptador(this);
        AutoCompleteTextView autoCompleteOpcionesLugarMedico =findViewById(R.id.autoLugar);
        autoCompleteOpcionesLugarMedico.setAdapter(adaptadorLugarMedico);

        btnGuardarAsignacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertarAsignacion();
            }
        });

        //Conexión al Servidor- Consulta AutoComplete Medicos
        RequestQueue queue= Volley.newRequestQueue(this);
        String url= WebService.urlRaiz+WebService.servicioAsignarMedico;
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url,

                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            ListadoAsignarMedico medico=new ListadoAsignarMedico(object);
                            //Carga de datos
                            adaptadorMedico.add(medico);
                            listIDMedico = ( object2.getString("ID_MEDICO"));
                            opcionesMedico.add(listIDMedico);

                        }

                        autoCompleteOpcionesMedico.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();

                                String c= opcionesMedico.get(position);

                                idMedico=c;

                                retornaIdMedico(idMedico);


                               // Toast.makeText(getApplicationContext(), "Item23: " +idMedico, Toast.LENGTH_SHORT).show();

                            }

                        });





                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {
            Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        stringRequest.setTag("REQUEST");
        queue.add(stringRequest);


        //Conexión al Servidor- Consulta AutoComplete Especialidad
        RequestQueue queue2= Volley.newRequestQueue(this);
        String url2= WebService.urlRaiz+WebService.servicioAsignarEspecialidad;
        StringRequest stringRequest2= new StringRequest(Request.Method.GET,url2,

                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            ListadoAsignarEspecialidad esp=new ListadoAsignarEspecialidad(object);
                            //Carga de datos
                            adaptadorEspecialidad.add(esp);
                            listIDEspecialidad = ( object2.getString("ID_ESPECIALIDAD"));
                            opcionesEspecialidad.add(listIDEspecialidad);

                        }

                        autoCompleteOpcionesEspecialidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();

                                String c= opcionesEspecialidad.get(position);

                                idEspecialidad=c;


                                retornaIdEspecialidad(idEspecialidad);


                               // Toast.makeText(getApplicationContext(), "Item23: " +idEspecialidad, Toast.LENGTH_SHORT).show();

                            }

                        });





                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {
            Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        stringRequest2.setTag("REQUEST");
        queue2.add(stringRequest2);


        //Conexión al Servidor- Consulta AutoComplete Lugar Médico
        RequestQueue queue3= Volley.newRequestQueue(this);
        String url3= WebService.urlRaiz+WebService.servicioAsignarLugarMedico;
        StringRequest stringRequest3= new StringRequest(Request.Method.GET,url3,

                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            ListadoAsignarLugarMedico lugarMedic=new ListadoAsignarLugarMedico(object);
                            //Carga de datos
                            adaptadorLugarMedico.add(lugarMedic);
                            listIDLugarMedico = ( object2.getString("ID_LUGAR"));
                            opcionesLugaresMedicos.add(listIDLugarMedico);

                        }

                        autoCompleteOpcionesLugarMedico.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();

                                String c= opcionesLugaresMedicos.get(position);

                                idLugarMedico=c;


                                retornaIdLugarMedico(idLugarMedico);



                              //  Toast.makeText(getApplicationContext(), "Item23: " +idLugarMedico, Toast.LENGTH_SHORT).show();

                            }

                        });





                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {
            Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        stringRequest3.setTag("REQUEST");
        queue3.add(stringRequest3);
    }

    private void retornaIdLugarMedico(String idLugarMedico) {
        CharSequence p=idLugarMedico;
        String p2= p.toString();
        tvIdLugarMedico.setText(p2);
        //Carga de datos
    }

    private void retornaIdEspecialidad(String idEspecialidad) {
        CharSequence p=idEspecialidad;
        String p2=p.toString();
        tvIdEspecialidad.setText(p2);
        //Carga de datos
    }

    private void retornaIdMedico(String idMedico) {
        CharSequence p=idMedico;
        String p2=p.toString();
        tvIdMedico.setText(p2);
        //Carga de datos
    }

    private void insertarAsignacion() {
        String url = WebService.urlRaiz +WebService.servicioIngresarMedicoTrabaja;
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
                parametros.put("id_medico",tvIdMedico.getText().toString());
                return parametros;
            }
        };
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

}