package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EliminarCuenta extends AppCompatActivity {

    Toolbar toolbar = new Toolbar();
    public Class<Login> login = Login.class;
    public Context ctx;

    public void getContexto(Context ctx)
    {
        this.ctx = ctx;
    }
    Button btnEliminarCuenta_;
    EditText etContraActual2;
    TextInputLayout contContraActual2;
    int contraseniaCorrecta = 0;

    int confirmacion=0;

    String compararContrasenia="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cuenta);

        toolbar.show(this, "Eliminar cuenta", true);
        btnEliminarCuenta_=findViewById(R.id.btnEliminarCuenta_);
        etContraActual2=findViewById(R.id.etContraActual2);
        contContraActual2=findViewById(R.id.contContraActual2);
        obtenerDatosUsuario();
        btnEliminarCuenta_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarContraBD()==1) {
                    contContraActual2.setErrorIconDrawable(null);
                    contContraActual2.setError(null);
                    //Toast.makeText( EliminarCuenta.this,"Las contrasenia es correcta", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(EliminarCuenta.this).setIcon(
                            getDrawable(R.drawable.peligro)
                            )
                           // android.R.drawable.ic_dialog_alert)
                            .setTitle("Eliminar cuenta")
                            .setMessage("¿Estás seguro que quieres eliminar esta cuenta?")
                            .setPositiveButton(
                            "Si",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //listener.removeSafezoneFromServer(center, radius, index);
                                   // confirmacion=1;
                                    eliminarUsuario();
                                }
                            })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //listener.removeSafezoneFromServer(center, radius, index);
                                            //confirmacion=1;
                                            finish();
                                           // eliminarUsuario();
                                        }
                                    }
                                    )
                            .show();
                    //AppCompatActivity appCompatActivity= new AppCompatActivity();
                   // eliminarUsuario();
                    //onCreateDialog();
                }

            }
        });

        etContraActual2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                contContraActual2.setErrorIconDrawable(null);
                contContraActual2.setError(null);
            }
        });

    }

    private void eliminarUsuario() {

       /* AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage("¿Está seguro de eliminar la cuenta?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {*/
                        /*Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activities.startActivity(intent);*/
                        //confirmacion=1;
                   /* }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        confirmacion=0;
                        dialog.dismiss();
                    }
                });
        builder.show();*/

       // onCreateDialog();

        //if(confirmacion==1) {
            String url = WebService.urlRaiz + WebService.servicioEliminarUsuarios;
            SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
            String email = preferences.getString("estado_correo", "");
            final ProgressDialog loading = ProgressDialog.show(this, "Eliminando cuenta...", "Espere por favor");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Descartar el diálogo de progreso
                    // final ProgressDialog loading = ProgressDialog.show(this, "Actualizando la información...", "Espere por favor");
                    loading.dismiss();

                    //Mostrando el mensaje de la respuesta
                    Toast.makeText(getApplicationContext(), "Se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                    SharedPreferences preferences = getSharedPreferences("omitir_log", Context.MODE_PRIVATE);
                    boolean estado = false;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("estado_inicio",estado);
                    editor.apply();

                    SharedPreferences preferences1 = getSharedPreferences("omitir_log_admin", Context.MODE_PRIVATE);
                    boolean estado1 = false;
                    SharedPreferences.Editor editor1 = preferences1.edit();
                    editor1.putBoolean("estado_inicio_admin",estado1);
                    editor1.apply();
                    Intent intent = new Intent(EliminarCuenta.this, Login.class);
                    startActivity(intent);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Descartar el diálogo de progreso
                    loading.dismiss();
                    //Showing toast
                    Toast.makeText(getApplicationContext(), "ERROR" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Convertir bits a cadena


                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("email", email);
                    //parametros.put("contrasenia", getSHA256(etContraActual2.getText().toString().trim()));
                    return parametros;
                }
            };
            //Creación de una cola de solicitudes
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //Agregar solicitud a la cola
            requestQueue.add(stringRequest);
       // }
    }

/*
    @NonNull
    public Dialog onCreateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Safe Zone");
        builder.setMessage("Are you sure you want to remove this safe zone?");
        builder.setPositiveButton(
                "REMOVE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //listener.removeSafezoneFromServer(center, radius, index);
                        confirmacion=1;
                        eliminarUsuario();
                    }
                });

        builder.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmacion=0;
                        dialog.cancel();
                        finish();

                    }
                });

        return builder.create();
    }
    */

/*
    public void handleBackPress() {
        mExitDialog = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(, R.style
                .AlertDialogStyle);
        builder.setTitle(R.string.leave_chat)
                .setMessage(R.string.leave_chat_desc)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mExitDialog = false;
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //showSaveHistoryDialog();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color
                .black14)));
    }
    */

    private int verificarContraBD() {
        if(compararContrasenia.toString().equals(getSHA256(etContraActual2.getText().toString()))){
            contraseniaCorrecta= 1;
        }
        else {
            contContraActual2.setError("Contraseña incorrecta");
            contContraActual2.requestFocus();
            contContraActual2.setErrorIconDrawable(null);
        }

        return contraseniaCorrecta;
    }

    private void obtenerDatosUsuario() {
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
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
}