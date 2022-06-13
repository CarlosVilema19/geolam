package com.riobamba.geolam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IngresoLugarMedico extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText txtTipología, txtCategoria, txtNombreLugar, txtDireccion, txtTelefono, txtWhatsApp, txtPaginaWeb, txtLatitud, txtLongitud, txtDescripcion;
    Button btnGuardarInfo;
    Spinner spinnerCategoria;
    //Imagen
    private Button btnCargarImagen;
    private ImageView ivFotoL;
    private Bitmap bitmap;
    private Bitmap newbitMap;
    private String claveImagen = "foto";
    private String claveNombre = "nombre";
    private int PICK_IMAGE_REQUEST = 1;

    //Items Tipología
    String[] itemsTip = {"Hospital General", "Hospital Básico", "Hospital del Día"};
    ArrayList<String> tipologia = new ArrayList<>();
    AutoCompleteTextView autoCompleteOpcionesTipologia;
    ArrayAdapter<String> adapterItemsTip;

    //Items Categoría
    String[] itemsCat = {"Privado", "Público"};
    ArrayList<String> categoriaList = new ArrayList<>();
    AutoCompleteTextView autoCompleteOpcionesCategoría;
    ArrayAdapter<String> adapterItemsCat;

    //consulta
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_lugar_medico);

        //


        //Imagen
        btnCargarImagen = (Button) findViewById(R.id.btn_cargarfotoL);
        btnGuardarInfo = findViewById(R.id.btn_guardarLugar);
        ivFotoL = findViewById(R.id.imageViewLugar);


        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == btnCargarImagen) {
                    showFileChooser();
                }

                if (v == btnGuardarInfo) {
                    insertarLugar();

                }

            }
        });
        //Base de datos consulta
        requestQueue = Volley.newRequestQueue(this);
        autoCompleteOpcionesCategoría = (AutoCompleteTextView)  findViewById(R.id.opcionesCategoria);
//spinnerCategoria=findViewById(R.id.spinnerCategoria);

     /*   JSONObject jsonObject = null;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, "https://qcqjfcit.lucusvirtual.es/consultaCategoria.php", jsonObject, new Response.Listener<JSONObject>()
        { @Override public void onResponse(JSONObject response) {

            try {
                JSONArray jsonArray = response.getJSONArray("categoria_medica");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String descripcion_categoria = jsonObject.optString("descripcion_categoria");

                    categoriaList.add(descripcion_categoria);
                    adapterItemsCat = new ArrayAdapter<>(IngresoLugarMedico.this,android.R.layout.simple_spinner_item, categoriaList);
                    adapterItemsCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria.setAdapter(adapterItemsCat);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

       /* }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) { }
        });*/
        //requestQueue.add(jsonObjectRequest);
        //spinnerCategoria.setOnItemSelectedListener(this);

/*
            // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://qcqjfcit.lucusvirtual.es/consultaCategoria.php", (String)null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("categoria_medica");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String descripcion_categoria = jsonObject.optString("descripcion_categoria");
                        categoriaList.add(descripcion_categoria);
                        adapterItemsCat = new ArrayAdapter<>(IngresoLugarMedico.this,android.R.layout.simple_spinner_item, categoriaList);
                        adapterItemsCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                       spinnerCategoria.setAdapter(adapterItemsCat);
                        // autoCompleteOpcionesCategoría.setAdapter(adapterItemsCat);

                        /////

                      /* autoCompleteOpcionesCategoría.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemCat = parent.getItemAtPosition(position).toString();
                                Toast.makeText(getApplicationContext(), "Item: " + itemCat, Toast.LENGTH_SHORT).show();

                            }

                        });*/
/*
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Complete todos los campos" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

/*
        requestQueue.add(jsonObjectRequest);
        spinnerCategoria.setOnItemSelectedListener(this);
        //autoCompleteOpcionesCategoría.setOnItemSelectedListener();
*/



///////////////////////////////////
        //Items Tipología Autocomplete
        autoCompleteOpcionesTipologia = (AutoCompleteTextView) findViewById(R.id.opcionesTipologia);
        adapterItemsTip = new ArrayAdapter<String>(this, R.layout.lista_items_tipologia, itemsTip);
        autoCompleteOpcionesTipologia.setAdapter(adapterItemsTip);
        autoCompleteOpcionesTipologia.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemTip = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + itemTip, Toast.LENGTH_SHORT).show();

            }

        });

        //Items Categoría Autocomplete
        autoCompleteOpcionesCategoría = (AutoCompleteTextView) findViewById(R.id.opcionesCategoria);
        adapterItemsCat = new ArrayAdapter<String>(this, R.layout.lista_items, itemsCat);
        autoCompleteOpcionesCategoría.setAdapter(adapterItemsCat);
        autoCompleteOpcionesCategoría.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemCat = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + itemCat, Toast.LENGTH_SHORT).show();

            }

        });

        //otros campos
        txtTipología = findViewById(R.id.opcionesTipologia);
        txtCategoria= findViewById(R.id.opcionesCategoria);
        txtNombreLugar = findViewById(R.id.ingresoNombreLugar);
        txtDireccion = findViewById(R.id.ingresoDireccion);
        txtTelefono = findViewById(R.id.ingresoTelefono);
        txtWhatsApp = findViewById(R.id.ingresoWhatsApp);
        txtPaginaWeb = findViewById(R.id.ingresoPaginaWeb);
        txtLatitud = findViewById(R.id.ingresoLatitud);
        txtLongitud = findViewById(R.id.ingresoLongitud);
        txtDescripcion = findViewById(R.id.ingresoDescripcionLugar);
        btnGuardarInfo= findViewById(R.id.btn_guardarLugar);

        btnGuardarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //General
                insertarLugar();

            }
        });
    }

//Bitmap


    public String getStringImagen(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void insertarLugar() {
        final ProgressDialog loading = ProgressDialog.show(this, "Guardando la información...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tvcpdudx.lucusvirtual.es/insertar_lugar.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                //Mostrando el mensaje de la respuesta
                Toast.makeText(getApplicationContext(), "Operación Exitosa", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                //Showing toast
                Toast.makeText(getApplicationContext(), "Complete todos los campos" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen_lugar = getStringImagen(bitmap); //Imagen
                //Obtener el nombre de la imagen
                String nombreImagen = txtNombreLugar.getText().toString().trim();

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("id_tipologia_lugar", txtTipología.getText().toString());
                parametros.put("id_categoria", txtCategoria.getText().toString());
                parametros.put("nombre_lugar", txtNombreLugar.getText().toString());
                parametros.put("direccion", txtDireccion.getText().toString());
                parametros.put("telefono", txtTelefono.getText().toString());
                parametros.put("whatsapp", txtWhatsApp.getText().toString());
                parametros.put("pagina_web", txtPaginaWeb.getText().toString());
                parametros.put("latitud", txtLatitud.getText().toString());
                parametros.put("longitud", txtLongitud.getText().toString());
                parametros.put("descripcion_lugar", txtDescripcion.getText().toString());

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
