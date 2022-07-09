package com.riobamba.geolam.modelo;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.riobamba.geolam.LugarMapa;
import com.riobamba.geolam.OpinionListado;
import com.riobamba.geolam.R;
import com.riobamba.geolam.databinding.ActivityMapaBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConexionMapa extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapaBinding binding;
    Button btnListarLugarCercano;
    List<ListadoMapa> mapaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //llamada a la funcion para obtener las coordenadas
        mapaList = new ArrayList<>();
        obtenerCoordenadas();

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
        Integer i = 0;
        Float latitud, longitud;
        String nombreLugar, direccionLugar;
        LatLng riobamba = new LatLng( -1.67435, -78.6483);
        mMap.addMarker(new MarkerOptions().position(riobamba).title("Riobamba").snippet("Riobamba").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        for (i = 0; i<5;i++)
        {
            latitud = mapaList.get(i).getLatitud();
            longitud = mapaList.get(i).getLongitud();
            nombreLugar = mapaList.get(i).getNombreLugar();
            direccionLugar = mapaList.get(i).getDireccionLugar();

            LatLng metropolitana = new LatLng(latitud, longitud);
            mMap.addMarker(new MarkerOptions().position(metropolitana).title(nombreLugar).snippet(direccionLugar).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(riobamba, 14));
    }

    public void obtenerCoordenadas()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = WebService.urlRaiz + WebService.servicioListarLugaresMapa;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            mapaList.add(new ListadoMapa(
                                    (float) obj.getDouble("latitud"),
                                    (float) obj.getDouble("longitud"),
                                    obj.getString("nombre_lugar"),
                                    obj.getString("direccion")
                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);
    }


}