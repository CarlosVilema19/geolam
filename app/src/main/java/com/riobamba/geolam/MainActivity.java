package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.riobamba.geolam.modelo.ConexionMapa;

public class MainActivity extends AppCompatActivity {
    Button btnInicio, btnListarLugar, btnLog, btnRegistrar, btnLugarMedico, btnMapa;
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
                Intent intent = new Intent(MainActivity.this, LugarMedico.class);
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
    }
}
