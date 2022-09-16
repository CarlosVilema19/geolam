package com.riobamba.geolam.modelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.riobamba.geolam.LugarMapa;
import com.riobamba.geolam.MapaLugarCerca;
import com.riobamba.geolam.R;
import com.riobamba.geolam.databinding.ActivityMapaBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ConexionMapa extends AppCompatActivity implements OnMapReadyCallback {
    Button btnListarLugarCercano;
    List<ListadoMapa> mapaList;
    String[] distanc;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    Integer count =0, count2 = 0; // contadores para verificar si la conexion se establece correctamente
    Double[] distancias;
    String[] lugarCerca;
    Button btnMapa, btnMapaPul;
    private GoogleMap mMap;
    TextView lugarDistancia;
    ProgressDialog loading; //Mensaje de carga en el mapa


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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapaBinding binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapaList = new ArrayList<>();
        obtenerCoordenadas();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //Senalar el icono donde pulsa en el menu inferior
        btnMapa = findViewById(R.id.btnLugaresCercanos2);
        btnMapaPul = findViewById(R.id.btnLugaresCercanos);
        toolbar.obtenerBotIni(btnMapa,btnMapaPul);


        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConexionMapa.this, ConexionMapa.class);
                startActivity(intent);
                finish();
            }
        });
        //llamada a la funcion para obtener las coordenadas
        btnListarLugarCercano = findViewById(R.id.btnLugaresCercanosMapa);
        btnListarLugarCercano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTitulo("Lugares más cercanos");
                Intent intent = new Intent(ConexionMapa.this, MapaLugarCerca.class);
                intent.putExtra("MapaLugarCerca", distanc);
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

                //Verifica si la conexion es ccorrecta
                for (Location location : locationResult.getLocations()) {
                    loading = ProgressDialog.show(ConexionMapa.this, "Cargando...", "Espere por favor");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if(count2 == 0){
                                Toast.makeText(ConexionMapa.this, "Se produjo un error, intente nuevamente", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                finish();
                            }else
                            {
                                agregarMarcador(location.getLatitude(),location.getLongitude());
                            }
                        }
                    },1500);
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
    public void onMapReady(@NonNull GoogleMap googleMap) {mMap = googleMap;}

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
                        count2 =1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void agregarMarcador(double lat, double lng) {
        loading.dismiss();
        Toast.makeText(this, "Carga exitosa", Toast.LENGTH_SHORT).show();
        btnListarLugarCercano.setVisibility(View.VISIBLE);
        Float latitud, longitud;
        String nombreLugar, direccionLugar;
        BitmapDescriptor puntero = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        BitmapDescriptor iconoPuntero = BitmapDescriptorFactory.fromResource(R.drawable.punterogeo);
        LatLng riobamba = new LatLng(-1.67435, -78.6483);
        LatLng coordenadas = new LatLng(lat, lng);//coordenadas de mi posicion
        String distanciaString;
        lugarCerca = new String[count];
        distancias = new Double[count];
        distanc = new String[count];
        Proceso proceso = new Proceso();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
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
                //pasa las distancias a la siguiente actividad
                distanc[i] = distancia;

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
            lugarDistancia.setText(proceso.verCercano(distancias, lugarCerca,count));

            String text = proceso.verDistanciaCercano(distancias,count).toString();
            if(proceso.verDistanciaCercano(distancias,count)>5)
            {
                int icon  = R.drawable.peligro;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(icon)
                        .setTitle("Aviso")
                        .setMessage("El lugar de atención médica más cercano se encuentra a una distancia mayor a los 5 Km ¿Desea continuar?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                builder.show();
            }

            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(riobamba, 13.5F);
            mMap.animateCamera(miUbicacion);
        }
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
                    // En try ponemos hacer 'algo', si la configuración de ubicación es correcta,
                    // Mostramos el diálogo llamando a startResolutionForResult()
                    // y es verificado el resultado en el método onActivityResult()

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

    //Controla si acepta o no los permisos de ubicacion para activar el gps
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // Se cumplen todas las configuraciones de ubicación.
                // La aplicación envía solicitudes de ubicación del usuario.
                iniciarActualizacionesUbicacion();
                //Toast.makeText(ConexionMapa.this, "Si llega", Toast.LENGTH_SHORT).show();
            } else {
                //checkLocationSetting(builder);
                int icon  = R.drawable.peligro;
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setIcon(icon)
                        .setTitle("Aviso").setMessage("Para hacer uso del mapa debe activar la ubicación")
                        .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkLocationSetting(builder);
                            }
                        });
                builder2.show();
            }
        }
    }
    //Solicitar permisos de acceso cuando se usa por primera vez  la app
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

    //Funcion para guardar el titulo de la actividad
    public void guardarTitulo(String titulo)
    {
        SharedPreferences preferences = getSharedPreferences("tituloRefe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tituloRefe",titulo);
        editor.apply();
    }
}