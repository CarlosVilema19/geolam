package com.riobamba.geolam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.navigation.NavigationView;
import com.riobamba.geolam.modelo.ConexionMapa;
import com.riobamba.geolam.modelo.Toolbar;

public class Inicio extends AppCompatActivity {
    Button btnlugarmedico, btnListadoLugar, btnEspecialidades, btnCerrar;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    public static final Integer btnListadoMapa = R.layout.activity_listado_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btnlugarmedico = findViewById(R.id.btnlugarescercanos);
        btnListadoLugar = findViewById(R.id.btnlugaresmedicos);
        btnEspecialidades = findViewById(R.id.btnespecialidades);
        btnCerrar = findViewById(R.id.btnCerrarSesion);

        toolbar.show(this, "Geolam", false); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

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
                startActivity(intent);
            }
        }
        );
        btnEspecialidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, ListadoEspecialidad.class);
                startActivity(intent);}
        }
        );

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEstadoButton();
                Intent intent = new Intent(Inicio.this, Login.class);
                startActivity(intent);
            }
        }
        );
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
        toolbar.ejecutarItemSelected(this,item);
        if(item.getItemId()==R.id.iCerrarSesion)
        {
            guardarEstadoButton();
            Intent intent = new Intent(Inicio.this, Login.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardarEstadoButton()
    {
        //SharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        SharedPreferences preferences = getSharedPreferences("omitir_log", Context.MODE_PRIVATE);
        boolean estado = false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("estado_inicio",estado);
        editor.commit();

    }
    //Metodos para la barra inferior
    public void moverInicio(View view) //dirige al Inicio
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarInicio());
    }
    public void moverMapa(View view)    //dirige al mapa
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarMapa());
    }
    public void moverEspe(View view)    //dirige a la especialidad
    {
        toolbar.getContexto(this);
        startActivity(toolbar.retornarEspecialidad());
    }


}
