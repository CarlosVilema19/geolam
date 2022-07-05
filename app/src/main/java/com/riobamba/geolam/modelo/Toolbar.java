package com.riobamba.geolam.modelo;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.riobamba.geolam.Inicio;
import com.riobamba.geolam.ListadoEspecialidad;
import com.riobamba.geolam.R;

import java.util.Objects;

public class Toolbar extends AppCompatActivity{

    public void show(AppCompatActivity activities,String titulo , Boolean flechaRegreso )
    {
        activities.setSupportActionBar(activities.findViewById(R.id.toolbar));
        Objects.requireNonNull(activities.getSupportActionBar()).setTitle(titulo);
        activities.getSupportActionBar().setDisplayHomeAsUpEnabled(flechaRegreso);

    }

    public void ejecutarItemSelected(Context ctx, MenuItem item)
    {
        if(item.getItemId()==R.id.iInicioMenu){
            Toast.makeText(ctx, "Inicio", Toast.LENGTH_SHORT).show();
        }
    }

}
