package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.riobamba.geolam.modelo.ConexionMapa;
import com.riobamba.geolam.modelo.ListadoMapa;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button btnInicio, btnListarLugar, btnLog, btnRegistrar, btnLugarMedico, btnMapa,
            btnGestionLugar, btnGestionUsuario, btnInicioAdmin, btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInicio = findViewById(R.id.buttonInicio);
        btnListarLugar = findViewById(R.id.buttonListarLugar);
        btnLog = findViewById(R.id.buttonLogin);
        btnRegistrar = findViewById(R.id.buttonRegistrar);
        btnLugarMedico = findViewById(R.id.buttonLugarMedico);
        btnMapa= findViewById(R.id.btnMapa);
        btnGestionLugar= findViewById(R.id.btnGestionLugar);
        btnGestionUsuario= findViewById(R.id.btnGestionUsuario);
        btnInicioAdmin= findViewById(R.id.btnInicioAdmin);
        btnMenu= findViewById(R.id.btnMenu);

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Inicio.class);
                startActivity(intent);
            }
        });
        btnListarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListarLugarMedico.class);
                startActivity(intent);
            }
        });
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, registrar.class);
                startActivity(intent);
            }
        });
        btnLugarMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListarLugarUsuario.class);
                startActivity(intent);
            }
        });
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConexionMapa.class);
                startActivity(intent);
            }
        });
        btnGestionLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GestionLugares.class);
                startActivity(intent);
            }
        });
        btnGestionUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GestionUsuarios.class);
                startActivity(intent);
            }
        });
        btnInicioAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InicioAdmin.class);
                startActivity(intent);
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Menu.class);
                startActivity(intent);
            }
        });

    }
}