package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.BuscarEspecialidadesLMAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Buscar_Especialidades extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    BuscarEspecialidadesLMAdaptador adaptador;
    AutoCompleteTextView acLugar;
    Button btnBuscarEsp;
    String lugar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    //Lugar
    String listLugar;
    ArrayList<String> opListLugar= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_especialidades);
        adaptador = new BuscarEspecialidadesLMAdaptador(this);
        btnBuscarEsp = findViewById(R.id.btnBuscarEspe);

        toolbar.show(this, "Búsqueda Avanzada", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)
        lugar();

        //Autocomplete
        acLugar = findViewById(R.id.autoCompleteLugar);

        btnBuscarEsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lugar = acLugar.getText().toString();

                if(lugar.equals(""))
                {
                    Toast.makeText(Buscar_Especialidades.this, "Campo vacío", Toast.LENGTH_SHORT).show();
                }
                else {
                    guardarLugar(lugar);
                    Intent intent = new Intent(Buscar_Especialidades.this, EspecialidadBusquedaList.class);
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

    public void guardarLugar(String lugarEspe)
    {
        SharedPreferences preferences = getSharedPreferences("lugarEspe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lugarEspe", lugarEspe);
        editor.apply();
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