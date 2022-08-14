package com.riobamba.geolam.modelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.riobamba.geolam.Listado;
import com.riobamba.geolam.ListadoUsuariosAdminControl;
import com.riobamba.geolam.ListarLugarUsuario;
import com.riobamba.geolam.LugarMapa;
import com.riobamba.geolam.R;
import com.riobamba.geolam.RegistroAdmin;
import com.riobamba.geolam.databinding.ActivityMapaBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ConexionMapa extends AppCompatActivity implements OnMapReadyCallback {
   // private GoogleMap mMap;
    private ActivityMapaBinding binding;
    Button btnListarLugarCercano;
    List<ListadoMapa> mapaList;

    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    Integer count =0 ;
    Double[] distancias;
    String[] lugarCerca;
    Button btnMapa;

    // Estado del Settings de verificación de permisos del GPS
    private static final int REQUEST_CHECK_SETTINGS = 102;

    // La clase FusedLocationProviderClient
    private FusedLocationProviderClient fusedLocationClient;

    // La clase LocationCallback se utiliza para recibir notificaciones de FusedLocationProviderApi
    // cuando la ubicación del dispositivo ha cambiado o ya no se puede determinar.
    private LocationCallback mlocationCallback;

    // La clase LocationSettingsRequest.Builder extiende un Object
    // y construye una LocationSettingsRequest.
    private LocationSettingsRequest.Builder builder;

    // La clase LocationRequest sirve para  para solicitar las actualizaciones
    // de ubicación de FusedLocationProviderApi
    public LocationRequest mLocationRequest;

    // Marcador para la ubicación del usuario
    Marker marker;

    // Mapa de Google
    private GoogleMap mMap;

    TextView lugarDistancia;
    Double distanciaCerca;


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapaList = new ArrayList<>();
        obtenerCoordenadas();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        btnMapa = findViewById(R.id.btnLugaresCercanos);
        btnMapa.setBackgroundColor(Color.argb(30,128,128,128));

        //llamada a la funcion para obtener las coordenadas
        btnListarLugarCercano = findViewById(R.id.btnLugaresCercanosMapa);
        btnListarLugarCercano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConexionMapa.this, LugarMapa.class);
                startActivity(intent);
            }
        });
        lugarDistancia = findViewById(R.id.tvDistancia);

        toolbar.show(this, "Lugares cercanos", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        //obtenerCoordenadas();
        //agregarMarcador(-1.6128,-78.85620);

        // Hago uso de FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Método para obtener la última ubicación del usuario
        obtenerUltimaUbicacion();
        // Con LocationCallback enviamos notificaciones de la ubicación del usuario
        mlocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                // Si no hay coordenadas de la ubicación del usuario le pasamos un return
                // Cuando obtenemos la coordenadas de ubicación del usuario, agregamos
                // un marcador para la ubicación del usuario con el método agregarMarcador()
                for (Location location : locationResult.getLocations()) {
                    agregarMarcador(location.getLatitude(),location.getLongitude());
                    //Log.e("Coordenadas: ", location.toString());
                }
                super.onLocationResult(locationResult);
            }
        };

        // Obtenemos actualizaciones de la ubicación del usuario
        mLocationRequest = createLocationRequest();

        // Construimos un LocationSettingsRequest
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        // Verificamos la configuración de los permisos de ubicación
        checkLocationSetting(builder);
        //habilitarUbicacion();



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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //obtenerCoordenadas();
       // agregarMarcador(-1.6128,-78.85620);
    }

    public void obtenerCoordenadas()
    {

        String url = WebService.urlRaiz + WebService.servicioListarLugaresMapa;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        count = array.length();
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


    private void agregarMarcador(double lat, double lng) {

        String url = WebService.urlRaiz + WebService.servicioListarLugaresMapa;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        Toast.makeText(this, "Aquí no es el error, sigue buscando :v", Toast.LENGTH_SHORT).show();
                        JSONArray array = new JSONArray(response);
                        count = array.length();
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






        Float latitud, longitud;
        String nombreLugar, direccionLugar;
        BitmapDescriptor puntero = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        BitmapDescriptor iconoPuntero = BitmapDescriptorFactory.fromResource(R.drawable.punterogeo);
        LatLng riobamba = new LatLng(-1.67435, -78.6483);
        LatLng coordenadas = new LatLng(lat, lng);//coordenadas de mi posicion
        String distanciaString;
        lugarCerca = new String[count];
        distancias = new Double[count];
        Proceso proceso = new Proceso();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            //if (marker != null) marker.remove();
            mMap.addMarker(new MarkerOptions()
                    .position(riobamba)
                    .icon(puntero)
                    .title("Riobamba"));

            for (int i = 0; i < count; i++)
            {

                latitud = mapaList.get(i).getLatitud();
                longitud = mapaList.get(i).getLongitud();
                nombreLugar = mapaList.get(i).getNombreLugar();
                direccionLugar = mapaList.get(i).getDireccionLugar();
                DecimalFormat formato1 = new DecimalFormat("#0.0");
                String distancia = formato1.format(proceso.obtenerDistancia(lat, lng, latitud,longitud));

                distanciaString = "Distancia: " + distancia + " " + "Km";

                distancias[i] = proceso.obtenerDistancia(lat, lng, latitud,longitud);
                lugarCerca[i] = nombreLugar;

                LatLng lugarMedico = new LatLng(latitud, longitud);
                mMap.addMarker(new MarkerOptions()
                        .position(lugarMedico)
                        .title(nombreLugar)
                        .snippet(distanciaString)
                        .icon(puntero)
                        .icon(iconoPuntero));

            }
           // Toast.makeText(this, count.toString(), Toast.LENGTH_SHORT).show();

            lugarDistancia.setText(proceso.verCercano(distancias, lugarCerca,count));

            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(riobamba, 13.5F);
            mMap.animateCamera(miUbicacion);
        }
    }

    public void guardarContador(Integer contador)
    {
        SharedPreferences preferences = getSharedPreferences("contador", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("contador", contador);
        editor.apply();
    }

    private void obtenerUltimaUbicacion() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                dialogoSolicitarPermisoGPS();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setSmallestDisplacement(30);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void checkLocationSetting(LocationSettingsRequest.Builder builder) {

        builder.setAlwaysShow(true);

        // Dentro de la variable 'cliente' iniciamos LocationServices, para los servicios de ubicación
        SettingsClient cliente = LocationServices.getSettingsClient(this);

        // Creamos una task o tarea para verificar la configuración de ubicación del usuario
        Task<LocationSettingsResponse> task = cliente.checkLocationSettings(builder.build());

        // Adjuntamos OnSuccessListener a la task o tarea

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // Si la configuración de ubicación es correcta,
                // se puede iniciar solicitudes de ubicación del usuario
                // mediante el método iniciarActualizacionesUbicacion() que crearé más abajo.
                iniciarActualizacionesUbicacion();
                //return;
            }
        });

        // Adjuntamos addOnCompleteListener a la task para gestionar si la tarea se realiza correctamente

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);

                    // En try podemos hacer 'algo', si la configuración de ubicación es correcta,
                    // Mostramos el diálogo llamando a startResolutionForResult()
                    // y es verificado el resultado en el método onActivityResult()

                    //response.getLocationSettingsStates().isLocationPresent();
                   // obtenerCoordenadas();
                    //count = 17;
                    //Objects.requireNonNull(response.getLocationSettingsStates()).isLocationUsable();


                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            // La configuración de ubicación no está satisfecha.
                            // Le mostramos al usuario un diálogo de confirmación de uso de GPS.
                            try {
                                // Transmitimos a una excepción resoluble.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;

                                // Mostramos el diálogo llamando a startResolutionForResult()
                                // y es verificado el resultado en el método onActivityResult().
                                resolvable.startResolutionForResult(
                                        ConexionMapa.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignora el error.
                            } catch (ClassCastException e) {
                                // Ignorar, aca podría ser un error imposible.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Si la configuración de ubicación no está satisfecha
                            // podemos hacer algo.
                            break;
                    }
                }
            }
        });

    }

    public void iniciarActualizacionesUbicacion() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                return;
            }
        }

        // Obtenemos la ubicación más reciente
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                mlocationCallback,
                null /* Looper */);
    }



    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mlocationCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // Se cumplen todas las configuraciones de ubicación.
                // La aplicación envía solicitudes de ubicación del usuario.
                iniciarActualizacionesUbicacion();
            } else {
                checkLocationSetting(builder);
            }
        }
    }

    private void dialogoSolicitarPermisoGPS(){
        if (ActivityCompat.checkSelfPermission(ConexionMapa.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ConexionMapa.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ConexionMapa.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
    }
    //Metodos para la barra inferior
    public void moverInicio(View view) //dirige al Inicio
    {
        toolbar.getActividad(this,this);
        toolbar.retornarInicio();
    }
    public void moverMapa(View view)    //dirige al mapa
    {
        toolbar.getActividad(this,this);
        toolbar.retornarMapa();
    }
    public void moverEspe(View view)    //dirige a la especialidad
    {
        toolbar.getActividad(this,this);
        toolbar.retornarEspecialidad();
    }

    //Funcion para rellenar el menu contextual en la parte superior -- proviene de la clase Toolbar
    @Override
    public boolean onCreateOptionsMenu(@NonNull android.view.Menu menu) {
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