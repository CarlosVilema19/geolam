package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.ListadoLugar;
import com.riobamba.geolam.modelo.ListadoLugarAdaptador;
import com.riobamba.geolam.modelo.ListadoTipologia;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MedicoBusqueda extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    AutoCompleteTextView acLugar, acEspe;
    Button btnBuscar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    String lugar, especialidad;
    //Lugar
    String listLugar;
    ArrayList<String> opListLugar= new ArrayList<>();
    //Especialidad
    String listEspeNombres;
    ArrayList<String> especialidades= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_medico);
        acLugar = findViewById(R.id.acBusLugar);
        acEspe = findViewById(R.id.acBusEspe);
        btnBuscar = findViewById(R.id.btnBuscarMed);

        toolbar.show(this, "Búsqueda Avanzada", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)
        lugar();
        especialidades();
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lugar = acLugar.getText().toString().toUpperCase().trim();
                especialidad = acEspe.getText().toString().toUpperCase().trim();
                if(lugar.equals("") && especialidad.equals(""))
                {
                    Toast.makeText(MedicoBusqueda.this, "Ingrese al menos un campo", Toast.LENGTH_SHORT).show();
                }
                else {
                    guardarLugar(lugar);
                    guardarEspe(especialidad);
                    Intent intent = new Intent(MedicoBusqueda.this, MedicoBusquedaList.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void lugar() {
        //Conexión al Servidor- Consulta AutoComplete Tipología
        String url=WebService.urlRaiz+WebService.servicioAsignarLugarMedico;
        //adaptadorTipo.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.POST,url,
                response ->
                {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            listLugar = (object.getString("NOMBRE_LUGAR"));
                            opListLugar.add(listLugar);
                        }
                        opListLugar.add(opListLugar.size(), "NINGUNO");
                        ArrayAdapter adapter;
                        adapter=new ArrayAdapter<String> (this, R.layout.lista_items, opListLugar);
                        acLugar.setAdapter(adapter);
                        acLugar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = parent.getItemAtPosition(position).toString();
                                if(item.equals("NINGUNO"))
                                {
                                    acLugar.setText("");
                                }
                                else {
                                    String selected = (String) parent.getItemAtPosition(position);
                                    int pos = opListLugar.indexOf(selected);
                                    acLugar.setText(selected);
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error del servidor",Toast.LENGTH_SHORT).show();
        });
        stringRequest.setTag("REQUEST");
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    private void especialidades() {
        //Conexión al Servidor- Consulta AutoComplete Tipología
        String url=WebService.urlRaiz+WebService.servicioConsultaEspecialidades;
        //adaptadorTipo.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url,
                response ->
                {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            listEspeNombres = (object.getString("DESCRIPCION_ESPECIALIDAD"));
                            especialidades.add(listEspeNombres);
                        }
                        ArrayAdapter adapter;
                        adapter=new ArrayAdapter<String> (this,R.layout.lista_items, especialidades);
                        acEspe.setAdapter(adapter);
                        acEspe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = parent.getItemAtPosition(position).toString();
                                if(item.equals(""))
                                {
                                    acEspe.setText("");
                                }
                                else {

                                    String selected = (String) parent.getItemAtPosition(position);
                                    int pos = especialidades.indexOf(selected);
                                    acEspe.setText(selected);
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error del servidor",Toast.LENGTH_SHORT).show();
        });
        stringRequest.setTag("REQUEST");
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void guardarLugar(String lugar)
    {
        SharedPreferences preferences = getSharedPreferences("lugar",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lugar", lugar);
        editor.apply();
    }
    public void guardarEspe(String espe)
    {
        SharedPreferences preferences = getSharedPreferences("espeMed",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("espeMed", espe);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    //Metodos para la barra inferior
    public void moverInicio(View view) //dirige al Inicio
    {
        toolbar.getActividad(this,this);
        toolbar.retornarInicio();
    }
    public void moverMapa(View view)    //dirige al mapa
    {
        toolbar.getActividad(this,this);
        toolbar.retornarMapa();
    }
    public void moverEspe(View view)    //dirige a la especialidad
    {
        toolbar.getActividad(this,this);
        toolbar.retornarEspecialidad();
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

