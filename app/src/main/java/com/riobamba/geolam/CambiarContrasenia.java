package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CambiarContrasenia extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    EditText txtContraseniaAntigua=null,
            txtContraseniaNueva, txtConfirmarContrasenia;
    TextInputLayout contNuevaContrasenia,contContraActual,contConfirmarContra;
    Button btnGuardarCambios, btnCancelar;
    ImageButton btnVerificarContraseniaAntigua;
    String compararContrasenia="";

    Button btnTutorial;



    int contraseniaCorrecta = 0;

    Toolbar toolbar = new Toolbar();
    public Class<Login> login = Login.class;
    public Context ctx;

    public void getContexto(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasenia);
        txtContraseniaAntigua = findViewById(R.id.etContraAntes);
        txtContraseniaNueva = findViewById(R.id.etNuevaContra);
        txtConfirmarContrasenia=findViewById(R.id.etConfirContra);
        contNuevaContrasenia= findViewById(R.id.contNuevaContrasenia);
        contContraActual=findViewById(R.id.contContraActual);
        contConfirmarContra=findViewById(R.id.contConfirmarContra);

        btnCancelar=findViewById(R.id.btnCancelar2);
        btnGuardarCambios=findViewById(R.id.btnGuardarCambiosUsu);
        btnVerificarContraseniaAntigua=findViewById(R.id.btnVerificarContrasenia);



        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contNuevaContrasenia.setError("");
                contConfirmarContra.setError("");
                modificarDatos();
            }
        });
        txtContraseniaAntigua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contContraActual.setErrorIconDrawable(null);
                contContraActual.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if(txtContraseniaAntigua.getText().toString().isEmpty()){
                    contContraActual.setErrorIconDrawable(null);
                    contContraActual.setError(null);
                }*/
            }
        });

        txtContraseniaNueva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contNuevaContrasenia.setErrorIconDrawable(null);
                contNuevaContrasenia.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if(txtContraseniaNueva.getText().toString().isEmpty()){
                    contNuevaContrasenia.setErrorIconDrawable(null);
                    contNuevaContrasenia.setError(null);
                }*/
            }
        });


        txtConfirmarContrasenia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contConfirmarContra.setErrorIconDrawable(null);
                contConfirmarContra.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //if(txtConfirmarContrasenia.getText().toString().isEmpty()){

                //}
            }
        });

        txtContraseniaAntigua.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //  if(txtContraseniaAntigua.requestFocus()){
                contConfirmarContra.setErrorIconDrawable(null);
                contConfirmarContra.setError(null);
                //}
            }
        });

        btnVerificarContraseniaAntigua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contContraActual.setErrorIconDrawable(null);
                contContraActual.setError(null);

                if(txtContraseniaAntigua.getText().toString().equals("")){
                    contContraActual.setErrorIconDrawable(null);
                    contContraActual.setError("Campo vacío");
                    contContraActual.requestFocus();
                }else{
                    cambioContrasenia();
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MostrarResultado();
        toolbar.show(this, "Contraseña", true);
    }

    public void modificarDatos() {
        if(cambioContrasenia()==2) {
            if (validarCaracteresContrasenia() == 1) {
                if(validarContrasenia()==1){
                    if(txtContraseniaAntigua.getText().toString().equals(txtContraseniaNueva.getText().toString())){
                        Toast.makeText(this, "Ingrese una contraseña distinta a la actual", Toast.LENGTH_SHORT).show();
                    }else{
                    String url = WebService.urlRaiz + WebService.servicioModificarContrasenia;
                    SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
                    String email = preferences.getString("estado_correo", "");
                    final ProgressDialog loading = ProgressDialog.show(this, "Actualizando la contraseña...", "Espere por favor");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //Descartar el diálogo de progreso
                            // final ProgressDialog loading = ProgressDialog.show(this, "Actualizando la información...", "Espere por favor");
                            loading.dismiss();

                            //Mostrando el mensaje de la respuesta
                            Toast.makeText(getApplicationContext(), "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(CambiarContrasenia.this, Configuraciones.class);
                            startActivity(intent);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Descartar el diálogo de progreso
                            loading.dismiss();
                            //Showing toast
                            Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //Convertir bits a cadena


                            Map<String, String> parametros = new HashMap<String, String>();
                            parametros.put("email", email);
                            parametros.put("contrasenia", getSHA256(txtContraseniaNueva.getText().toString().trim()));
                            return parametros;
                        }
                    };
                    //Creación de una cola de solicitudes
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    //Agregar solicitud a la cola
                    requestQueue.add(stringRequest);
                    }
                }
            }
        }

    }


    public void MostrarResultado()
    {

        //final ProgressDialog loading2 = ProgressDialog.show(this, "Obteniendo información...", "Espere por favor");

        //obtener el correo del usuario logueado
        SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
        String email = preferences.getString("estado_correo","");

        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioDatosPersonales;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                compararContrasenia = obj.getString("contrasenia");
                                // loading2.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", email);
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }
    public int cambioContrasenia(){
        int respuesta=0;

        if(txtContraseniaAntigua.getText().toString().equals("")){
            respuesta=1;
            Toast.makeText(this,"Campo vacío",Toast.LENGTH_SHORT);
            btnGuardarCambios.setEnabled(false);
        }
        else
        {
            if(validarContraseniaBD()==1){

                respuesta =2;
                txtContraseniaNueva.setEnabled(true);
                txtContraseniaAntigua.setEnabled(false);
                contNuevaContrasenia.setPasswordVisibilityToggleEnabled(true);
                btnVerificarContraseniaAntigua.setEnabled(false);
                contContraActual.setPasswordVisibilityToggleEnabled(false);
                contConfirmarContra.setPasswordVisibilityToggleEnabled(true);
                txtContraseniaNueva.requestFocus();
                txtConfirmarContrasenia.setEnabled(true);

            }
        }
        return respuesta;
    }
    public int validarContraseniaBD(){
        if(compararContrasenia.toString().equals(getSHA256(txtContraseniaAntigua.getText().toString()))){
            btnGuardarCambios.setEnabled(true);
            contraseniaCorrecta= 1;

            txtContraseniaNueva.getBackground().setColorFilter(Color.argb(210,255,255,255), PorterDuff.Mode.SRC_ATOP);

            txtConfirmarContrasenia.getBackground().setColorFilter(Color.argb(210,255,255,255), PorterDuff.Mode.SRC_ATOP);
        }
        else {
            contContraActual.setError("Contraseña incorrecta");
            contContraActual.requestFocus();
            contContraActual.setErrorIconDrawable(null);
        }

        return contraseniaCorrecta;
    }
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=*])"+     // al menos un caracter especial
                    "(?=\\S+$)" +            // sin espacios en blanco
                    ".{10,16}" +                // al menos 10 caracteres
                    "$");

    private int validarCaracteresContrasenia(){
        int datCorrecto=0;
        String passwordInput = txtContraseniaNueva.getText().toString().trim();
        if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            Toast.makeText(CambiarContrasenia.this, "La contraseña es demasiado débil o fuera de rango", Toast.LENGTH_SHORT).show();
            contNuevaContrasenia.setError("Ingrese de 10-16 caracteres (Agregar un caracter especial y sin espacios)");
            contNuevaContrasenia.requestFocus();
            contNuevaContrasenia.setErrorIconDrawable(null);
        }
        else{
            //pass.setError("Contraseña válida");
            datCorrecto = 1;
        }
        return datCorrecto;
    }

    private int validarContrasenia(){
        int datCorrecto=0;
        String contrasenia = txtContraseniaNueva.getText().toString();
        String confirmContrasenia = txtConfirmarContrasenia.getText().toString();

        // Comparar si son iguales
        if (contrasenia.equals(confirmContrasenia)) {
            //Toast.makeText(registrar.this, "La contraseñas coinciden", Toast.LENGTH_SHORT).show();
            datCorrecto=1;
        } else {
            // Si no, entonces indicamos el error y damos focus
            contConfirmarContra.setError("Las contraseñas no coinciden, ingrese nuevamente");
            contConfirmarContra.requestFocus();
            contConfirmarContra.setErrorIconDrawable(null);
            // txtConfirmarContrasenia.requestFocus();
            datCorrecto=0;
        }
        return datCorrecto;
    }

    public String getSHA256(String data) {
        return Hashing.sha256().hashString(data, StandardCharsets.UTF_8).toString();
    }

    //Funcion para rellenar el menu contextual en la parte superior -- proviene de la clase Toolbar
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
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

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}