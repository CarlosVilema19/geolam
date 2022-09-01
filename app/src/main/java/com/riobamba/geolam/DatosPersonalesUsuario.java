package com.riobamba.geolam;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.riobamba.geolam.modelo.DatosPersonalesAdaptador;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.hash.Hashing;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCache;
import com.riobamba.geolam.modelo.DatosPersonales;
import com.riobamba.geolam.modelo.Proceso;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatosPersonalesUsuario extends AppCompatActivity {
    EditText txtName, txtApe, txtEdad;

    Button btnGuardarCambios, btnCancelar;
    Toolbar toolbar = new Toolbar();
    String compararNombre="";
    String compararApellido="";
    String compararFecha="";

    String imagen_lugar;
    TextView tvEmail;


    //Edad

    String edadUsu = "";
    private int dia,mes,anio;
    Button btnFechaIngreso;
    EditText txtFechaingreso;
    String fechaNacBD;
    String edadCalculada;
    String fechaVerif="";
    String fechaNac="";

//Imagen

    private Button btnCargarImagen;


    //Botones Editar

    Button btnEditNombre, btnEditApellido,btnEditEdad;

    //-------
    private ImageView ivFotoP;
    private Bitmap bitmap;
    private Bitmap newbitMap;
    private String claveImagen = "foto";
    private String claveNombre = "nombre";
    private int PICK_IMAGE_REQUEST = 1;

    String ruta;
    String urlImagenLugar;
    String urlSinEspacios;

    public Class<Login> login = Login.class;
    public Context ctx;

    public void getContexto(Context ctx)
    {
        this.ctx = ctx;
    }

    int datosIgualesNombre=0, datosIgualesApellido =0, datosIgualesEdad=0, datosIgualesContrasenia=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);
        //Calendario Edad

        txtFechaingreso = findViewById(R.id.etFecha_);
        txtEdad = findViewById(R.id.edEdad2);
        btnFechaIngreso = findViewById(R.id.btnFechaNac);
        Proceso proc = new Proceso();
        toolbar.show(this, "Perfil", true);
        //Variables
        txtName =findViewById(R.id.etNombre3);
        txtApe =findViewById(R.id.etApellido3);
       // txtEdad=findViewById(R.id.etEdad);

        ivFotoP= (ImageView) findViewById(R.id.ivPerfil3);
        btnGuardarCambios=findViewById(R.id.btnGuardarCambiosUsu);
        btnCancelar=findViewById(R.id.btnCancelar);
        btnCargarImagen=findViewById(R.id.btn_cargarf3);
        tvEmail = findViewById(R.id.tvEmailUsu1);

        btnEditNombre=findViewById(R.id.btnEditNombre);
        btnEditApellido=findViewById(R.id.btnEditApellido);

       /* if(txtName.isFocusable())
        {
            txtApe.setFocusable(false);
            txtEdad.setFocusable(false);
            txtApe.requestFocus();
        }else{
            if(txtApe.isFocusable()){
                txtName.setFocusable(false);
                txtEdad.setFocusable(false);
            }
            else {
                if(txtEdad.isFocusable()){}
                txtName.setFocusable(false);
                txtApe.setFocusable(false);
            }
        }*/

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        //EDAD

        btnFechaIngreso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtName.setFocusable(false);
                txtApe.setFocusable(false);
                if(v==btnFechaIngreso)
                {
                    final Calendar calen = Calendar.getInstance();
                    dia = calen.get(Calendar.DAY_OF_MONTH);
                    mes = calen.get(Calendar.MONTH);
                    anio = calen.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(DatosPersonalesUsuario.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String fecha = dayOfMonth+"/"+(month+1)+"/"+year;
                            //fechaVerif =
                                    //year+"/"+(month+1)+"/"+dayOfMonth;
                            DecimalFormat formato2 = new DecimalFormat("#00");
                            fechaVerif=year+"/"+formato2.format((month+1))+"/"+formato2.format(dayOfMonth);
                            fechaNac = year+formato2.format((month+1))+formato2.format(dayOfMonth);
                            txtFechaingreso.setText(fecha);
                            //Edad en en anios y meses
                            edadCalculada = proc.calcularEdad(year,month+1,dayOfMonth);
                            //Edad en anios
                            edadUsu = proc.calcularEdadAnios(year,month+1,dayOfMonth);
                            txtEdad.setText(edadCalculada);


                        }
                    },dia,mes,anio);
                    datePickerDialog.show();
                    datePickerDialog.getDatePicker().setMaxDate(new Date().getTime()-(proc.calcularAniosMili(15)));
                    datePickerDialog.getDatePicker().setMinDate(new Date().getTime()-(proc.calcularAniosMili(100)));
                }

            }
        });


        //Bloquea Ingreso de Texto
        //txtName.setClickable(false);
        //txtName.setFocusable(false);
      //  txtName.setEnabled(false);


        //txtApe.setClickable(false);
        //txtApe.setFocusable(false);


        //ivFotoP.setImageBitmap(null);
        //ivFotoP.setBackground(null);
