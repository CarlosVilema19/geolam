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
import android.content.SharedPreferences;
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
    EditText txtCate,txtTipo,txtEspe;
    Button btnBuscar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    String categoria, tipologia, especialidad;
    //Items Sexo F y M
    AutoCompleteTextView autoCompleteTxtEdSexo;
    ArrayAdapter<String> adapterItems;
    String[] items = {"NINGUNO","PRIVADO", "PUBLICO"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_lugar);
        txtCate = findViewById(R.id.edBusCate);
        txtTipo = findViewById(R.id.edBusTipo);
        txtEspe = findViewById(R.id.edBusEspe);
        btnBuscar = findViewById(R.id.btnBuscar);

        toolbar.show(this, "Búsqueda Avanzada", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

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

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                categoria = txtCate.getText().toString();
                tipologia = txtTipo.getText().toString();
                especialidad = txtEspe.getText().toString();
                
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

 /*   private void buscarLugar() {
        String url = WebService.urlRaiz + WebService.servicioBusquedaLugar;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Se ha agregado con éxito", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("categoria", txtCate.getText().toString());
                parametros.put("tipologia", txtTipo.getText().toString());
                parametros.put("especialidad", txtEspe.getText().toString());

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

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
        toolbar.getContexto(this);
        startActivity(toolbar.retornarInicio());
    }
    public void moverMapa(View view)    //dirige al mapa
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarMapa());
    }
    public void moverEspe(View view)    //dirige a la especialidad
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarEspecialidad());
    }

}



