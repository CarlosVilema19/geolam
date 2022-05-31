package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void moveToRegistrar(View view) {
        startActivity(new Intent(getApplicationContext(), registrar.class));
        finish();
    }

    public void moveToInicio(View view) {
        startActivity(new Intent(getApplicationContext(), Inicio.class));
        finish();
    }
    public void moveToListarLugarMedico(View view) {
        startActivity(new Intent(getApplicationContext(), ListarLugarMedico.class));
        finish();
    }

    public void moveToLugarMedico(View view) {
        startActivity(new Intent(getApplicationContext(), LugarMedico.class));
        finish();
    }
    public void moveToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        finish();
    }

}