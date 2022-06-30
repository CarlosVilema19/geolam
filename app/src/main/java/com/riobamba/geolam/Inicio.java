package com.riobamba.geolam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.navigation.NavigationView;
import com.riobamba.geolam.modelo.ConexionMapa;
import com.riobamba.geolam.modelo.ListadoLugar;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.ListadoMapa;

public class Inicio extends AppCompatActivity {
    Button btnlugarmedico, btnListadoLugar, btnEspecialidades;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;


    public static final Integer btnListadoMapa = R.layout.activity_listado_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        setContentView(R.layout.activity_menu);
        btnlugarmedico = findViewById(R.id.btnlugarescercanos);
        btnListadoLugar = findViewById(R.id.btnlugaresmedicos);
        btnEspecialidades = findViewById(R.id.btnespecialidades);

        //Login login = (Login) getIntent().getSerializableExtra("Login");




        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.menu);
        navigationView = findViewById(R.id.navigationViewer);

        actionBarDrawerToggle = new ActionBarDrawerToggle( this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        navigationView.setItemIconTintList(null);






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

                Intent intent = new Intent(Inicio.this,Listado.class);
                //intent.putExtra("Inicio", (Parcelable) login);
                startActivity(intent);


            }
        }
        );
        btnEspecialidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, ListadoEspecialidad.class);
                startActivity(intent);
            }
        }
        );
    }


}

