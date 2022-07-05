package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GestionLugares extends AppCompatActivity {
    Button  btnTipologia, btnEspecialidad, btnLugares, btnMedico, btnAsignarMedico, btnAsignarEspecialidades,
            btnActualizarLugarMedico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_lugares);

        btnTipologia= findViewById(R.id.btnTipologia);
        btnEspecialidad= findViewById(R.id.btnEspecialidad);
        btnMedico = findViewById(R.id.btnMedico);
        btnLugares=findViewById(R.id.btnLugares);
        btnAsignarMedico=findViewById(R.id.btnAsignarMedico);
        btnAsignarEspecialidades=findViewById(R.id.btnAsignarEspecialidad);
        btnActualizarLugarMedico=findViewById(R.id.btnActualizarLugar);
        btnTipologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionLugares.this, IngresoTipologia.class);
                startActivity(intent);
            }
        });

        btnActualizarLugarMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionLugares.this, actualizar_lugar_medico.class);
                startActivity(intent);
            }
        });

        btnAsignarMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionLugares.this, AsignarMedico.class);
                startActivity(intent);
            }
        });
        btnAsignarEspecialidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionLugares.this, AsignarEspecialidad.class);
                startActivity(intent);
            }
        });


        btnEspecialidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionLugares.this, IngresoEspecialidad.class);
                startActivity(intent);
            }
        });

        btnMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionLugares.this, IngresoMedico.class);
                startActivity(intent);
            }
        });
        btnLugares.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionLugares.this, IngresoLugarMedico.class);
                startActivity(intent);
            }
        });

    }
}