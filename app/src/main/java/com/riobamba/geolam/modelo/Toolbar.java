package com.riobamba.geolam.modelo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.riobamba.geolam.Bienvenida;
import com.riobamba.geolam.DatosPersonalesUsu;
import com.riobamba.geolam.EspecialidadListadoUsuario;
import com.riobamba.geolam.Inicio;
import com.riobamba.geolam.InicioAdmin;
import com.riobamba.geolam.Listado;
import com.riobamba.geolam.ListadoEspecialidad;
import com.riobamba.geolam.Login;
import com.riobamba.geolam.R;

import java.util.Objects;

public class Toolbar extends AppCompatActivity{

    public Class<Bienvenida> inicioClass = Bienvenida.class;
    public Class<ConexionMapa> conexionMapaClass = ConexionMapa.class;
    public Class<EspecialidadListadoUsuario> listadoEspecialidadClass = EspecialidadListadoUsuario.class;
    public Class<DatosPersonalesUsu> datosUsuClass = DatosPersonalesUsu.class;
    public Class<Login> login = Login.class;
    public Context ctx;

    public void getContexto(Context ctx)
    {
        this.ctx = ctx;
    }

    public void show(AppCompatActivity activities,String titulo , Boolean flechaRegreso )
    {
        activities.setSupportActionBar(activities.findViewById(R.id.toolbar));
        Objects.requireNonNull(activities.getSupportActionBar()).setTitle(titulo);
        activities.getSupportActionBar().setDisplayHomeAsUpEnabled(flechaRegreso);

    }

    public void ejecutarItemSelected(MenuItem item, AppCompatActivity activities)
    {
        if(item.getItemId()==R.id.iInicioMenu){
            activities.startActivity(new Intent (ctx,inicioClass));
        }
        if(item.getItemId()==R.id.iSalir){
            salir(activities);
        }
        if(item.getItemId()==R.id.iCerrarSesion)
        {
            guardarEstadoButton(activities);
            activities.startActivity(new Intent(ctx, login));
        }
        if(item.getItemId()==R.id.iMisDatos)
        {
            activities.startActivity(new Intent(ctx, datosUsuClass));
        }

        if(item.getItemId()==android.R.id.home)
        {
            activities.finish();
        }

    }


    /*public void ejecutarItemAdmin(Context ctx, MenuItem item, AppCompatActivity activities)
    {
        if(item.getItemId()==R.id.iInicioMenu){
            activities.startActivity(new Intent (ctx,inicioClass));
        }
        if(item.getItemId()==R.id.iSalir){
            salir(activities);
        }
        if(item.getItemId()==R.id.iCerrarSesion)
        {
            guardarEstadoButton(activities);
            activities.startActivity(new Intent(ctx, login));
        }
        if(item.getItemId()==R.id.iMisDatos)
        {
            activities.startActivity(new Intent(ctx, datosUsuClass));
        }
    }*/


    public Intent retornarInicio() {return new Intent(ctx, inicioClass);}
    public Intent retornarMapa() {return new Intent(ctx, conexionMapaClass);}
    public Intent retornarEspecialidad() {return new Intent(ctx, listadoEspecialidadClass);}


    public void salir (AppCompatActivity activities) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setMessage("¿Desea salir de Geolam?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activities.startActivity(intent);
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
    }


    public void guardarEstadoButton(AppCompatActivity activities)
    {
        SharedPreferences preferences = activities.getSharedPreferences("omitir_log", Context.MODE_PRIVATE);
        boolean estado = false;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("estado_inicio",estado);
        editor.commit();

        SharedPreferences preferences1 = activities.getSharedPreferences("omitir_log_admin", Context.MODE_PRIVATE);
        boolean estado1 = false;
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putBoolean("estado_inicio_admin",estado1);
        editor1.commit();
    }

}
