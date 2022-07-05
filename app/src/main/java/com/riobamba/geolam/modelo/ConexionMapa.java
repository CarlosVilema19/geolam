package com.riobamba.geolam.modelo;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.riobamba.geolam.GestionUsuarios;
import com.riobamba.geolam.InicioAdmin;
import com.riobamba.geolam.Listado;
import com.riobamba.geolam.LugarMapa;
import com.riobamba.geolam.R;
import com.riobamba.geolam.databinding.ActivityMapaBinding;

import java.util.List;

public class ConexionMapa extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapaBinding binding;
    Button btnListarLugarCercano;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);



        btnListarLugarCercano = findViewById(R.id.btnLugaresCercanosMapa);
        btnListarLugarCercano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConexionMapa.this, LugarMapa.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //ListadoMapa listadoMapa = mapaList.get(0);

        // Add a marker in Sydney and move the camera
        /*LatLng riobamba = new LatLng(listadoMapa.getLatitud(), listadoMapa.getLongitud());
        mMap.addMarker(new MarkerOptions().position(riobamba).title(listadoMapa.getNombreLugar()).snippet(listadoMapa.getDireccionLugar()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
*/
        LatLng metropolitana = new LatLng(-1.66872,  -78.6488);
        mMap.addMarker(new MarkerOptions().position(metropolitana).title("Hospital General Clinica Metroplitana").snippet("Junín entre España & García Moreno").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(metropolitana,14));
    }
}