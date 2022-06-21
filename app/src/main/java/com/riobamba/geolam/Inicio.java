package com.riobamba.geolam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.riobamba.geolam.modelo.ConexionMapa;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.ListadoMapa;

public class Inicio extends AppCompatActivity {
    Button btnlugarmedico, btnListadoLugar, btnEspecialidades;
    public static final Integer btnListadoMapa = R.layout.activity_listado_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btnlugarmedico = findViewById(R.id.btnlugarescercanos);
        btnListadoLugar = findViewById(R.id.btnlugaresmedicos);
        btnEspecialidades = findViewById(R.id.btnespecialidades);

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
        btnEspecialidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }
        );
    }


}