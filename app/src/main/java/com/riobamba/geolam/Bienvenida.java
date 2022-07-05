package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;

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
        SharedPreferences preferences = getSharedPreferences("omitir_log", Context.MODE_PRIVATE);
    if(!preferences.getBoolean("estado_inicio", false))
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login = new Intent(getApplicationContext(),Login.class);
                startActivity(login);
                finish();
            }
        },2500);
    }else{
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent inicio = new Intent(getApplicationContext(),Inicio.class);
                startActivity(inicio);
                finish();
            }
        },2500);
    }






    }
}