package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class InfoApp extends AppCompatActivity {
    EditText ingresarSugerencia;
    Button btnEnviarSugerencia;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_app);
        ingresarSugerencia= findViewById(R.id.etSugerencia);
        btnEnviarSugerencia = findViewById(R.id.btnEnviarSuge);

        toolbar.show(this, "Acerca de la app", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        btnEnviarSugerencia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(ingresarSugerencia.getText().toString().equals(""))
                {
                    Toast.makeText(InfoApp.this, "Ingrese una sugerencia primero", Toast.LENGTH_SHORT).show();
                }
                else{
                    insertarOpinion();
                }
            }
        });

    }

    private int validarComentario(){
        int camposVacios=0;
        if(ingresarSugerencia.getText().toString().length()<=200) {
            if (Pattern.compile(" {3,}").matcher(ingresarSugerencia.getText().toString()).find()) {
                ingresarSugerencia.setError("¡Verifique que no haya más de dos espacios en blanco!");
                ingresarSugerencia.requestFocus();
            } else if(ingresarSugerencia.getText().toString().length()<3 && ingresarSugerencia.getText().toString().length()>0)
            {
                ingresarSugerencia.setError("El texto ingresado es demasiado corto");
                ingresarSugerencia.requestFocus();
            }else {
                camposVacios = 1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error!", Toast.LENGTH_SHORT).show();
            ingresarSugerencia.setError("Supera los caracteres permitidos (máximo 200)");
            ingresarSugerencia.requestFocus();
        }
        return camposVacios;
    }

    private void insertarOpinion() {

        if(validarComentario() == 1) {

            SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
            String email = preferences.getString("estado_correo", "");

            String url = WebService.urlRaiz + WebService.servicioIngresoSugerencia;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Gracias por su sugerencia", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error con el servidor", Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", email);
                parametros.put("sugerencia", ingresarSugerencia.getText().toString().trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        }

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
}
