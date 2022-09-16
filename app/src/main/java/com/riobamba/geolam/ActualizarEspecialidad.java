package com.riobamba.geolam;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ActualizarEspecialidad extends AppCompatActivity {

    EditText txtName;
    Button btnGuardarCambios, btnCancelar, btnConsultar;
    Toolbar toolbar = new Toolbar();
    String compararNombre="", mensajeExiste = "¡Esta especialidad ya existe!";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_especialidad);

        toolbar.show(this, "Actualizar", false);

        //Variables
        txtName =findViewById(R.id.etEspecialidad);
        btnGuardarCambios=findViewById(R.id.btnAgregarEspecialidad);
        btnCancelar=findViewById(R.id.btnCancelarEspe);
        btnConsultar=findViewById(R.id.btnEspecialidadAgregada);
        ListadoLugarAdmin actualizarEspe = (ListadoLugarAdmin) getIntent().getSerializableExtra("ActualizarEspe");
        MostrarResultado(actualizarEspe);

        btnConsultar.setVisibility(View.GONE);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(verificaSimilitud() == 1))
                {
                    finish();
                    Intent intent = new Intent(ActualizarEspecialidad.this, EspecialidadListadoAdmin.class);
                    startActivity(intent);
                }
                else
                {
                    int icon  = R.drawable.peligro;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarEspecialidad.this);
                    builder.setIcon(icon)
                            .setTitle("Cancelar")
                            .setMessage("Se perderán los cambios realizados. ¿Desea continuar?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    Intent intent = new Intent(ActualizarEspecialidad.this, EspecialidadListadoAdmin.class);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            }
        });

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarEspecialidad(actualizarEspe);

            }
        });

    }

    private void validarEspecialidad(ListadoLugarAdmin actualizarEspe ){
        if (verificaSimilitud() == 1) {
            if (validarCampos() == 1) {
                String url = WebService.urlRaiz + WebService.servicioValidarExistenciaEspecialidad;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response ->
                        {
                            try {
                                JSONObject object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                                String existencia = object.getString("valida");
                                if (existencia.equals("existe")) {
                                    txtName.setError(mensajeExiste);
                                    txtName.requestFocus();
                                } else {
                                    modificarDatos(actualizarEspe);
                                }
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }, error -> {
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new HashMap<String, String>();
                        parametros.put("descripcion_especialidad", txtName.getText().toString().toUpperCase().trim());
                        return parametros;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        }else
        {
            Toast.makeText(this,"No se ha realizado ningún cambio",Toast.LENGTH_SHORT).show();
        }
    }

    public void MostrarResultado(ListadoLugarAdmin actualizarEspe)
    {
        final ProgressDialog loading2 = ProgressDialog.show(this, "Obteniendo información...", "Espere por favor");

        String id_espe = actualizarEspe.getId().toString();

        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioListarEspeActual;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading2.dismiss();
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                txtName.setText(obj.getString("DESCRIPCION_ESPECIALIDAD"));
                                compararNombre = obj.getString("DESCRIPCION_ESPECIALIDAD");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id_espe", id_espe);
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void modificarDatos(ListadoLugarAdmin actualizarEspe) {
        String id_espe = actualizarEspe.getId().toString();
        String url = WebService.urlRaiz + WebService.servicioActualizarEspecialidad;
        final ProgressDialog loading = ProgressDialog.show(this, "Actualizando la información...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                //Mostrando el mensaje de la respuesta
                Toast.makeText(getApplicationContext(), "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(ActualizarEspecialidad.this, EspecialidadListadoAdmin.class);
                startActivity(intent);
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                //Showing toast
                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("especialidad", txtName.getText().toString().trim().toUpperCase());
                parametros.put("id_espe", id_espe);
                return parametros;
            }
        };
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    private int verificaSimilitud() {

        int band=0;
        if(compararNombre.trim().equals(txtName.getText().toString().trim())) {
            band=0;
        }
        else if(compararNombre.trim().equals(txtName.getText().toString().trim()))
        {
            band=0;
        }
        else if(!compararNombre.trim().equals(txtName.getText().toString()))
        {
            band=1;
        }

        return band;
    }

    public int validarCampos(){

        int respuesta =0;

        if(txtName.getText().toString().equals(""))
        {
            Toast.makeText(ActualizarEspecialidad.this, "Campos vacíos. Por favor ingrese datos", Toast.LENGTH_SHORT).show();
            txtName.setError("Ingrese el nombre");
        }

        else if(!txtName.getText().toString().equals("")){
            respuesta=2;
        }
        if(respuesta==2) {
            if(validarNombre()==1) {
                respuesta=1;
            }
        }
        return respuesta;
    }

    private int validarNombre(){

        int camposVacios=0;
        if(txtName.getText().toString().length()<=50) {
            if (txtName.getText().toString().equals("")) {
                txtName.setError("¡Ingrese una Especialidad!");
                txtName.requestFocus();
            } else if (Pattern.compile(" {2,}").matcher(txtName.getText().toString()).find()) {
                txtName.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtName.requestFocus();
            } else if(txtName.getText().toString().length()<5 && txtName.getText().toString().length()>0)
            {
                txtName.setError("Nombre demasiado corto. (Mínimo 5 caracteres)");
                txtName.requestFocus();
            }else {
                camposVacios = 1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Especialidad", Toast.LENGTH_SHORT).show();
            txtName.setError("Nombre demasiado largo. (Máximo 80 caracteres)");
            txtName.requestFocus();
        }
        return camposVacios;
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
    @Override public void onBackPressed() { }  //Anula la flecha de regreso del telefono
}
