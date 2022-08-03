package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.riobamba.geolam.modelo.ListadoAsignarEspecialidad;
import com.riobamba.geolam.modelo.ListadoAsignarEspecialidadAdaptador;
import com.riobamba.geolam.modelo.ListadoAsignarLugarMedico;
import com.riobamba.geolam.modelo.ListadoAsignarLugarMedicoAdaptador;
import com.riobamba.geolam.modelo.ListadoAsignarMedico;
import com.riobamba.geolam.modelo.ListadoAsignarMedicoAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
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
    String listMedicoNombres;
    String listEspecialidadNombres;
    String listLugarMedicoNombres;

    Button btnGuardarAsignacion;
    Button btnAsignaciones;

    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    String guardarValor;
    //Listado ID Médico - Especialidades - Lugares Medicos
    ArrayList<String> opcionesMedicoID= new ArrayList<>();
    ArrayList<String> opcionesEspecialidadID= new ArrayList<>();
    ArrayList<String> opcionesLugaresMedicosID= new ArrayList<>();

    //Listado Nombres -Médico-Especialidades-Luhares Médicos

    ArrayList<String> opcionesMedicoNombres= new ArrayList<>();
    ArrayList<String> opcionesEspecialidadNombres= new ArrayList<>();
    ArrayList<String> opcionesLugaresMedicosNombres= new ArrayList<>();
    AutoCompleteTextView autoCompleteOpcionesLugarMedico;
    AutoCompleteTextView autoCompleteOpcionesEspecialidad;
    AutoCompleteTextView autoCompleteOpcionesMedico;

    //Autocomplete Medico, Especialidad, Lugar
    /*private ListadoAsignarMedicoAdaptador adaptadorMedico;
    private ListadoAsignarEspecialidadAdaptador adaptadorEspecialidad;
    private ListadoAsignarLugarMedicoAdaptador adaptadorLugarMedico;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_medico);
        tvIdMedico= findViewById(R.id.TextViewIDMedico);
        tvIdEspecialidad=findViewById(R.id.TextViewIDEspecialidad);
        tvIdLugarMedico=findViewById(R.id.TextViewIDLugar);
        btnGuardarAsignacion = findViewById(R.id.btn_guardarAsignarMedic);
        btnAsignaciones = findViewById(R.id.btnAsignaciones);

        //AutoComplete
        //adaptadorMedico=new ListadoAsignarMedicoAdaptador(this);
       autoCompleteOpcionesMedico=findViewById(R.id.autoMedico);

        //autoCompleteOpcionesMedico.setAdapter(adaptadorMedico);

       // adaptadorEspecialidad= new ListadoAsignarEspecialidadAdaptador(this);
       autoCompleteOpcionesEspecialidad=findViewById(R.id.autoEspecialidad);
        //autoCompleteOpcionesEspecialidad.setAdapter(adaptadorEspecialidad);

       // adaptadorLugarMedico= new ListadoAsignarLugarMedicoAdaptador(this);
      autoCompleteOpcionesLugarMedico =findViewById(R.id.autoLugar);
       // autoCompleteOpcionesLugarMedico.setAdapter(adaptadorLugarMedico);


        toolbar.show(this, "Gestión de lugares", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)


        btnAsignaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AsignarMedico.this, MedicoAsignacion.class);
                startActivity(intent);
            }
        });


        btnGuardarAsignacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertarAsignacion();

            }
        });

        //Conexión al Servidor- Consulta AutoComplete Medicos

        String url= WebService.urlRaiz+WebService.servicioAsignarMedico;
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url,

                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            //ListadoAsignarMedico medico=new ListadoAsignarMedico(object);
                            //Carga de datos
                            //adaptadorMedico.add(medico);
                            listMedicoNombres= (object.getString("NOMBRE_MEDICO").concat(" ").concat(object2.getString("APELLIDO_MEDICO")));
                            opcionesMedicoNombres.add(listMedicoNombres);
                            listIDMedico = ( object2.getString("ID_MEDICO"));
                            opcionesMedicoID.add(listIDMedico);
                            ArrayAdapter adapter;
                            adapter=new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, opcionesMedicoNombres);
                            autoCompleteOpcionesMedico.setAdapter(adapter);
                        }

                        autoCompleteOpcionesMedico.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();
                                String c= opcionesMedicoID.get(position);
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
        RequestQueue queue= Volley.newRequestQueue(this);
        stringRequest.setTag("REQUEST");
        queue.add(stringRequest);


        //Conexión al Servidor- Consulta AutoComplete Especialidad

        String url2= WebService.urlRaiz+WebService.servicioEspecialidadesDisponibles;
        StringRequest stringRequest2= new StringRequest(Request.Method.GET,url2,

                response ->
                {
                    try{
                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            //ListadoAsignarEspecialidad esp=new ListadoAsignarEspecialidad(object);
                            //Carga de datos
                            //adaptadorEspecialidad.add(esp);
                            listEspecialidadNombres=(object.getString("descripcion_especialidad"));
                            opcionesEspecialidadNombres.add(listEspecialidadNombres);
                            listIDEspecialidad = ( object2.getString("id_especialidad"));
                            opcionesEspecialidadID.add(listIDEspecialidad);
                            ArrayAdapter adapter;
                            adapter=new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, opcionesEspecialidadNombres);
                            autoCompleteOpcionesEspecialidad.setAdapter(adapter);
                        }

                        autoCompleteOpcionesEspecialidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();
                                String c= opcionesEspecialidadID.get(position);
                                idEspecialidad=c;
                                retornaIdEspecialidad(idEspecialidad);
                                aparecerLugares(retornaIdEspecialidad(idEspecialidad));
                                //guardarValor= retornaIdEspecialidad(idEspecialidad);
                                //Toast.makeText(getApplicationContext(), "ID prueba " + retornaIdEspecialidad(idEspecialidad), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), "ID prueba " +tvIdEspecialidad.getText().toString(), Toast.LENGTH_SHORT).show();
                               // Toast.makeText(getApplicationContext(), "Item23: " +idEspecialidad, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {
            Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        RequestQueue queue2= Volley.newRequestQueue(this);
        stringRequest2.setTag("REQUEST");
        queue2.add(stringRequest2);


    }

    private void aparecerLugares(String s) {

        String valorID=s;
        //Toast.makeText(this,"este es "+ s,Toast.LENGTH_SHORT).show();
        //Conexión al Servidor- Consulta AutoComplete Lugar Médico
        autoCompleteOpcionesLugarMedico.setAdapter(null);
        opcionesLugaresMedicosID.clear();
        listIDLugarMedico = ( null);
        //opcionesLugaresMedicosID.add(null);
        listLugarMedicoNombres = (null);
        opcionesLugaresMedicosNombres.clear();
        String url3= WebService.urlRaiz+WebService.servicioEspecialidadLugar;
        StringRequest stringRequest3= new StringRequest(Request.Method.POST,url3,

                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            //ListadoAsignarLugarMedico lugarMedic=new ListadoAsignarLugarMedico(object);
                            //Carga de datos
                            //adaptadorLugarMedico.add(lugarMedic);
                            listIDLugarMedico = ( object.getString("id_lugar"));
                            opcionesLugaresMedicosID.add(listIDLugarMedico);
                            listLugarMedicoNombres = (object2.getString("nombre_lugar"));
                            opcionesLugaresMedicosNombres.add(listLugarMedicoNombres);
                            ArrayAdapter adapter;
                            adapter=new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, opcionesLugaresMedicosNombres);
                            autoCompleteOpcionesLugarMedico.setAdapter(adapter);

                        }

                        autoCompleteOpcionesLugarMedico.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();
                                String c= opcionesLugaresMedicosID.get(position);
                                idLugarMedico=c;
                                retornaIdLugarMedico(idLugarMedico);
                                //Toast.makeText(getApplicationContext(), "Item23: " +idLugarMedico, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {

            Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("id_especialidad",valorID.toString());

                return parametros;
            }
        };



        RequestQueue queue3= Volley.newRequestQueue(this);
        stringRequest3.setTag("REQUEST");
        queue3.add(stringRequest3);
    }

    private void retornaIdLugarMedico(String idLugarMedico) {
        CharSequence p=idLugarMedico;
        String p2= p.toString();
        tvIdLugarMedico.setText(p2);
        //Carga de datos
    }

    private String retornaIdEspecialidad(String idEspecialidad) {
        CharSequence p=idEspecialidad;
        String p2=p.toString();
        tvIdEspecialidad.setText(p2);
        String a=tvIdEspecialidad.getText().toString();
        return a;
        //Carga de datos
    }

    private void retornaIdMedico(String idMedico) {
        CharSequence p=idMedico;
        String p2=p.toString();
        tvIdMedico.setText(p2);
        //Carga de datos
    }

    private void insertarAsignacion() {
        if(!tvIdMedico.getText().toString().equals("")
                &&!tvIdLugarMedico.getText().toString().equals("")
                && !tvIdEspecialidad.getText().toString().equals(""))
        {
            String url = WebService.urlRaiz + WebService.servicioIngresarMedicoTrabaja;
            final ProgressDialog loading = ProgressDialog.show(this, "Guardando la información...", "Espere por favor");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Descartar el diálogo de progreso
                    loading.dismiss();

                    //Mostrando el mensaje de la respuesta
                    Toast.makeText(getApplicationContext(), "Se ha asignado el médico correctamente", Toast.LENGTH_SHORT).show();
                    tvIdEspecialidad.setText(null);
                    tvIdMedico.setText(null);
                    tvIdLugarMedico.setText(null);
                    autoCompleteOpcionesMedico.clearListSelection();
                    autoCompleteOpcionesEspecialidad.clearListSelection();
                    autoCompleteOpcionesLugarMedico.clearListSelection();
                    autoCompleteOpcionesLugarMedico.requestFocus();
                    autoCompleteOpcionesEspecialidad.requestFocus();
                    autoCompleteOpcionesMedico.requestFocus();

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
                    parametros.put("id_lugar", tvIdLugarMedico.getText().toString());
                    parametros.put("id_especialidad", tvIdEspecialidad.getText().toString());
                    parametros.put("id_medico", tvIdMedico.getText().toString());
                    return parametros;
                }
            };
            //Creación de una cola de solicitudes
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //Agregar solicitud a la cola
            requestQueue.add(stringRequest);
        }
        else {

            if (tvIdMedico.getText().toString().equals("")
                    &&tvIdEspecialidad.getText().toString().equals("")
                    &&tvIdLugarMedico.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Seleccione la información correspondiente", Toast.LENGTH_SHORT).show();

                }else{
                    if (tvIdMedico.getText().toString().equals("")
                            &&tvIdEspecialidad.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Seleccione un médico y una Especialidad", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (tvIdMedico.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Seleccione un Médico", Toast.LENGTH_SHORT).show();
                        }
                        else{

                                if (tvIdEspecialidad.getText().toString().equals("")&&tvIdLugarMedico.getText().toString().equals("")) {
                                    Toast.makeText(getApplicationContext(), "Seleccione una Especialidad & Lugar Médico", Toast.LENGTH_SHORT).show();

                            }else {
                                if (tvIdEspecialidad.getText().toString().equals("")) {
                                    Toast.makeText(getApplicationContext(), "Seleccione una Especialidad", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if(tvIdLugarMedico.getText().toString().equals("")){

                                        Toast.makeText(getApplicationContext(), "Seleccione un Lugar Médico", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }
                    }

                }
            }
/*
        autoCompleteOpcionesLugarMedico.setAdapter(null);
        opcionesLugaresMedicosID.clear();
        listIDLugarMedico = ( null);
        //opcionesLugaresMedicosID.add(null);
        listLugarMedicoNombres = (null);
        opcionesLugaresMedicosNombres.clear();

        autoCompleteOpcionesEspecialidad.setAdapter(null);
        opcionesEspecialidadID.clear();
        listIDEspecialidad=(null);
        listEspecialidadNombres=(null);
        opcionesEspecialidadNombres.clear();

*/


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
