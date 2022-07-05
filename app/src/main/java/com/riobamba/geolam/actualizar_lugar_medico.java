package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoCategoriaAdaptador;
import com.riobamba.geolam.modelo.ListadoTipologia;
import com.riobamba.geolam.modelo.ListadoTipologiaAdaptador;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class actualizar_lugar_medico extends AppCompatActivity {
    EditText txtTipologia, txtCategoria, txtNombreLugar, txtDireccion, txtTelefono, txtWhatsApp, txtPaginaWeb, txtLatitud, txtLongitud, txtDescripcion;
    TextView tvIdTipo;
    TextView tvIdCategoria ;
    TextView tvIdLugarMedico;

    String pTip;
    String pCat;
    Button btnGuardarInfo;
    String imagen_lugar;
    String idCategoria;
    String idTipologia;
    String listIDCat;
    String listIDTipo;
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
        setContentView(R.layout.activity_actualizar_lugar_medico);
        tvIdLugarMedico=findViewById(R.id.TextViewIDLugar_);
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
        ivFotoL = findViewById(R.id.imageViewLugar);
        btnGuardarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //General
                if(validarCampos()==4)
                {
                    insertarLugar();
                }
            }
        });
        //Conexión al Servidor- Consulta AutoComplete Tipología
        RequestQueue queue= Volley.newRequestQueue(this);
        String url=WebService.urlRaiz+WebService.servicioObtenerDatosLugarMedico;
        //adaptadorTipo.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url,
                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++) {

                            JSONObject object = array.getJSONObject(i);

                            tvIdLugarMedico.setText(object.getString("ID_LUGAR"));

                            txtNombreLugar.setText(object.getString("NOMBRE_LUGAR").toUpperCase());
                            txtDireccion.setText(object.getString("DIRECCION"));
                            txtTelefono.setText(object.getString("TELEFONO"));

                            Toast.makeText(actualizar_lugar_medico.this, object.getString("IMAGEN_LUGAR"), Toast.LENGTH_SHORT).show();
                            String urlI= object.getString("IMAGEN_LUGAR");
                            ivFotoL.setImageURI(Uri.parse(urlI));
                            /*RequestQueue request = Volley.newRequestQueue(getApplicationContext());

                           String urlImage =(object.getString("IMAGEN_LUGAR"));

                            urlImage=urlImage.replace(" ","%20");
                            ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    ivFotoL.setImageBitmap(response);
                                }
                                }, 0, 0, ImageView.ScaleType.CENTER,null,new Response.ErrorListener()
                                {
                                    @Override
                                            public void onErrorResponse(VolleyError error)
                                    {

                                    }

                            });


                            request.add(imageRequest);
 */
                            txtWhatsApp.setText(object.getString("WHATSAPP"));
                            txtPaginaWeb.setText(object.getString("PAGINA_WEB"));
                            txtLatitud.setText(object.getString("LATITUD"));
                            txtLongitud.setText(object.getString("LONGITUD"));
                            txtDescripcion.setText(object.getString("DESCRIPCION_LUGAR"));


                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        stringRequest.setTag("REQUEST");
        queue.add(stringRequest);



    }

    private void actualizar() {

    }

    public int validarCampos(){
        int respuesta =0;
        String nameImage= String.valueOf(ivFotoL.getTag());

        if(nameImage.equals("bg1")&&txtNombreLugar.getText().toString().equals("")
                && txtDireccion.getText().toString().equals("") &&
                /*txtTelefono.getText().toString().equals("") && */

                txtLatitud.getText().toString().equals("")
                && txtLongitud.getText().toString().equals("") && txtDescripcion.getText().toString().equals("")){
            Toast.makeText(actualizar_lugar_medico.this, "Campos vacíos. Por favor ingrese datos", Toast.LENGTH_SHORT).show();
            txtNombreLugar.setError("Ingrese el nombre del lugar");
            txtNombreLugar.requestFocus();
            txtDireccion.setError("Ingrese la dirección");
            //txtTelefono.setError("Ingrese el teléfono");
            txtLatitud.setError("Ingrese la latitud");
            txtLongitud.setError("Ingrese la longitud");
            txtDescripcion.setError("Ingrese la descripción");
        }
        else {if(nameImage.equals("bg1")){Toast.makeText(actualizar_lugar_medico.this, "Ingrese una imagen", Toast.LENGTH_SHORT).show();}
        else{ if(txtNombreLugar.getText().toString().equals("")){Toast.makeText(actualizar_lugar_medico.this, "Ingrese el nombre", Toast.LENGTH_SHORT).show();
            txtNombreLugar.setError("Ingrese el nombre");
            txtNombreLugar.requestFocus();}
        else {if(txtDireccion.getText().toString().equals("")){Toast.makeText(actualizar_lugar_medico.this, "Ingrese la dirección", Toast.LENGTH_SHORT).show();
            txtDireccion.setError("Ingrese la dirección");
            txtDireccion.requestFocus();
        }
           /* else {if(txtTelefono.getText().toString().equals("")){Toast.makeText(IngresoLugarMedico.this, "Ingrese el teléfono", Toast.LENGTH_SHORT).show();
                txtTelefono.setError("Ingrese el teléfono");
                txtTelefono.requestFocus();
            }*/
        else {if(txtLatitud.getText().toString().equals("")){Toast.makeText(actualizar_lugar_medico.this, "Ingrese la Latitud", Toast.LENGTH_SHORT).show();
            txtLatitud.setError("Ingrese la latitud");
            txtLatitud.requestFocus();
        }
        else {if (txtLongitud.getText().toString().equals("")) { Toast.makeText(actualizar_lugar_medico.this, "Ingrese la Longitud", Toast.LENGTH_SHORT).show();
            txtLongitud.setError("Ingrese la longitud");
            txtLongitud.requestFocus();
        }
        else {if (txtDescripcion.getText().toString().equals("")) {Toast.makeText(actualizar_lugar_medico.this, "Ingrese la descripción", Toast.LENGTH_SHORT).show();
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
            if(txtNombreLugar.getText().toString().length()<10)
            {
                Toast.makeText(this, "¡Error! Nombre del lugar", Toast.LENGTH_SHORT).show();
                txtNombreLugar.setError("Nombre demasiado corto. (Mínimo 10 caracteres)");
                txtNombreLugar.requestFocus();
            }
            else {
                datCorrecto=1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Nombre del lugar", Toast.LENGTH_SHORT).show();
            txtNombreLugar.setError("Nombre demasiado largo. (Mínimo 40 caracteres)");
            txtNombreLugar.requestFocus();
        }

        return datCorrecto;
    }

    public boolean urlValida(String url) {

/*
    try {
        new URL(url).toURI();

        return true;
    }
    catch (URISyntaxException exception) {
        return false;
    }
    catch (MalformedURLException exception) {
        return false;


 */

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

  /*  if (url == null || "".equals(url) )
    {
        return 0;
    }
       final Pattern p = Patterns.WEB_URL;
       final Matcher m = p.matcher(url);
        if(m.matches() )
        //if(Patterns.WEB_URL.matcher(url).matches())
        {
            datCorrecto=1;

        }
        else{
            Toast.makeText(this, "¡Error! Página Web", Toast.LENGTH_SHORT).show();
            txtPaginaWeb.setError("Dirección inválida");
            txtPaginaWeb.requestFocus();

        }*/
    /*if(urlValida(url))
    {
        datCorrecto=1;
    }
    else
    {
        txtPaginaWeb.setError("error url");
    }

*/
        if(url.length()<100) {
            if (urlValida(url)) {
                datCorrecto = 1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Página web", Toast.LENGTH_SHORT).show();
            txtPaginaWeb.setError("Página web demasiada larga. (Mínimo 100 caracteres)");
            txtPaginaWeb.requestFocus();
        }
    /*
    if (urlValida(uriUrl.toString()))
    {
        datCorrecto=1;
    }
    else{txtPaginaWeb.setError("error pagina");}
       // System.out.print("La url dada " + url + " no es válida");


    return datCorrecto;
    }
*/
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
        if(txtDireccion.getText().toString().length()<80){
            if(txtDireccion.getText().toString().length()<10)
            {
                Toast.makeText(this, "¡Error! Dirección del lugar", Toast.LENGTH_SHORT).show();
                txtDireccion.setError("Dirección demasiada corta. (Mínimo 10 caracteres)");
                txtDireccion.requestFocus();
            }
            else {
                datCorrecto=1;
            }
        }
        else
        {
            Toast.makeText(this, "¡Error! Dirección del lugar", Toast.LENGTH_SHORT).show();
            txtDireccion.setError("Dirección demasiada larga. (Mínimo 40 caracteres)");
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

                //Descartar el diálogo de progreso
                loading.dismiss();

                //Mostrando el mensaje de la respuesta
                Toast.makeText(getApplicationContext(), "Se ha registrado el lugar correctamente", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(), Login.class));
                //finish();
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
               // imagen_lugar = getStringImagen(bitmap); //Imagen

                //Obtener el nombre de la imagen
                String nombreImagen = txtNombreLugar.getText().toString().trim();


                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id_tipologia_lugar", tvIdTipo.getText().toString());
                parametros.put("id_categoria", tvIdCategoria.getText().toString());
                parametros.put("nombre_lugar", txtNombreLugar.getText().toString());
                parametros.put("direccion", txtDireccion.getText().toString());
                parametros.put("telefono", txtTelefono.getText().toString());
                parametros.put("whatsapp", txtWhatsApp.getText().toString());
                parametros.put("pagina_web", txtPaginaWeb.getText().toString());
                parametros.put("latitud", txtLatitud.getText().toString());
                parametros.put("longitud", txtLongitud.getText().toString());
                parametros.put("descripcion_lugar", txtDescripcion.getText().toString());

                //Imagen
              //  parametros.put(claveImagen, imagen_lugar);
               // parametros.put(claveNombre, nombreImagen);


                return parametros;
            }
        };
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

}