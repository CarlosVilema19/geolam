package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Bienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences preferences = getSharedPreferences("omitir_log", Context.MODE_PRIVATE);
        SharedPreferences preferencesAdmin = getSharedPreferences("omitir_log_admin", Context.MODE_PRIVATE);

        if(preferences.getBoolean("estado_inicio", true))
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  Intent inicio = new Intent(getApplicationContext(),Inicio.class);
                Intent inicio = new Intent(getApplicationContext(),Listado.class);
                startActivity(inicio);
                finish();
            }
        },100);
    }else if(preferencesAdmin.getBoolean("estado_inicio_admin", true)){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent inicioAdmin = new Intent(getApplicationContext(),InicioAdmin.class);
                startActivity(inicioAdmin);
                finish();
            }
        },100);

    }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent login = new Intent(getApplicationContext(),Login.class);
                    startActivity(login);
                    finish();
                }
            },2000);
        }






    }
}