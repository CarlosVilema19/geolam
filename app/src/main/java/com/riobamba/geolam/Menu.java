package com.riobamba.geolam;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.riobamba.geolam.modelo.ConexionMapa;


public class Menu extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    MenuView.ItemView itemInicioMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.menu);
        navigationView = findViewById(R.id.navigationViewer);

        actionBarDrawerToggle = new ActionBarDrawerToggle( this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        
        //itemInicioMenu = findViewById(R.id.btnInicioMenu);

        /*@Override
        public boolean onOptionsItemSelected(itemInicioMenu) {
            // Handle item selection
            switch (menu.getItemId()) {
                case R.id.btnInicioMenu:
                    newGame();
                    return true;
                case R.id.help:
                    showHelp();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
*/


    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        itemInicioMenu = findViewById(R.id.btnInicioMenu);
        switch (item.getItemId()) {
            case R.id.btnInicioMenu:
                Intent intent = new Intent(Menu.this, Listado.class);
                startActivity(intent);
                return true;
            case R.id.btnMisDatos:
                Intent intent2 = new Intent(Menu.this, Inicio.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

}
