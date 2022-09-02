package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.ListadoLugarAdmin;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class actualizar_lugar_medico extends AppCompatActivity {
    EditText txtTipologia, txtCategoria, txtNombreLugar, txtDireccion, txtTelefono, txtWhatsApp, txtPaginaWeb, txtLatitud, txtLongitud, txtDescripcion;
    TextView tvIdTipo, tvIdCategoria,tvIdLugarMedico;
    int  posTipologia;
    String pTip, pCat,imagen_lugar,listIDCat,listIDTipo,ruta,urlImagenLugar,urlSinEspacios;
    Button btnGuardarInfo, btnCancelarInfo;
    Integer aux = 0;
    String[] datosServidor;

    //Imagen
    private Button btnCargarImagen;
    private ImageView ivFotoL;
    private Bitmap bitmap;

    private final String claveImagen = "foto";
    private final String claveNombre = "nombre";
    private final int PICK_IMAGE_REQUEST = 1;

    ArrayList<String> opcionesTipologia = new ArrayList<>();
    ArrayList<String> opcionesCategoria= new ArrayList<>();
    String listCatNombres;
    ArrayList<String> opcionesCategoriaNombres=new ArrayList<>();

    String listTipologiasNombres;
    ArrayList<String> opcionesTipologiaNombres= new ArrayList<>();

    AutoCompleteTextView autoCompleteOpcionesCategoria;
    AutoCompleteTextView autoCompleteOpcionesTipologia;

    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_lugar_medico);

        toolbar.show(this, "Actualizar Datos", false); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        //adaptadorCategoria = new ListadoCategoriaAdaptador(this);
        autoCompleteOpcionesCategoria=findViewById(R.id.autoCat2);

        //adaptadorTipo= new ListadoTipologiaAdaptador(this);
        autoCompleteOpcionesTipologia=findViewById(R.id.autoTipologia2);

        //Adaptador
        //autoCompleteOpcionesCategoria.setAdapter(adaptadorCategoria);

       // autoCompleteOpcionesTipologia.setAdapter(adaptadorTipo);

        tvIdLugarMedico=findViewById(R.id.TextViewIDLugar_);
        txtTipologia = findViewById(R.id.autoTipologia2);
        txtCategoria= findViewById(R.id.autoCat2);
        txtNombreLugar = findViewById(R.id.ingresoNombreLugar);
        txtDireccion = findViewById(R.id.ingresoDireccion);
        txtTelefono = findViewById(R.id.ingresoTelefono);
        txtWhatsApp = findViewById(R.id.ingresoWhatsApp);
        txtPaginaWeb = findViewById(R.id.ingresoPaginaWeb);
        txtLatitud = findViewById(R.id.ingresoLatitud);
        txtLongitud = findViewById(R.id.ingresoLongitud);
        txtDescripcion = findViewById(R.id.ingresoDescripcionLugar);
        tvIdTipo = (TextView) findViewById(R.id.TextViewIDTipologia2);
        tvIdCategoria = (TextView) findViewById(R.id.TextViewIDCategoria2);
        btnGuardarInfo= findViewById(R.id.btn_guardarLugar);
        btnCargarImagen = (Button) findViewById(R.id.btn_cargarfotoL);
        ivFotoL = findViewById(R.id.imageViewLugar);


        btnGuardarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //General
                if(validarCampos()==4)
                {
                    if(aux == 1 &&
                            datosServidor[0].equals(txtNombreLugar.getText().toString())&&
                            datosServidor[1].equals(txtDireccion.getText().toString())&&
                            datosServidor[2].equals(txtTelefono.getText().toString())&&
                            datosServidor[3].equals(txtWhatsApp.getText().toString())&&
                            datosServidor[4].equals(txtPaginaWeb.getText().toString())&&
                            datosServidor[5].equals(txtLatitud.getText().toString())&&
                            datosServidor[6].equals(txtLongitud.getText().toString())&&
                            datosServidor[7].equals(txtDescripcion.getText().toString())&&
                            datosServidor[8].equals(txtTipologia.getText().toString())&&
                            datosServidor[9].equals(txtCategoria.getText().toString()))
                    {
                        Toast.makeText(actualizar_lugar_medico.this, "No se ha realizado cambios", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        insertarLugar();
                    }
                }
            }
        });

        btnCancelarInfo = findViewById(R.id.btnCancelarAct);
        btnCancelarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(aux == 0 ||
                        !datosServidor[0].equals(txtNombreLugar.getText().toString())||
                        !datosServidor[1].equals(txtDireccion.getText().toString())||
                        !datosServidor[2].equals(txtTelefono.getText().toString())||
                        !datosServidor[3].equals(txtWhatsApp.getText().toString())||
                        !datosServidor[4].equals(txtPaginaWeb.getText().toString())||
                        !datosServidor[5].equals(txtLatitud.getText().toString())||
                        !datosServidor[6].equals(txtLongitud.getText().toString())||
                        !datosServidor[7].equals(txtDescripcion.getText().toString())||
                        !datosServidor[8].equals(txtTipologia.getText().toString())||
                        !datosServidor[9].equals(txtCategoria.getText().toString()))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(actualizar_lugar_medico.this);
                    builder.setMessage("Se perderán todos los cambios realizados ¿Desea continuar?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    Intent intent = new Intent(actualizar_lugar_medico.this,ListadoCrud.class);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
                else
                {
                    finish();
                    Intent intent = new Intent(actualizar_lugar_medico.this,ListadoCrud.class);
                    startActivity(intent);
                }
            }
        });

        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.equals(btnCargarImagen) ) {
                    showFileChooser();
                    aux = 0;
                }
            }
        });

        //Lugar Médico
        ListadoLugarAdmin listadoLugar = (ListadoLugarAdmin) getIntent().getSerializableExtra("ListadoLugarAdmin");
        actualizarDatos(listadoLugar);


    }
    @Override public void onBackPressed() { }  //Anula la flecha de regreso del telefono

    private void actualizarDatos(ListadoLugarAdmin listadoLugar) {
        ProgressDialog loading; //Mensaje de carga en el mapa
        loading = ProgressDialog.show(actualizar_lugar_medico.this, "Cargando...", "Espere por favor");
        tipologia();
        new Handler().postDelayed(new Runnable() {  //tiempo para cargar la categoria
            @Override
            public void run() {
                categoria();
                new Handler().postDelayed(new Runnable() {  //tiempo para cargar toda la información
                    @Override
                    public void run() {
                        loading.dismiss();
                        String id_lugar = listadoLugar.getId().toString();
                        String url3=WebService.urlRaiz+WebService.servicioObtenerDatosLugarMedico;
                        StringRequest stringRequest3= new StringRequest(Request.Method.POST,url3,
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
                                            String urlImage=object.getString("IMAGEN_LUGAR");
                                            imagenReturn(urlImage);
                                            tvIdTipo.setText(object.getString("ID_TIPOLOGIA_LUGAR").toString());
                                            tvIdCategoria.setText(object.getString("ID_CATEGORIA").toString());
                                            txtWhatsApp.setText(object.getString("WHATSAPP"));
                                            txtPaginaWeb.setText(object.getString("PAGINA_WEB"));
                                            txtLatitud.setText(object.getString("LATITUD"));
                                            txtLongitud.setText(object.getString("LONGITUD"));
                                            txtDescripcion.setText(object.getString("DESCRIPCION_LUGAR"));

                                            datosServidor = new String[10];

                                                datosServidor[0] = object.getString("NOMBRE_LUGAR");
                                                datosServidor[1] = object.getString("DIRECCION");
                                                datosServidor[2] = object.getString("TELEFONO");
                                                datosServidor[3] = object.getString("WHATSAPP");
                                                datosServidor[4] = object.getString("PAGINA_WEB");
                                                datosServidor[5] = object.getString("LATITUD");
                                                datosServidor[6] = object.getString("LONGITUD");
                                                datosServidor[7] = object.getString("DESCRIPCION_LUGAR");
                                                datosServidor[8] = autoCompleteOpcionesTipologia.getAdapter().getItem(seleccionTipologia()).toString();
                                                datosServidor[9] = autoCompleteOpcionesCategoria.getAdapter().getItem(seleccionCategoria()).toString();


                                        }
                                        autoCompleteOpcionesTipologia.setText(autoCompleteOpcionesTipologia.getAdapter().getItem(seleccionTipologia()).toString(),false);
                                        autoCompleteOpcionesCategoria.setText(autoCompleteOpcionesCategoria.getAdapter().getItem(seleccionCategoria()).toString(),false);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                },error -> {Toast.makeText(actualizar_lugar_medico.this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> parametros = new HashMap<String, String>();
                                parametros.put("id_lugar", id_lugar);
                                return parametros;
                            }
                        };
                        stringRequest3.setTag("REQUEST");
                        RequestQueue queue3= Volley.newRequestQueue(actualizar_lugar_medico.this);
                        queue3.add(stringRequest3);
                    }
                },1000);
            }
        },1000);
    }

    private void categoria() {
        //Conexión al Servidor- Consulta AutoComplete Categoría


        String url2=WebService.urlRaiz+WebService.servicioListarCategoria;
        //adaptadorTipo.clear();
        StringRequest stringRequest2= new StringRequest(Request.Method.GET,url2,
                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            //JSONObject object2 = array.getJSONObject(i);
                            //ListadoCategoria cat=new ListadoCategoria(object);
                            //adaptadorCategoria.add(cat);
                            listCatNombres=(object.getString("DESCRIPCION_CATEGORIA"));
                            opcionesCategoriaNombres.add(listCatNombres);
                            listIDCat = ( object.getString("ID_CATEGORIA"));
                            opcionesCategoria.add(listIDCat);
                            ArrayAdapter adapter;
                            adapter=new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, opcionesCategoriaNombres);
                            autoCompleteOpcionesCategoria.setAdapter(adapter);


                        }

                        autoCompleteOpcionesCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemCat = parent.getItemAtPosition(position).toString();
                                //String a=itemCat;
                                //idCategoria =RetornaID(a);
                                //Toast.makeText(getApplicationContext(), "Item: " + idCategoria, Toast.LENGTH_SHORT).show();

                                String c= opcionesCategoria.get(position);
                                pCat=c;
                                retornaIdCategoria(pCat);
                               // Toast.makeText(getApplicationContext(), "Item Categoria: " +pCat, Toast.LENGTH_SHORT).show();

                            }

                        });


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        RequestQueue queue2= Volley.newRequestQueue(this);
        stringRequest2.setTag("REQUEST");
        queue2.add(stringRequest2);



    }

    private void tipologia() {
        //Conexión al Servidor- Consulta AutoComplete Tipología

        String url=WebService.urlRaiz+WebService.servicioListarTipologia;
        //adaptadorTipo.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.GET,url,
                response ->
                {
                    try{


                        JSONArray array= new JSONArray(response);
                        for(int i=0;i<array.length();i++){

                            JSONObject object = array.getJSONObject(i);
                            //JSONObject object2 = array.getJSONObject(i);
                            //Carga de datos
                            //adaptadorTipo.add(tipo);
                            listTipologiasNombres=(object.getString("DESCRIPCION_TIPO_LUGAR"));
                            opcionesTipologiaNombres.add(listTipologiasNombres);
                            listIDTipo = ( object.getString("ID_TIPOLOGIA_LUGAR"));
                            opcionesTipologia.add(listIDTipo);
                            ArrayAdapter adapter;
                            adapter=new ArrayAdapter<String> (this, android.R.layout.simple_dropdown_item_1line, opcionesTipologiaNombres);
                            autoCompleteOpcionesTipologia.setAdapter(adapter);

                        }

                        autoCompleteOpcionesTipologia.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemTipo = parent.getItemAtPosition(position).toString();


                                String p= String.valueOf(position).trim();

                                String c= opcionesTipologia.get(position);

                                pTip=c;

                                retornaIdTipologia(pTip);


                            }

                        });



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },error -> {Toast.makeText(this,"Error -->"+ error.toString(),Toast.LENGTH_SHORT).show();

        });
        stringRequest.setTag("REQUEST");
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private int seleccionCategoria() {
       int  posCategoria =0;
        for(int i = 0; i<opcionesCategoria.size(); i++){
            if(opcionesCategoria.get(i).equals(tvIdCategoria.getText().toString())){
                posCategoria =i;
            }
        }

       // Toast.makeText(getApplicationContext(), "Item Categoria: " + posCategoria, Toast.LENGTH_SHORT).show();
        return posCategoria;
    }

    private int seleccionTipologia() {
      int  posTipologia =0;
        for(int j = 0; j<opcionesTipologia.size(); j++){
            if(opcionesTipologia.get(j).equals(tvIdTipo.getText().toString())){
                posTipologia =j;
            }
        }

       // Toast.makeText(getApplicationContext(), "Item Tipo: " + posTipologia, Toast.LENGTH_SHORT).show();
       return posTipologia;
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
                Bitmap newbitMap = Bitmap.createScaledBitmap(bitmap, new_width, new_height, true);
                ivFotoL.setImageBitmap(newbitMap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        }
        else{
            urlImagenLugar=urlSinEspacios;
        }

        Picasso.get().load(urlImagenLugar).fit().centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(ivFotoL);
        loading2.dismiss();

        //Toast.makeText(actualizar_lugar_medico.this,urlImagenLugar, Toast.LENGTH_SHORT).show();
/*
        RequestQueue request4 = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(urlImagenLugar, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ivFotoL.setImageBitmap(response);

            }
        }, 0, 0,ImageView.ScaleType.CENTER, null,new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();

            }

        });
*/

       // request4.add(imageRequest);
        band=1;
        return band;

    }
    public String getStringImagen(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
    public int validarCampos(){
        int respuesta =0;
        String nameImage= String.valueOf(ivFotoL.getTag());

        if(/*nameImage.equals("bg1")&&*/txtNombreLugar.getText().toString().equals("")
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
        else {if(txtNombreLugar.getText().toString().equals("")){Toast.makeText(actualizar_lugar_medico.this, "Ingrese el nombre", Toast.LENGTH_SHORT).show();
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
        }}}}//}
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
                if(nameImage.equals("bg1")){
                    aux = 1; // para saber si no se ha cambiado la imagen
                }
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
            txtDireccion.setError("Dirección demasiada larga. (Máximo 40 caracteres)");
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
        String url = WebService.urlRaiz +WebService.servicioActualizarLugar;
        final ProgressDialog loading = ProgressDialog.show(this, "Actualizando la información...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                //Mostrando el mensaje de la respuesta
                Toast.makeText(getApplicationContext(), "Se ha actualizado el lugar correctamente", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(actualizar_lugar_medico.this,ListadoCrud.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                //Showing toast
                Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id_lugar", tvIdLugarMedico.getText().toString());
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
                parametros.put("existencia_imagen", String.valueOf(aux));

                if(aux == 0)
                {
                    //Convertir bits a cadena
                    imagen_lugar = getStringImagen(bitmap); //Imagen

                    //Obtener el nombre de la imagen
                    String nombreImagen = txtNombreLugar.getText().toString().trim();
                    //Imagen
                    parametros.put(claveImagen, imagen_lugar);
                    parametros.put(claveNombre, nombreImagen);
                }
                return parametros;
            }
        };
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
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