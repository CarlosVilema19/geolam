package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.riobamba.geolam.modelo.Toolbar;

public class Configuraciones extends AppCompatActivity {

    Toolbar toolbar = new Toolbar();
    Button btnMisDatos,btnContrasenia, btnEliminarCuenta,btnAcercaApp, btnTerminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciones);

        toolbar.show(this, "Configuración", true);

        btnMisDatos=findViewById(R.id.btnCuenta_);
        btnContrasenia=findViewById(R.id.btnContrasenia);
        btnEliminarCuenta=findViewById(R.id.btnEliminarCuenta);
        btnAcercaApp=findViewById(R.id.btnAcercaApp);
        btnTerminos=findViewById(R.id.btnTerminosCondiciones);

        btnMisDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Configuraciones.this, DatosPersonalesUsuario.class);
                startActivity(intent);
            }
        });

        btnContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Configuraciones.this, CambiarContrasenia.class);
                startActivity(intent);
            }
        });
        btnEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(Configuraciones.this, EliminarCuenta.class);
              startActivity(intent);
            }
        });
        btnAcercaApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Configuraciones.this, InfoApp.class);
                startActivity(intent);
            }
        });
        btnTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Configuraciones.this, TerminosCondiciones.class);
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
}