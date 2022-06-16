package com.riobamba.geolam;

import static android.content.Intent.createChooser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.app.ProgressDialog;
import android.provider.MediaStore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.riobamba.geolam.modelo.WebService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;

public class registrar extends AppCompatActivity {
    EditText txtName, txtEmail, pass, txtApe, txtEdad, txtSexo;
    Button btnInsert;
    String[] items = {"Hombre", "Mujer"};

    //Imagen
    private Button btnCargarImagen;
    private ImageView ivFoto;
    private Bitmap bitmap;
    private Bitmap newbitMap;
    private String claveImagen = "foto";
    private String claveNombre = "nombre";
    private int PICK_IMAGE_REQUEST = 1;


    //Items Sexo F y M
    AutoCompleteTextView autoCompleteTxtEdSexo;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Imagen
        btnCargarImagen = (Button) findViewById(R.id.btn_cargarfoto);
        btnInsert = findViewById(R.id.btn_register);
        ivFoto = findViewById(R.id.imageViewPerfil);



        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == btnCargarImagen) {
                    showFileChooser();
                }

                if (v == btnInsert) {
                    insertarUsusario();

                }

            }
        });


        //Items Sexo F y M Autocomplete
        autoCompleteTxtEdSexo = (AutoCompleteTextView) findViewById(R.id.edsexo);
        adapterItems = new ArrayAdapter<String>(this, R.layout.lista_items, items);
        autoCompleteTxtEdSexo.setAdapter(adapterItems);
        autoCompleteTxtEdSexo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();

            }

        });

        //otros campos
        txtEmail = findViewById(R.id.etemail);
        txtName = findViewById(R.id.ednombre);
        txtApe = findViewById(R.id.edapellido);
        txtEdad = findViewById(R.id.ededad);
        txtSexo = findViewById(R.id.edsexo);
        pass = findViewById(R.id.etcontrasenia);
        btnInsert = findViewById(R.id.btn_register);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //General
                insertarUsusario();

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


    private void insertarUsusario() {
        String url = WebService.urlRaiz+WebService.servicioInsertar;
        final ProgressDialog loading = ProgressDialog.show(this, "Creando perfil...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                //Mostrando el mensaje de la respuesta
                Toast.makeText(getApplicationContext(), "Operación Exitosa", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Descartar el diálogo de progreso
                loading.dismiss();
                //Showing toast
                Toast.makeText(getApplicationContext(), "Complete todos los campos"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap); //Imagen
                //Obtener el nombre de la imagen
                String nombreImagen = txtEmail.getText().toString().trim();

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("email", txtEmail.getText().toString());
                parametros.put("nombre_usuario", txtName.getText().toString());
                parametros.put("apellido_usuario", txtApe.getText().toString());
                parametros.put("edad", txtEdad.getText().toString());
                parametros.put("sexo", txtSexo.getText().toString());
                parametros.put("contrasenia", pass.getText().toString());

                //Imagen
                parametros.put(claveImagen, imagen);
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
                int bwidth=bitmap.getWidth();
                int bheight=bitmap.getHeight();
                int swidth=ivFoto.getWidth();
                int sheight=ivFoto.getHeight();
                int new_width = swidth;
                int new_height = (int) Math.floor((double) bheight *( (double) new_width / (double) bwidth));
                newbitMap = Bitmap.createScaledBitmap(bitmap,new_width,new_height, true);
                ivFoto.setImageBitmap(newbitMap);


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

    public void login(View v) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}