//        ivFotoP.setBackgroundResource(0);
//        ivFotoP.destroyDrawingCache();
  //      ivFotoP.buildDrawingCache();
        //ivFotoP.getDrawingCache();
       // ivFotoP.setImageDrawable(null);
        //ivFotoP.invalidate();
        //ivFotoP.setImageResource(0);

      //  urlImagenLugar=null;


        //ivFotoP.destroyDrawingCache();
        //ivFotoP.willNotCacheDrawing();
      //  txtConfirmarContrasenia.setEnabled(false);
       // txtContraseniaNueva.setEnabled(false);
        /*bitmap = BitmapFactory.decodeResource(this.getResources(), 0);
        ivFotoP.setImageBitmap(bitmap);
        ivFotoP.setImageBitmap(null);

       ivFotoP.setDrawingCacheEnabled(true);
*/
        MostrarResultado();



        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                modificarDatos();



            }
        });

        btnEditNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                android:clickable="false"
                android:cursorVisible="false"
                android:focusable="false"
                android:focusableInTouchMode="false"
                */
                txtApe.setFocusable(false);
                txtName.setClickable(true);
                txtName.setSelection(txtName.getText().length());

                txtName.setCursorVisible(true);
                txtName.setFocusable(true);
                txtName.setFocusableInTouchMode(true);
                txtName.setInputType((InputType.TYPE_CLASS_TEXT));
                txtName.requestFocus();

                /*

                txtName.setClickable(true);
                txtName.setFocusable(true);
                txtName.setInputType((InputType.TYPE_CLASS_TEXT));
                txtName.setTextIsSelectable(true);
                txtName.requestFocus();
                */


            }
        });
        /*
        txtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txtName.setFocusable(false);
            }
        });
        txtApe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txtApe.setFocusable(false);
            }
        });
*/
        btnEditApellido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*txtName.setClickable(false);
                txtName.setCursorVisible(false);
                txtName.setFocusable(false);
                txtName.setFocusableInTouchMode(false);
