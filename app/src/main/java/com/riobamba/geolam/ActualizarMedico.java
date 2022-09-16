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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ActualizarMedico extends AppCompatActivity {

    EditText txtName, txtApe, txtDescrip;
    Button btnGuardarCambios, btnCancelar, btnConsultar;
    Toolbar toolbar = new Toolbar();
    String compararNombre="",compararApellido="",compararDescripcion="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_medico);

        toolbar.show(this, "Actualizar", false);

        //Variables
        txtName =findViewById(R.id.ednombreMedico);
        txtApe =findViewById(R.id.edapellidoMedico);
        txtDescrip =findViewById(R.id.eddescripcionMedico);
        btnGuardarCambios=findViewById(R.id.btnAgregarMedico);
        btnCancelar=findViewById(R.id.btnCancelarMedi);
        btnConsultar=findViewById(R.id.btnVerAgregados);
        ListadoLugarAdmin actualizarMedi = (ListadoLugarAdmin) getIntent().getSerializableExtra("ActualizarMedi");
        MostrarResultado(actualizarMedi);

        btnConsultar.setVisibility(View.GONE);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(verificaSimilitud() == 1))
                {
                    finish();
                    Intent intent = new Intent(ActualizarMedico.this, MedicoAdmin.class);
                    startActivity(intent);
                }
                else
                {
                    int icon  = R.drawable.peligro;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarMedico.this);
                    builder.setIcon(icon)
                            .setTitle("Cancelar")
                            .setMessage("Se perderán los cambios realizados. ¿Desea continuar?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    Intent intent = new Intent(ActualizarMedico.this, MedicoAdmin.class);
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
                modificarDatos(actualizarMedi);

            }
        });

    }

    public void MostrarResultado(ListadoLugarAdmin actualizarMedi)
    {
        final ProgressDialog loading2 = ProgressDialog.show(this, "Obteniendo información...", "Espere por favor");

        String id_medi = actualizarMedi.getId().toString();

        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioListarMediActual;
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
                                txtName.setText(obj.getString("NOMBRE_MEDICO"));
                                compararNombre = obj.getString("NOMBRE_MEDICO");
                                txtApe.setText(obj.getString("APELLIDO_MEDICO"));
                                compararApellido = obj.getString("APELLIDO_MEDICO");
                                txtDescrip.setText(obj.getString("DESCRIPCION_MEDICO"));
                                compararDescripcion = obj.getString("DESCRIPCION_MEDICO");
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
                parametros.put("id_medico", id_medi);
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void modificarDatos(ListadoLugarAdmin actualizarMedico) {
        String id_medi = actualizarMedico.getId().toString();
        if (validarCampos() == 1) {
            if(verificaSimilitud()==1) {
                String url = WebService.urlRaiz + WebService.servicioActualizarMedico;
                final ProgressDialog loading = ProgressDialog.show(this, "Actualizando la información...", "Espere por favor");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(getApplicationContext(), "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(ActualizarMedico.this, MedicoAdmin.class);
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
                        parametros.put("nombre", txtName.getText().toString().trim().toUpperCase());
                        parametros.put("apellido", txtApe.getText().toString().trim().toUpperCase());
                        parametros.put("descripcion", txtDescrip.getText().toString().trim());
                        parametros.put("id_medico", id_medi);
                        return parametros;
                    }
                };
                //Creación de una cola de solicitudes
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                //Agregar solicitud a la cola
                requestQueue.add(stringRequest);

                // ivFotoP.setTag("bg1");
            }else
            {
                Toast.makeText(this,"No se ha realizado ningún cambio",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int verificaSimilitud() {

        int band=0;
         if(!compararNombre.trim().equals(txtName.getText().toString().trim())||
                 !compararApellido.trim().equals(txtApe.getText().toString().trim()) ||
                 !compararDescripcion.trim().equals(txtDescrip.getText().toString().trim()))
        {
            band=1;
        }

        return band;
    }

    public int validarCampos(){

        int respuesta =0;

        if(txtName.getText().toString().equals("") || txtApe.getText().toString().equals(""))
        {
            Toast.makeText(ActualizarMedico.this, "Campos vacíos. Por favor ingrese datos", Toast.LENGTH_SHORT).show();
            txtApe.setError("Ingrese el apellido");
            txtName.setError("Ingrese el nombre");
        }
        else if(validarNombre()==1 && validarApellido()==1 && validarDescripcion()==1) {
                respuesta=1;
            }

        return respuesta;
    }

    private int validarNombre(){

        int camposVacios=0;
        if(txtName.getText().toString().length()<=30) {
            if (txtName.getText().toString().equals("")) {
                txtName.setError("¡Ingrese un Nombre!");
                txtName.requestFocus();
            } else if (Pattern.compile(" {2,}").matcher(txtName.getText().toString()).find()) {
                txtName.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtName.requestFocus();
            } else if(txtName.getText().toString().length()<3 && txtName.getText().toString().length()>0)
            {
                txtName.setError("Nombre demasiado corto. (Mínimo 3 caracteres)");
                txtName.requestFocus();
            }else {
                camposVacios = 1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Especialidad", Toast.LENGTH_SHORT).show();
            txtName.setError("Nombre demasiado largo. (Máximo 30 caracteres)");
            txtName.requestFocus();
        }
        return camposVacios;
    }

    private int validarApellido(){
        int camposVacios=0;
        if(txtApe.getText().toString().length()<=30) {
            if (txtApe.getText().toString().equals("")) {
                txtApe.setError("¡Ingrese un Apellido!");
                txtApe.requestFocus();
            } else if (Pattern.compile(" {2,}").matcher(txtApe.getText().toString()).find()) {
                txtApe.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtApe.requestFocus();
            } else if(txtApe.getText().toString().length()<3 && txtApe.getText().toString().length()>0)
            {
                txtApe.setError("Apellido demasiado corto. (Mínimo 3 caracteres)");
                txtApe.requestFocus();
            }else {
                camposVacios = 1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Especialidad", Toast.LENGTH_SHORT).show();
            txtApe.setError("Nombre demasiado largo. (Máximo 30 caracteres)");
            txtApe.requestFocus();
        }
        return camposVacios;
    }

    private int validarDescripcion(){
        int camposVacios=0;
        if(txtDescrip.getText().toString().length()<=150) {
            if (Pattern.compile(" {2,}").matcher(txtDescrip.getText().toString()).find()) {
                txtDescrip.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtDescrip.requestFocus();
            } else if(txtDescrip.getText().toString().length()<5 && txtDescrip.getText().toString().length()>0)
            {
                txtDescrip.setError("Descripción demasiada corta. (Mínimo 5 caracteres)");
                txtDescrip.requestFocus();
            }else {
                camposVacios = 1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Especialidad", Toast.LENGTH_SHORT).show();
            txtDescrip.setError("Descripción demasiada corta. (Máximo 150 caracteres)");
            txtDescrip.requestFocus();
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
