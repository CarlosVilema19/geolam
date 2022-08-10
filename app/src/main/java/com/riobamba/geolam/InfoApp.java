package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.riobamba.geolam.modelo.Toolbar;

public class InfoApp extends AppCompatActivity {
    Toolbar toolbar = new Toolbar();
    Button btnEnviar;
    EditText sugerencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_app);
        toolbar.show(this, "Acerca la app", true);
        btnEnviar = findViewById(R.id.btnEnviarSuge);
        sugerencia = findViewById(R.id.etSugerencia);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(InfoApp.this, LugarMapa.class);
                //startActivity(intent);
                if(sugerencia.getText().toString().equals(""))
                {
                    Toast.makeText(InfoApp.this, "Ingrese una sugerencia primero", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(InfoApp.this, "Se ha enviado tu sugerencia", Toast.LENGTH_SHORT).show();
                    sugerencia.setText("");
                }

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
        toolbar.getContexto(this);
        toolbar.ejecutarItemSelected(item, this);

        return super.onOptionsItemSelected(item);
    }


}