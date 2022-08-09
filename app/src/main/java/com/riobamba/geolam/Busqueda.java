package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.riobamba.geolam.modelo.Toolbar;

public class Busqueda extends AppCompatActivity {
    Button btnBusRapLug, btnBusAvanLug, btnBusRapEspe, btnBusAvanMed, btnBusRapMed ;

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

        toolbar.show(this, "Búsqueda", true);


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

        btnBusAvanMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Busqueda.this, MedicoBusqueda.class);
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