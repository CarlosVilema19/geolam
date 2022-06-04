package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GestionLugares extends AppCompatActivity {
    Button  btnTipologia, btnEspecialidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_lugares);

        btnTipologia= findViewById(R.id.btnTipologia);
        btnEspecialidad= findViewById(R.id.btnEspecialidad);

        btnTipologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionLugares.this, IngresoTipologia.class);
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
    }
}