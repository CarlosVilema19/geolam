package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.riobamba.geolam.modelo.Toolbar;

public class Busqueda extends AppCompatActivity {
    Button btnBusRapLug, btnBusAvanLug, btnBusRapEspe, btnBusAvanMed, btnBusRapMed, btnBusAvanEspe, btnBusSombra ;

    Toolbar toolbar = new Toolbar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        btnBusAvanLug = findViewById(R.id.btnBusAvanLugar);
        btnBusRapLug = findViewById(R.id.btnBusRapLugar);
        btnBusRapEspe = findViewById(R.id.btnBusRapEspe);
        btnBusAvanMed = findViewById(R.id.btnBusAvanMed);
        btnBusRapMed = findViewById(R.id.btnBusRapMed);
        btnBusAvanEspe = findViewById(R.id.btnBusAvanEspe);

        toolbar.show(this, "Búsqueda", true);
        btnBusSombra = findViewById(R.id.btnEspecialidad);
        btnBusSombra.setBackgroundColor(Color.argb(30,128,128,128));

        btnBusRapLug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Busqueda.this, LugarMapa.class);
                startActivity(intent);
            }
        });

        btnBusAvanLug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Busqueda.this, LugarBusqueda.class);
                startActivity(intent);
            }
        });

        btnBusRapEspe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Busqueda.this, EspecialidadListadoUsuario.class);
                startActivity(intent);
            }
        });

        btnBusAvanEspe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Busqueda.this, Buscar_Especialidades.class);
                startActivity(intent);
            }
        });

        btnBusRapMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Busqueda.this, MedicoUsuario.class);
                startActivity(intent);
            }
        });

        btnBusAvanMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Busqueda.this, MedicoBusqueda.class);
                startActivity(intent);
            }
        });



    }


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


}