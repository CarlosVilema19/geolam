package com.riobamba.geolam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.riobamba.geolam.modelo.ConexionMapa;

public class Inicio extends AppCompatActivity {
    Button btnlugarmedico, btnListadoLugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btnlugarmedico = findViewById(R.id.btnlugarescercanos);
        btnListadoLugar = findViewById(R.id.btnlugaresmedicos);

        btnlugarmedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, ConexionMapa.class);
                startActivity(intent);
            }
        }
        );

        btnListadoLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Listado.class);
                startActivity(intent);
            }
        }
        );
    }


}