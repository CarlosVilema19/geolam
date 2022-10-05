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
import android.text.Editable;
import android.text.TextWatcher;
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

public class LugarBusqueda extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    EditText txtCate,txtTipo,txtEspe;
    Button btnBuscar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    String categoria, tipologia, especialidad;
    //Items Sexo F y M
    AutoCompleteTextView autoCompleteTxtEdSexo;
    ArrayAdapter<String> adapterItems;

    String[] items = {"NINGUNO","PRIVADO", "PUBLICO"};

    //Tipología
    String listTipologiasNombres;
    ArrayList<String> opcionesTipologiaNombres= new ArrayList<>();

    AutoCompleteTextView autoCompleteOpcionesTipologia;

    //Especialidad
    String listEspeNombres;
    AutoCompleteTextView autoCompleteEspecialidad;
    ArrayList<String> especialidades= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_lugar);
        txtCate = findViewById(R.id.edBusCate);
        txtTipo = findViewById(R.id.edBusTipo);
        txtEspe = findViewById(R.id.edBusEspe);
        btnBuscar = findViewById(R.id.btnBuscar);
        autoCompleteOpcionesTipologia=findViewById(R.id.autoTipologia4);
        autoCompleteEspecialidad=findViewById(R.id.autoBusqEspe);

        toolbar.show(this, "Búsqueda Avanzada", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)
        tipologia();
        especialidades();
        //Items Sexo F y M Autocomplete
        autoCompleteTxtEdSexo = findViewById(R.id.edBusCate);
        adapterItems = new ArrayAdapter<String>(this, R.layout.lista_items, items);
        autoCompleteTxtEdSexo.setAdapter(adapterItems);
        autoCompleteTxtEdSexo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
                if(item.equals("NINGUNO"))
                {
                    autoCompleteTxtEdSexo.setText("");
                }

            }

        });
        autoCompleteEspecialidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (autoCompleteEspecialidad.getOnItemSelectedListener()==null){
                    txtEspe.setText("");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                categoria = txtCate.getText().toString().toUpperCase().trim();
                tipologia = txtTipo.getText().toString().toUpperCase().trim();
                especialidad = txtEspe.getText().toString().toUpperCase().trim();
                
                if(categoria.equals("") && tipologia.equals("") && especialidad.equals(""))
                {
                    Toast.makeText(LugarBusqueda.this, "Ingrese al menos un campo", Toast.LENGTH_SHORT).show();
                }
                else {
                    guardarCate(categoria);
                    guardarTipo(tipologia);
                    guardarEspe(especialidad);
                    Intent intent = new Intent(LugarBusqueda.this, LugarBusquedaList.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void tipologia() {
        //Conexión al Servidor- Consulta AutoComplete Tipología

        String url=WebService.urlRaiz+WebService.servicioListarTipologia;
        //adaptadorTipo.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url,
                response ->
                {
                    try {


                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            //JSONObject object2 = array.getJSONObject(i);
                            //Carga de datos
                            //adaptadorTipo.add(tipo);
                            listTipologiasNombres = (object.getString("DESCRIPCION_TIPO_LUGAR"));
                            //listTipologiasNombres[listTipologiasNombres.length()+1]="NINGUNO";
                            opcionesTipologiaNombres.add(listTipologiasNombres);

                            //Toast.makeText(this,opcionesTipologiaNombres.size(),Toast.LENGTH_SHORT).show();
                            //listIDTipo = ( object.getString("ID_TIPOLOGIA_LUGAR"));
                            //opcionesTipologia.add(listIDTipo);


                        }
                        opcionesTipologiaNombres.add(opcionesTipologiaNombres.size(), "NINGUNO");
                        ArrayAdapter adapter;
                        adapter=new ArrayAdapter<String> (this, R.layout.lista_items, opcionesTipologiaNombres);
                        autoCompleteOpcionesTipologia.setAdapter(adapter);
                        autoCompleteOpcionesTipologia.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               // String itemTipo = parent.getItemAtPosition(position).toString();


                                //String p= String.valueOf(position).trim();

                                //String c= opcionesTipologia.get(position);

                                //pTip=c;

                                //retornaIdTipologia(pTip);
                                String item = parent.getItemAtPosition(position).toString();
                                if(item.equals("NINGUNO"))
                                {
                                    autoCompleteOpcionesTipologia.setText("");
                                    txtTipo.setText("");

                                }
                                else {

                                    String selected = (String) parent.getItemAtPosition(position);
                                    int pos = opcionesTipologiaNombres.indexOf(selected);
                                    txtTipo.setText(selected);
                                }

                            }

                        });



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

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
                            //JSONObject object2 = array.getJSONObject(i);
                            //Carga de datos
                            //adaptadorTipo.add(tipo);
                            listEspeNombres = (object.getString("DESCRIPCION_ESPECIALIDAD"));
                            //listTipologiasNombres[listTipologiasNombres.length()+1]="NINGUNO";
                            especialidades.add(listEspeNombres);

                            //Toast.makeText(this,opcionesTipologiaNombres.size(),Toast.LENGTH_SHORT).show();
                            //listIDTipo = ( object.getString("ID_TIPOLOGIA_LUGAR"));
                            //opcionesTipologia.add(listIDTipo);


                        }
                        ArrayAdapter adapter;
                        adapter=new ArrayAdapter<String> (this,R.layout.lista_items, especialidades);
                        autoCompleteEspecialidad.setAdapter(adapter);
                        autoCompleteEspecialidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // String itemTipo = parent.getItemAtPosition(position).toString();


                                //String p= String.valueOf(position).trim();

                                //String c= opcionesTipologia.get(position);

                                //pTip=c;

                                //retornaIdTipologia(pTip);
                               String item = parent.getItemAtPosition(position).toString();
                                if(item.equals(""))
                                {
                                    autoCompleteEspecialidad.setText("");
                                    txtEspe.setText("");

                                }
                                else {

                                    String selected = (String) parent.getItemAtPosition(position);
                                int pos = especialidades.indexOf(selected);
                                txtEspe.setText(selected);
                                }



                            }

                        });



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        stringRequest.setTag("REQUEST");
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    public void guardarCate(String cate)
    {
        SharedPreferences preferences = getSharedPreferences("cate",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cate", cate);
        editor.apply();
    }
    public void guardarTipo(String tipo)
    {
        SharedPreferences preferences = getSharedPreferences("tipo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tipo", tipo);
        editor.apply();
    }
    public void guardarEspe(String espe)
    {
        SharedPreferences preferences = getSharedPreferences("espe",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("espe", espe);
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