*/

                txtName.setFocusable(false);
                txtApe.setClickable(true);
                txtApe.setFocusable(true);
                txtApe.setSelection(txtApe.getText().length());
                txtApe.setCursorVisible(true);
                txtApe.setFocusableInTouchMode(true);
                txtApe.setInputType((InputType.TYPE_CLASS_TEXT));
                //txtApe.setTextIsSelectable(true);
                txtApe.requestFocus();

            }
        });

        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v==btnCargarImagen) {
                    showFileChooser();
                }


            }
        });


    }
    public void showFileChooser() {


        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imagen"), PICK_IMAGE_REQUEST);

    }
    public void MostrarResultado()
    {
        /*
        ivFotoP.destroyDrawingCache();
        ivFotoP.buildDrawingCache();
        ivFotoP.setBackgroundResource(0);
       ivFotoP.getDrawingCache();
        ivFotoP.setImageDrawable(null);
        ivFotoP.invalidate();
        */
      //  txtName.setClickable(false);
       // txtName.setFocusable(false);
        //  txtName.setEnabled(false);


        //txtApe.setClickable(false);
        //txtApe.setFocusable(false);

        final ProgressDialog loading2 = ProgressDialog.show(this, "Obteniendo información...", "Espere por favor");


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


                                //ivFotoP.setDrawingCacheEnabled(false);
                               // ivFotoP.clearFocus();

                                String urlImage=obj.getString("imagen");
                                Toast.makeText(DatosPersonalesUsuario.this,urlImage.toString(),Toast.LENGTH_SHORT).show();
                                String nameImage= String.valueOf(ivFotoP.getTag());
                              //  imagenReturn(urlImage);
                              if(imagenReturn(urlImage)==1) {
                                    //&&nameImage.equals("bg2")
                                    tvEmail.setText(obj.getString("email"));
                                    txtName.setText(obj.getString("nombre_usuario"));
                                    compararNombre=obj.getString("nombre_usuario");
                                    txtApe.setText(obj.getString("apellido_usuario"));
                                    compararApellido=obj.getString("apellido_usuario");
                                    txtEdad.setText(obj.getString("edad"));
                                    txtFechaingreso.setText(obj.getString("fecha_nacimiento"));
                                    compararFecha=obj.getString("fecha_nacimiento").replace("-","/");
                                   // compararContrasenia = obj.getString("contrasenia");
                                    loading2.dismiss();
                                }

                              //  ivFotoP.setDrawingCacheEnabled(false);

                               // Toast.makeText(getApplicationContext(), compararContrasenia.toString(), Toast.LENGTH_SHORT).show();
                                //ivFotoP.clearColorFilter();
                                //ivFotoP.setImageBitmap(null);
                                //ivFotoP.destroyDrawingCache();


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

    private int imagenReturn(String url) {
        final ProgressDialog loading2 = ProgressDialog.show(this, "Obteniendo información...", "Espere por favor");
        int band=0;
        if(url.contains(WebService.imagenRaiz)) {
            urlSinEspacios = url.replace(" ", "%20");
            String data = urlSinEspacios;
            String[] split = data.split(WebService.imagenRaiz);
            ruta = null;
            for (int i = 0; i < split.length; i++) {
                ruta = split[1];
            }
            urlImagenLugar= WebService.urlRaiz+ruta;
           // Toast.makeText(getApplicationContext(), ruta, Toast.LENGTH_LONG).show();
        }
        else{
            urlImagenLugar=urlSinEspacios;
        }

       /* Picasso.get().load(urlImagenLugar).networkPolicy(NetworkPolicy.OFFLINE).into(ivFotoP, new Callback() {
            @Override
            public void onSuccess() {
               // Log.i("Load image from caché! " + urlImagenLugar);
               // textView.setText("From Caché:\n " + Uri.parse(urlImage).getLastPathSegment());

            }*/

         /*   @Override
            public void onError(Exception e) {
                Log.e(String.valueOf(DatosPersonalesUsuario.this),"onError() " + e.getMessage());
                Log.i(String.valueOf(DatosPersonalesUsuario.this), "Try to load image from internet! " + urlImagenLugar);
                //Can´t find image in cache, load from internet.
                */

                Picasso.get().load(urlImagenLugar).fit().centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(ivFotoP);
                loading2.dismiss();
               // Picasso.get().load(urlImagenLugar).into(ivFotoP);

            //}
       // });
/*
        Picasso.get().load(urlImagenLugar).fit().centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(ivFotoP);
                */

        //ivFotoP.setTag("bg2");
        band=1;
        return band;
        //Toast.makeText(this,urlImagenLugar, Toast.LENGTH_LONG).show();

      /*  RequestQueue request4 = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(urlImagenLugar, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                */
                //ivFotoP=findViewById(R.id.ivPerfil2);
               // bitmap.setConfig();

                //bitmap.setHeight(response.getHeight());
                //bitmap.setWidth(response.getWidth());
               // ivFotoP.setTag("bg2");
                //Configuración del mapa de bits en ImageView
                /*int bwidth = response.getWidth();
                int bheight = response.getHeight();
                int swidth = ivFotoP2.getWidth();
                int sheight = ivFotoP2.getHeight();
                int new_width = swidth;
                int new_height = (int) Math.floor((double) bheight * ((double) new_width / (double) bwidth));
                newbitMap = Bitmap.createScaledBitmap(response, new_width, new_height, true);
                */
               // Picasso.get().load(urlImagenLugar).into(ivFotoP);

                /*Picasso.get().load(urlImagenLugar).fit().centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(ivFotoP);*/

                //ivFotoP2.setImageBitmap(newbitMap);
               // bitmap=null;
               // ivFotoP2.setImageBitmap(bitmap);
                //ivFotoP2.getDrawingTime();

                //ImageHandler.saveImageToprefrence(getSharedPreferences(ImageHandler.MainKey,MODE_PRIVATE),response);
               // ImageView iv=(ImageView)findViewById(R.id.imageView);
                //ImageView ivFotoP2= (ImageView) findViewById(R.id.ivPerfil3);
               // ivFotoP2.setImageBitmap(null);

                //bitmap = null;
                //bitmap=response;
                //bitmap = Bitmap.createBitmap(response);
               // ivFotoP2.setImageBitmap(bitmap);
               // bitmap.recycle();
               // bitmap = null;
                //bitmap = Bitmap.createBitmap(ivFotoP2.getDrawingCache());
                //ivFotoP2.setImage(ImageSource.bitmap(response));
               // ivFotoP2.destroyDrawingCache();
                //ivFotoP.setBackgroundResource(0);

                //ivFotoP2.buildDrawingCache();
              //  ivFotoP2.getDrawingCache();

               //ivFotoP.setImageDrawable(null);
               // ivFotoP2.invalidate();
                //ivFotoP.setImageResource(0);
/*
            }
        }, 0, 0,ImageView.ScaleType.CENTER, null,new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                Log.e(String.valueOf(DatosPersonalesUsuario.this), "Image Load Error: ");

            }

        });
*/

        //request4.add(imageRequest);

    }

    public void modificarDatos() {
        if (validarCampos() == 1) {
                if(verificaSimilitud()==1) {
                    String url = WebService.urlRaiz + WebService.servicioModificarDatosPersonales;
                    final ProgressDialog loading = ProgressDialog.show(this, "Actualizando la información...", "Espere por favor");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //Descartar el diálogo de progreso
                            // final ProgressDialog loading = ProgressDialog.show(this, "Actualizando la información...", "Espere por favor");
                            loading.dismiss();

                            //Mostrando el mensaje de la respuesta
                            Toast.makeText(getApplicationContext(), "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
                            ivFotoP.setTag("bg1");
                            finish();
                            Intent intent = new Intent(DatosPersonalesUsuario.this, DatosPersonalesUsuario.class);
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
                            String nameImage= String.valueOf(ivFotoP.getTag());
                            String nombreImagen;

                            Map<String, String> parametros = new HashMap<String, String>();

                            if(nameImage.equals("bg2"))
                            {
                                imagen_lugar = getStringImagen(bitmap); //Imagen
                                //Obtener el nombre de la imagen
                                nombreImagen = tvEmail.getText().toString().trim();
                                parametros.put(claveImagen, imagen_lugar);
                                parametros.put(claveNombre, nombreImagen);

                            }else
                            {
                               // parametros.put(claveImagen, " ");
                               // parametros.put(claveNombre, " ");
                                String sinImage="1";
                                parametros.put("sin_imagen",sinImage.toString());

                            }

                            parametros.put("email", tvEmail.getText().toString());
                            parametros.put("nombre_usuario", txtName.getText().toString());
                            parametros.put("apellido_usuario", txtApe.getText().toString());
                            parametros.put("edad", txtEdad.getText().toString());

                            if (fechaNac.isEmpty()) {
                                String date = "1";
                                parametros.put("sinFecha", date.toString());//String.valueOf(fechaNacimiento));
                            } else {
                                parametros.put("fecha_nacimiento", fechaNac);//String.valueOf(fechaNacimiento));
                            }
                            String ban = "1";
                            parametros.put("sin_contrasenia", ban.toString());



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
        String nameImage= String.valueOf(ivFotoP.getTag());
       // String nombreImagen="";

        int band=0;



        if(compararNombre.trim().equals(txtName.getText().toString().trim())&&
                fechaVerif.toString().trim().equals("")&&
                compararApellido.trim().equals(txtApe.getText().toString().trim()) && ivFotoP.getTag().toString().equals("bg1")) {
           // Toast.makeText(DatosPersonalesUsuario.this,compararFecha.toString()+" " +fechaVerif.toString(),Toast.LENGTH_SHORT).show();
            band=0;
        }
            else{

                if(compararNombre.trim().equals(txtName.getText().toString().trim())&&
                        compararFecha.trim().equals(fechaVerif.toString().trim())&&
                        compararApellido.trim().equals(txtApe.getText().toString().trim()) && ivFotoP.getTag().toString().equals("bg1"))
                {
                    //Toast.makeText(DatosPersonalesUsuario.this,compararFecha.toString()+" " +fechaVerif.toString(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(DatosPersonalesUsuario.this,ivFotoP.getTag().toString(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(DatosPersonalesUsuario.this,compararNombre+" "+txtName.getText().toString(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(DatosPersonalesUsuario.this,compararApellido+" "+txtApe.getText().toString(),Toast.LENGTH_SHORT).show();
                    band=0;
                }
                else
                {
                    if(
                            !compararNombre.trim().equals(txtName.getText().toString())||
                                    !compararApellido.trim().equals(txtApe.getText().toString())||
                                    !compararFecha.trim().equals(fechaVerif.toString())||ivFotoP.getTag().toString().equals("bg2")
                    )
                    {
                        // btnGuardarCambios.setEnabled(true);

                        band=1;
                    }

                }


        }


        return band;
    }

    public String getStringImagen(Bitmap bmp) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String  encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public int validarCampos(){

        int respuesta =0;
        String nameImage= String.valueOf(ivFotoP.getTag());

        if(//nameImage.equals("bg1")  &&
                txtName.getText().toString().equals("") && txtApe.getText().toString().equals("") && txtEdad.getText().toString().equals(""))
        //&& txtSexo.getText().toString().equals("")
        {
            Toast.makeText(DatosPersonalesUsuario.this, "Campos vacíos. Por favor ingrese datos", Toast.LENGTH_SHORT).show();
            //txtEmail.setError("Ingrese el correo eletrónico");
            // txtEmail.requestFocus();
            txtName.setError("Ingrese el nombre");
            txtApe.setError("Ingrese el apellido");
            txtEdad.setError("Ingrese la edad");
        }

        else {if(txtName.getText().toString().equals("")){
            txtName.setError("Ingrese el nombre");
            txtName.requestFocus();
        }
        else {if(txtApe.getText().toString().equals("")){
            txtApe.setError("Ingrese el apellido");
            txtApe.requestFocus();
        }
        else {if(txtEdad.getText().toString().equals("")) {
            txtEdad.setError("Ingrese la edad");
            txtEdad.requestFocus();
        }
            else
            {
                if(!txtEdad.getText().toString().equals("") && !txtName.getText().toString().equals("")&& !txtApe.getText().toString().equals("")){
                    respuesta=2;
                }
            }



        }
        }

        }

        if(respuesta==2)
        {
            if(validarNombre()==1&&validarApellido()==1)
                //&&validarEdad()==1
                    //&& validarCaracteresContrasenia()==1&&validarContrasenia()==1)
            {
                //Toast.makeText(registrar.this, "Dtos verif correctos", Toast.LENGTH_SHORT).show();
                respuesta=1;

            }
            else {
                //Toast.makeText(registrar.this, "Alguno da 0", Toast.LENGTH_SHORT).show();
            }

        }
        else{

            //Toast.makeText(registrar.this, "No coinside alguna función", Toast.LENGTH_SHORT).show();
        }


        //}

        return respuesta;
    }
    private int validarEdad(){
        int datCorrecto=0;
        String Edad = txtEdad.getText().toString();

        // Comparar si está en el rango

        int numero = Integer.parseInt(Edad);
        if (numero >= 15 && numero <= 100) {
            //Toast.makeText(registrar.this, "Edad correcta", Toast.LENGTH_SHORT).show();

            datCorrecto=1;
        } else {
            // Si no, entonces indicamos el error y damos focus
            txtEdad.setError("Número fuera de rango(15-100 años)");
            txtEdad.requestFocus();
            datCorrecto=0;
        }



        return datCorrecto;
    }
    private int validarApellido(){
        int datCorrecto=0;
        if(txtApe.getText().toString().length()<80){

            if(txtApe.getText().toString().length()<3)
            {
                Toast.makeText(this, "¡Error! Apellido", Toast.LENGTH_SHORT).show();
                txtApe.setError("Apellido demasiado corto. (Mínimo 3 caracteres)");
                txtApe.requestFocus();
            }
            else {
                datCorrecto=1;
            }

        }
        else {
            Toast.makeText(this, "¡Error! Apellido", Toast.LENGTH_SHORT).show();
            txtApe.setError("Apellido demasiado largo. (Máximo 80 caracteres)");
            txtApe.requestFocus();
        }
        return datCorrecto;
    }

/*
public int validarContraseniaBD(){
        if(compararContrasenia.toString().equals(getSHA256(txtContraseniaAntigua.getText().toString()))){
            contraseniaCorrecta= 1;
            //txtContraseniaAntigua.setError("Contraseña correcta");

            //txtContraseniaNueva.setBackgroundColor(Color.TRANSPARENT);
           // txtContraseniaNueva.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            //txtContraseniaNueva.setDrawingCacheBackgroundColor(Color.BLACK);
            contNuevaContrasenia.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            contConfirmarContra.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        }
        else {
            contContraActual.setError("Contraseña incorrecta");
            contContraActual.requestFocus();
            contContraActual.setErrorIconDrawable(null);

        }

        return contraseniaCorrecta;
}*/

/*
public void validarContraseniaBD(){
    //obtener el correo del usuario logueado
    SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
    String email = preferences.getString("estado_correo", "");

    //URL del web service
    String url = WebService.urlRaiz + WebService.servicioObtenerDatosPersonales;
    //Metodo String Request
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                        String existencia = object.getString("valida");
                        //Toast.makeText(getApplicationContext(), existencia, Toast.LENGTH_SHORT).show();
                        if (existencia.equals("no_existe")) {
                            txtContraseniaAntigua.setError("Contraseña incorrecta");
                        } else {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                if (txtName.getText().toString().trim().equals(obj.getString("NOMBRE_USUARIO"))) {
                                    datosIgualesNombre = 1;
                                }
                                if (txtApe.getText().toString().trim().equals(obj.getString("APELLIDO_USUARIO"))) {
                                    datosIgualesApellido = 1;
                                }
                                if (txtEdad.getText().toString().trim().equals(obj.getString("EDAD"))) {
                                    datosIgualesEdad = 1;
                                }

                                    if (datosIgualesNombre != 0 || datosIgualesApellido != 0 || datosIgualesEdad != 0) {
                                        int cambiosRealizados = 1;
                                        modificarDatos(cambiosRealizados);
                                    } else {
                                        int cambiosRealizados = 0;
                                        modificarDatos(cambiosRealizados);
                                    }

                            }
                        }
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> parametros = new HashMap<String, String>();
            parametros.put("email", email);
            parametros.put("contrasenia",getSHA256(txtContraseniaAntigua.getText().toString().trim()));
            return parametros;
        }
    };

    Volley.newRequestQueue(this).add(stringRequest);
}

*/
    public String getSHA256(String data) {
        return Hashing.sha256().hashString(data, StandardCharsets.UTF_8).toString();
    }

    private int validarNombre(){

        int datCorrecto=0;
        if(txtName.getText().toString().length()<80){
            if(txtName.getText().toString().length()<3)
            {
                Toast.makeText(this, "¡Error! Nombre", Toast.LENGTH_SHORT).show();
                txtName.setError("Nombre demasiado corto. (Mínimo 3 caracteres)");
                txtName.requestFocus();
            }
            else {
                datCorrecto=1;
            }


        }
        else{
            Toast.makeText(this, "¡Error! Nombre", Toast.LENGTH_SHORT).show();
            txtName.setError("Nombre demasiado largo. (Mínimo 80 caracteres)");
            txtName.requestFocus();
        }
        return datCorrecto;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivFotoP.setTag("bg2");
                //Configuración del mapa de bits en ImageView
                int bwidth = bitmap.getWidth();
                int bheight = bitmap.getHeight();
                int swidth = ivFotoP.getWidth();
                int sheight = ivFotoP.getHeight();
                int new_width = swidth;
                int new_height = (int) Math.floor((double) bheight * ((double) new_width / (double) bwidth));
                newbitMap = Bitmap.createScaledBitmap(bitmap, new_width, new_height, true);
                ivFotoP.setImageBitmap(newbitMap);
               // newbitMap.recycle();
                //bitmap.recycle();

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
}
