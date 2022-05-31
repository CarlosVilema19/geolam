package com.riobamba.geolam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Inicio extends AppCompatActivity {
    Button btnlugarmedico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btnlugarmedico = findViewById(R.id.btnlugarescercanos);
        btnlugarmedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, LugarMedico.class);
                startActivity(intent);
            }
        }

        );
    }


}