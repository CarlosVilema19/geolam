package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.riobamba.geolam.modelo.Toolbar;

public class InicioAdmin extends AppCompatActivity {
    Button btnGestionLugar, btnGestionUsuario;
    Toolbar toolbar = new Toolbar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_admin);

        btnGestionLugar= findViewById(R.id.btnGestionLugar);
        btnGestionUsuario= findViewById(R.id.btnGestionUsuario);

        toolbar.show(this, "Geolam", false);


        btnGestionLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioAdmin.this, GestionLugares.class);
                startActivity(intent);
            }
        });
        btnGestionUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioAdmin.this, ListadoUsuariosAdminControl.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Funcion para ejecutar las instrucciones de los items -- proviene de la clase Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        toolbar.ejecutarItemSelected(this,item);
        if(item.getItemId()==R.id.iCerrarSesion)
        {
            guardarEstadoButton();
            Intent intent = new Intent(InicioAdmin.this, Login.class);
            startActivity(intent);
        }

        if(item.getItemId()==R.id.iSalir)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void guardarEstadoButton()
    {
        //SharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        SharedPreferences preferences1 = getSharedPreferences("omitir_log_admin", Context.MODE_PRIVATE);
        boolean estado1 = false;
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putBoolean("estado_inicio_admin",estado1);
        editor1.commit();

        SharedPreferences preferences = getSharedPreferences("omitir_log", Context.MODE_PRIVATE);
        boolean estado = false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("estado_inicio",estado);
        editor.commit();


    }


}