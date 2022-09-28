package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.Toolbar;

public class GestionLugares extends AppCompatActivity {
    Button  btnTipologia, btnEspecialidad, btnLugares, btnMedico,
            btnAsignarMedico, btnAsignarEspecialidades;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
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
        toolbar.show(this, "Gestión de lugares", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)


        btnTipologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionLugares.this, IngresoTipologia.class);
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


    //Funcion para rellenar el menu contextual en la parte superior -- proviene de la clase Toolbar
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
    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}