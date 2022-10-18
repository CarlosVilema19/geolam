package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.ListadoCategoria;
import com.riobamba.geolam.modelo.ListadoCategoriaAdaptador;
import com.riobamba.geolam.modelo.ListadoTipologia;
import com.riobamba.geolam.modelo.ListadoTipologiaAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IngresoLugarMedico extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    EditText txtTipologia, txtCategoria, txtNombreLugar, txtDireccion, txtTelefono, txtWhatsApp, txtPaginaWeb, txtLatitud, txtLongitud, txtDescripcion;
    TextView tvIdTipo;
    TextView tvIdCategoria ;

    String pTip;
    String pCat;
    Button btnGuardarInfo, btnAgregados, btnCancelar;
    String imagen_lugar;
    String idCategoria;
    String idTipologia;
    String listIDCat;
    String listIDTipo;

    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    //Imagen
    private Button btnCargarImagen;
    private ImageView ivFotoL;
    private Bitmap bitmap;

    private Bitmap newbitMap;
    private String claveImagen = "foto";
    private String claveNombre = "nombre";
    private int PICK_IMAGE_REQUEST = 1;

    ArrayList<String> opcionesTipologia = new ArrayList<>();
    ArrayList<String> opcionesCategoria= new ArrayList<>();
    //autocomplete
    private ListadoCategoriaAdaptador adaptadorCategoria;
    private ListadoTipologiaAdaptador adaptadorTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_lugar_medico);

        //AutoCompleteTextView

        adaptadorCategoria = new ListadoCategoriaAdaptador(this);
        AutoCompleteTextView autoCompleteOpcionesCategoria=findViewById(R.id.autoCat);

        adaptadorTipo= new ListadoTipologiaAdaptador(this);
        AutoCompleteTextView autoCompleteOpcionesTipologia=findViewById(R.id.autoTipologia);

        //Adaptador
        autoCompleteOpcionesCategoria.setAdapter(adaptadorCategoria);

        autoCompleteOpcionesTipologia.setAdapter(adaptadorTipo);

        toolbar.show(this, "Gestión de lugares", false); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)


        //Conexión al Servidor- Consulta AutoComplete Tipología
        RequestQueue queue= Volley.newRequestQueue(this);
        String url=WebService.urlRaiz+WebService.servicioListarTipologia;
        //adaptadorTipo.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url,
                response ->
                {
                    try{
                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            ListadoTipologia tipo=new ListadoTipologia(object);
                            //Carga de datos
                            adaptadorTipo.add(tipo);
                            listIDTipo = ( object2.getString("ID_TIPOLOGIA_LUGAR"));
                            opcionesTipologia.add(listIDTipo);
                        }

                        autoCompleteOpcionesTipologia.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();
                                String a= "";
                                a=itemTipo;
                               // idTipologia =RetornaID2(a);

                                String p= String.valueOf(position).trim();
                                //for(int k=0;k<opciones.size();k++)
                                //{
                                    String c= opcionesTipologia.get(position);
                                        pTip=c;
                                        //pTip=opciones.get();
                                        retornaIdTipologia(pTip);
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        stringRequest.setTag("REQUEST");
        queue.add(stringRequest);

        //Conexión al Servidor- Consulta AutoComplete Categoría

        RequestQueue queue2= Volley.newRequestQueue(this);
        String url2=WebService.urlRaiz+WebService.servicioListarCategoria;
        //adaptadorTipo.clear();
        StringRequest stringRequest2= new StringRequest(Request.Method.GET,url2,
                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            JSONObject object2 = array.getJSONObject(i);
                            ListadoCategoria cat=new ListadoCategoria(object);
                            adaptadorCategoria.add(cat);
                            listIDCat = ( object2.getString("ID_CATEGORIA"));
                            opcionesCategoria.add(listIDCat);
                        }

                        autoCompleteOpcionesCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemCat = parent.getItemAtPosition(position).toString();

                                String c= opcionesCategoria.get(position);
                                pCat=c;
                                retornaIdCategoria(pCat);
                            }

                        });



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        stringRequest2.setTag("REQUEST");
        queue2.add(stringRequest2);

        //Imagen
        btnCargarImagen = (Button) findViewById(R.id.btn_cargarfotoL);
        btnGuardarInfo = findViewById(R.id.btn_guardarLugar);
        btnAgregados = findViewById(R.id.btnAgregados);

        ivFotoL = findViewById(R.id.imageViewLugar);


        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.equals(btnCargarImagen) ) {
                    showFileChooser();
                }


            }
        });

        //otros campos
        txtTipologia = findViewById(R.id.autoTipologia);
        txtCategoria= findViewById(R.id.autoCat);
        txtNombreLugar = findViewById(R.id.ingresoNombreLugar);
        txtDireccion = findViewById(R.id.ingresoDireccion);
        txtTelefono = findViewById(R.id.ingresoTelefono);
        txtWhatsApp = findViewById(R.id.ingresoWhatsApp);
        txtPaginaWeb = findViewById(R.id.ingresoPaginaWeb);
        txtLatitud = findViewById(R.id.ingresoLatitud);
        txtLongitud = findViewById(R.id.ingresoLongitud);
        txtDescripcion = findViewById(R.id.ingresoDescripcionLugar);
        tvIdTipo = (TextView) findViewById(R.id.TextViewIDTipologia);
        tvIdCategoria = (TextView) findViewById(R.id.TextViewIDCategoria);
        btnGuardarInfo= findViewById(R.id.btn_guardarLugar);
        btnCancelar = findViewById(R.id.btnCancelar);


        btnGuardarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //General
                if (verificarSimilitud() == 0) {
                    Toast.makeText(IngresoLugarMedico.this, "Campos vacíos. Por favor ingrese datos", Toast.LENGTH_SHORT).show();
                }
              validarLugar();
            }
        });

        btnAgregados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngresoLugarMedico.this, ListadoCrud.class);
                startActivity(intent);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarSimilitud() == 0) {
                    finish();
                } else {
                    int icon  = R.drawable.peligro;
                    AlertDialog.Builder builder = new AlertDialog.Builder(IngresoLugarMedico.this);
                    builder.setIcon(icon)
                            .setTitle("Cancelar")
                            .setMessage("Se perderán todos los cambios realizados ¿Desea continuar?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
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


    }
    private void validarLugar(){
        if(verificarSimilitud()==1) {
            if (validarCampos() == 4) {
                String url = WebService.urlRaiz + WebService.servicioExistenciaLugar;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response ->
                        {

                            try {

                                JSONObject object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                                String existencia = object.getString("valida");
                                if (existencia.equals("existe")) {

                                    txtNombreLugar.setError("¡Este lugar ya existe!");
                                    txtNombreLugar.requestFocus();

                                } else {
                                    insertarLugar();
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
                        parametros.put("nombre_lugar", txtNombreLugar.getText().toString().toUpperCase().trim());

                        return parametros;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        }
    }
    private int verificarSimilitud()
    {
        int exis = 0;
        if(!txtNombreLugar.getText().toString().equals("")||
                !tvIdTipo.getText().toString().equals("")||
                !tvIdCategoria.getText().toString().equals("")||
                !txtDireccion.getText().toString().equals("")||
                !txtTelefono.getText().toString().equals("")||
                !txtWhatsApp.getText().toString().equals("")||
                !txtPaginaWeb.getText().toString().equals("")||
                !txtLatitud.getText().toString().equals("")||
                !txtLongitud.getText().toString().equals("")||
                !txtDescripcion.getText().toString().equals("")||
                !tvIdTipo.getText().toString().equals(""))
        {
            exis =1;
        }
        return exis;
    }

    private void retornaIdTipologia(String pTip) {

        CharSequence p=pTip;
        String p2=p.toString();
        tvIdTipo.setText(p2);
        //Carga de datos

    }
    private void retornaIdCategoria(String pCat) {

        CharSequence p=pCat;
        String p2=p.toString();
        tvIdCategoria.setText(p2);
        //Carga de datos

    }

    private String RetornaID(String item) {

        String data = item;
        String[] split = data.split("-");
        String id=null;
        for (int i=0; i<split.length; i++) {
            //System.out.println(split[i]);
            id= split[0];
            //Toast.makeText(getApplicationContext(), "Item: " + split[i], Toast.LENGTH_SHORT).show();
        }
       return id;
    }

    public String getStringImagen(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
public int validarCampos(){
    int respuesta =0;
        String nameImage= String.valueOf(ivFotoL.getTag());

            if(nameImage.equals("bg1")&&txtNombreLugar.getText().toString().equals("")
                    && txtDireccion.getText().toString().equals("") &&
                    /*txtTelefono.getText().toString().equals("") && */

    txtLatitud.getText().toString().equals("")
                    && txtLongitud.getText().toString().equals("") && txtDescripcion.getText().toString().equals("")){
            Toast.makeText(IngresoLugarMedico.this, "Campos vacíos. Por favor ingrese datos", Toast.LENGTH_SHORT).show();
                txtNombreLugar.setError("Ingrese el nombre del lugar");
                txtNombreLugar.requestFocus();
                txtDireccion.setError("Ingrese la dirección");
                //txtTelefono.setError("Ingrese el teléfono");
                txtLatitud.setError("Ingrese la latitud");
                txtLongitud.setError("Ingrese la longitud");
                txtDescripcion.setError("Ingrese la descripción");
            }
            else {if(nameImage.equals("bg1")){Toast.makeText(IngresoLugarMedico.this, "Ingrese una imagen", Toast.LENGTH_SHORT).show();}
            else{ if(txtNombreLugar.getText().toString().equals("")){Toast.makeText(IngresoLugarMedico.this, "Ingrese el nombre", Toast.LENGTH_SHORT).show();
            txtNombreLugar.setError("Ingrese el nombre");
            txtNombreLugar.requestFocus();}
            else {if(txtDireccion.getText().toString().equals("")){Toast.makeText(IngresoLugarMedico.this, "Ingrese la dirección", Toast.LENGTH_SHORT).show();
                txtDireccion.setError("Ingrese la dirección");
                txtDireccion.requestFocus();
            }
           /* else {if(txtTelefono.getText().toString().equals("")){Toast.makeText(IngresoLugarMedico.this, "Ingrese el teléfono", Toast.LENGTH_SHORT).show();
                txtTelefono.setError("Ingrese el teléfono");
                txtTelefono.requestFocus();
            }*/
            else {if(txtLatitud.getText().toString().equals("")){Toast.makeText(IngresoLugarMedico.this, "Ingrese la Latitud", Toast.LENGTH_SHORT).show();
                txtLatitud.setError("Ingrese la latitud");
                txtLatitud.requestFocus();
            }
            else {if (txtLongitud.getText().toString().equals("")) { Toast.makeText(IngresoLugarMedico.this, "Ingrese la Longitud", Toast.LENGTH_SHORT).show();
                txtLongitud.setError("Ingrese la longitud");
                txtLongitud.requestFocus();
            }
            else {if (txtDescripcion.getText().toString().equals("")) {Toast.makeText(IngresoLugarMedico.this, "Ingrese la descripción", Toast.LENGTH_SHORT).show();
                txtDescripcion.setError("Ingrese una descripción");
                txtDescripcion.requestFocus();
            }
            else{respuesta=2;}
        }}}}}
            //}
    }
int opcionales=0;
    if(respuesta==2) {
        if (validarNombre() == 1 && validarDireccion() == 1 &&  txtTelefono.getText().toString().equals("")
                && txtWhatsApp.getText().toString().equals("") && txtPaginaWeb.getText().toString().equals("")
        ) {
            respuesta = 3;
        } else {
            if (validarNombre() == 1 && validarDireccion() == 1 && !txtTelefono.getText().toString().equals("") && !txtWhatsApp.getText().toString().equals("") && !txtPaginaWeb.getText().toString().equals("")) {

                if (validarTelefono() == 1 && validarWhatsapp() == 1 && validarUrl() == 1) {
                    opcionales = 2;
                }
            } else {
                if (validarNombre() == 1 && validarDireccion() == 1 && !txtTelefono.getText().toString().equals("")&& txtWhatsApp.getText().toString().equals("") && txtPaginaWeb.getText().toString().equals("")) {
                    if (validarTelefono() == 1) {
                        opcionales = 3;
                    }
                } else {if (validarNombre() == 1 && validarDireccion() == 1 && !txtWhatsApp.getText().toString().equals("") && txtTelefono.getText().toString().equals("") && txtPaginaWeb.getText().toString().equals("")) {
                    if (validarWhatsapp() == 1) {
                        opcionales = 4;
                    }

                } else {
                    if (validarNombre() == 1 && validarDireccion() == 1  && !txtPaginaWeb.getText().toString().equals("") && txtTelefono.getText().toString().equals("")&& txtWhatsApp.getText().toString().equals("")) {
                        if (validarUrl() == 1) {
                            opcionales = 5;
                        }
                    }
                    else {
                        if (validarNombre() == 1 && validarDireccion() == 1 && !txtTelefono.getText().toString().equals("") && !txtWhatsApp.getText().toString().equals("") && txtPaginaWeb.getText().toString().equals("")) {
                            if (validarTelefono() == 1 && validarWhatsapp() == 1) {
                                opcionales = 6;
                            }

                        } else {
                            if (validarNombre() == 1 && validarDireccion() == 1 && txtTelefono.getText().toString().equals("") && !txtWhatsApp.getText().toString().equals("") && !txtPaginaWeb.getText().toString().equals("")) {
                                if (validarWhatsapp() == 1 && validarUrl() == 1) {
                                    opcionales = 7;
                                }
                            } else {
                                if (validarNombre() == 1 && validarDireccion() == 1 && !txtTelefono.getText().toString().equals("") && txtWhatsApp.getText().toString().equals("") && !txtPaginaWeb.getText().toString().equals("")) {

                                    if (validarTelefono() == 1 && validarUrl() == 1) {
                                        opcionales = 8;
                                    }
                                }
                            }
                        }
                    }



                    }
                }



            }

        }
        if (respuesta == 3 || opcionales ==2 || opcionales == 3 || opcionales == 4 || opcionales == 5 || opcionales == 6|| opcionales == 7 || opcionales == 8) {
            respuesta = 4;
        }

    }
        return respuesta;
}

private int validarNombre(){
     int datCorrecto=0;
     if(txtNombreLugar.getText().toString().length()<80){
         if (Pattern.compile(" {2,}").matcher(txtNombreLugar.getText().toString()).find()) {
             txtNombreLugar.setError("¡Verifique que no haya más de un espacio en blanco!");
             txtNombreLugar.requestFocus();
         }else if(txtNombreLugar.getText().toString().length()<5)
         {
             txtNombreLugar.setError("Nombre demasiado corto. (Mínimo 5 caracteres)");
             txtNombreLugar.requestFocus();
         }
         else {
             datCorrecto=1;
         }
     }
     else
     {
         Toast.makeText(this, "¡Error! Nombre del lugar", Toast.LENGTH_SHORT).show();
         txtNombreLugar.setError("Nombre demasiado largo. (Máximo 80 caracteres)");
         txtNombreLugar.requestFocus();
     }

     return datCorrecto;
}

public boolean urlValida(String url) {


    try {
        new URL(url).toURI();
        //URL urlV = new URL(url);
        return URLUtil.isValidUrl(url) && Patterns.WEB_URL.matcher(url).matches(); }
    catch (MalformedURLException e) {
        Toast.makeText(this, "¡Error! URL" + e.toString(), Toast.LENGTH_SHORT).show();
        txtPaginaWeb.setError("URL mal formada");
        txtPaginaWeb.requestFocus();
    }
    catch (URISyntaxException exception) {
        Toast.makeText(this, "¡Error! URL" + exception.toString(), Toast.LENGTH_SHORT).show();
        txtPaginaWeb.setError("Error de sintaxis");
        txtPaginaWeb.requestFocus();
        return false;
    }
    return false;




}
private int validarUrl() {
    int datCorrecto = 0;
    String url = txtPaginaWeb.getText().toString().trim();

    if(url.length()<100) {
        if (urlValida(url)) {
            datCorrecto = 1;
        }
        else if(Pattern.compile(" {2,}").matcher(txtPaginaWeb.getText().toString()).find())
            {
                txtPaginaWeb.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtPaginaWeb.requestFocus();
            }
    }
    else
    {
        Toast.makeText(this, "¡Error! Página web", Toast.LENGTH_SHORT).show();
        txtPaginaWeb.setError("Página web demasiada larga. (Máximo 100 caracteres)");
        txtPaginaWeb.requestFocus();
    }

    return datCorrecto;
}

private int validarDescripcion(){
        int datCorrecto=0;
        if(txtDescripcion.getText().toString().length()<80) {

            datCorrecto=1;
        }
        return datCorrecto;
}
private int validarDireccion(){

    int datCorrecto=0;
    if(txtDireccion.getText().toString().length()<200){
        if(Pattern.compile(" {2,}").matcher(txtDireccion.getText().toString()).find())
        {
            txtDireccion.setError("¡Verifique que no haya más de un espacio en blanco!");
            txtDireccion.requestFocus();
        } else if(txtDireccion.getText().toString().length()<5)
        {
            txtDireccion.setError("Dirección demasiada corta. (Mínimo 5 caracteres)");
            txtDireccion.requestFocus();
        }
        else {
            datCorrecto=1;
        }
    }
    else
    {
        Toast.makeText(this, "¡Error! Dirección del lugar", Toast.LENGTH_SHORT).show();
        txtDireccion.setError("Dirección demasiada larga. (Máximo 200 caracteres)");
        txtDireccion.requestFocus();
    }

    return datCorrecto;
}

private int validarTelefono(){
    int datCorrecto=0;
    if(txtTelefono.getText().toString().length()<11){
        if(txtTelefono.getText().toString().length()==7 )
        {
            datCorrecto=1;
        }
        else {

            Toast.makeText(this, "¡Error! Teléfono del lugar", Toast.LENGTH_SHORT).show();
            txtTelefono.setError("Teléfono inválido (Ingrese únicamente 7 dígitos)");
            txtTelefono.requestFocus();
        }
    }
    else
    {
        Toast.makeText(this, "¡Error! Número de Contacto del lugar", Toast.LENGTH_SHORT).show();
        txtTelefono.setError("Teléfono demasiado largo");
        txtTelefono.requestFocus();
    }

    return datCorrecto;

}
private int validarWhatsapp(){
    int datCorrecto=0;
    if(txtWhatsApp.getText().toString().length()<11){
        if( txtWhatsApp.getText().toString().length()==10)
        {
            datCorrecto=1;
        }
        else {

            Toast.makeText(this, "¡Error! WhatsApp del lugar", Toast.LENGTH_SHORT).show();
            txtWhatsApp.setError("Número de WhatsApp es inválido (Ingrese 10 dígitos)");
            txtWhatsApp.requestFocus();
        }
    }
    else
    {
        Toast.makeText(this, "¡Error! WhatsApp del lugar", Toast.LENGTH_SHORT).show();
        txtTelefono.setError("El número de WhatsApp es demasiado largo");
        txtTelefono.requestFocus();
    }

    return datCorrecto;

}
    private void insertarLugar() {
        String url = WebService.urlRaiz +WebService.servicioInsertarLugar;
        final ProgressDialog loading = ProgressDialog.show(this, "Guardando la información...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Se ha registrado el lugar correctamente", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(IngresoLugarMedico.this, IngresoLugarMedico.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                //Showing toast
                Toast.makeText(getApplicationContext(), "Se ha producido un error inesperado", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                imagen_lugar = getStringImagen(bitmap); //Imagen

                //Obtener el nombre de la imagen
                String nombreImagen = txtNombreLugar.getText().toString().trim();


                Map<String, String> parametros = new HashMap<String, String>();
               parametros.put("id_tipologia_lugar", tvIdTipo.getText().toString());
               parametros.put("id_categoria", tvIdCategoria.getText().toString());
               parametros.put("nombre_lugar", txtNombreLugar.getText().toString().trim().toUpperCase());
                parametros.put("direccion", txtDireccion.getText().toString().trim());
                parametros.put("telefono", txtTelefono.getText().toString().trim());
                parametros.put("whatsapp", txtWhatsApp.getText().toString().trim());
                parametros.put("pagina_web", txtPaginaWeb.getText().toString().trim());
                parametros.put("latitud", txtLatitud.getText().toString().trim());
                parametros.put("longitud", txtLongitud.getText().toString().trim());
                parametros.put("descripcion_lugar", txtDescripcion.getText().toString().trim());

                //Imagen
                parametros.put(claveImagen, imagen_lugar);
                parametros.put(claveNombre, nombreImagen);


                return parametros;
            }
        };
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }
    public void showFileChooser() {


        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imagen"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivFotoL.setTag("bg2");
                //Configuración del mapa de bits en ImageView
                int bwidth = bitmap.getWidth();
                int bheight = bitmap.getHeight();
                int swidth = ivFotoL.getWidth();
                int sheight = ivFotoL.getHeight();
                int new_width = swidth;
                int new_height = (int) Math.floor((double) bheight * ((double) new_width / (double) bwidth));
                newbitMap = Bitmap.createScaledBitmap(bitmap, new_width, new_height, true);
                ivFotoL.setImageBitmap(newbitMap);


            } catch (IOException e) {
                e.printStackTrace();
            }
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

    @Override
    public void onBackPressed() {}

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
