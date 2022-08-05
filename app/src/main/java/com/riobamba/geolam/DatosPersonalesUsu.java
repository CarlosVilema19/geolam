

package com.riobamba.geolam;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
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
import com.google.common.hash.Hashing;
import com.riobamba.geolam.modelo.DatosPersonales;
//import com.riobamba.geolam.modelo.DatosPersonalesAdaptador;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
/*
public class DatosPersonalesUsu extends AppCompatActivity {
    //Declarar la lista y el recycler view
    List<DatosPersonales> datosList;
    RecyclerView recyclerView;
    //DatosPersonalesAdaptador adaptador;
    Toolbar toolbar = new Toolbar(); //asignar el objeto de tipo toolbar
    EditText txtName, txtApe, txtContraseniaAntigua,
            txtEdad, txtContraseniaNueva, txtConfirmarContrasenia;
    Button btnGuardarCambios, btnCancelar;
    String imagen_lugar;
    TextView tvEmail;
    int datosIgualesNombre=0, datosIgualesApellido =0, datosIgualesEdad=0, datosIgualesContrasenia=0;

    private Button btnCargarImagen;
    private ImageView ivFoto;
    private Bitmap bitmap;
    private Bitmap newbitMap;
    private String claveImagen = "foto";
    private String claveNombre = "nombre";
    private int PICK_IMAGE_REQUEST = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_item);

        recyclerView = findViewById(R.id.rvListado);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datosList = new ArrayList<>();
        toolbar.show(this, "Mis datos", true); //Llamar a la clase Toolbar y ejecutar la funcion show() para mostrar la barra superior -- Parametros (Contexto, Titulo, Estado de la flecha de regreso)

        //llamar al mostrar resultado
        //MostrarResultado();

        //Variables - Modificar datos

        txtName =findViewById(R.id.etNombre);
        txtApe =findViewById(R.id.etApellido);
        txtEdad=findViewById(R.id.etEdad);
        txtContraseniaAntigua=findViewById(R.id.etContraAntes);
        txtContraseniaNueva= findViewById(R.id.etNuevaContra);
        txtConfirmarContrasenia=findViewById(R.id.etConfirContra);
        ivFoto=findViewById(R.id.ivPerfil);
        btnGuardarCambios=findViewById(R.id.btnGuardarCambiosUsu);
        btnCancelar=findViewById(R.id.btnCancelar);
        btnCargarImagen=findViewById(R.id.btn_cargarfoto2);
        tvEmail = findViewById(R.id.tvEmailUsu);


        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarDatos(0);
            }
        });


        /*btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == btnCargarImagen) {
                    showFileChooser();
                }
                else {

                    // PICK_IMAGE_REQUEST=1;
                    //insertarUsusario();
                }

            }
        });*/

       //txtContraseniaNueva.isEnabled();
       //txtConfirmarContrasenia.isEnabled();



    //}
/*
    public void MostrarResultado()
    {
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
                                datosList.add(new DatosPersonales(
                                        obj.getString("nombre_usuario"),
                                        obj.getString("apellido_usuario"),
                                        obj.getString("email"),
                                        obj.getString("imagen"),
                                        obj.getString("descripcion_tipo_usuario"),
                                        obj.getInt("id_tipo_usuario"),
                                        obj.getInt("edad")
                                ));
                            }
                            DatosPersonalesAdaptador myadapter = new DatosPersonalesAdaptador(DatosPersonalesUsu.this, datosList,
                                    new DatosPersonalesAdaptador.OnItemClickListener() {
                                        @Override//llamada al método para llamar a una pantalla cuando se presiona sobre el item
                                        public void onItemClick(DatosPersonales item) {moveToGuardar(item);}
                                    }, new DatosPersonalesAdaptador.OnClickListener() {

                                @Override//llamada al método para borrar presionando sobre el botón
                                public void onClick(DatosPersonales item) {
                                    moveToCancelar(item);
                                }
                            });
                            recyclerView.setAdapter(myadapter);

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
*/
/*
    public void verificaDatos()
    {
        //obtener el correo del usuario logueado
        SharedPreferences preferences = getSharedPreferences("correo_email", Context.MODE_PRIVATE);
        String email = preferences.getString("estado_correo","");

        //URL del web service
        String url = WebService.urlRaiz + WebService.servicioObtenerDatosPersonales;
        //Metodo String Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                            String existencia = object.getString("valida");
                            //Toast.makeText(getApplicationContext(), existencia, Toast.LENGTH_SHORT).show();
                            if (existencia.equals("no_existe")) {
                                txtContraseniaAntigua.setError("Contraseña incorrecta");
                            }
                            else{
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
                                if(txtContraseniaAntigua.getText().toString().trim().equals(obj.getString("CONTRASENIA"))){
                                    datosIgualesContrasenia=1;
                                    //txtContraseniaNueva.setVisibility(View.VISIBLE);
                                    //txtConfirmarContrasenia.setVisibility(View.VISIBLE);
                                }
                                if (datosIgualesNombre != 0 || datosIgualesApellido != 0 || datosIgualesEdad != 0) {
                                    int cambiosRealizados=1;
                                    modificarDatos(cambiosRealizados);
                                }
                                else {
                                    int cambiosRealizados=0;
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", email);
                //parametros.put("contrasenia",getSHA256(txtContraseniaAntigua.getText().toString().trim()));
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }
 public void modificarDatos(int cambiosRealizados) {
     if (validarCampos() == 1) {
     if(cambiosRealizados==1){
         String url = WebService.urlRaiz + WebService.servicioModificarDatosPersonales;
         final ProgressDialog loading = ProgressDialog.show(this, "Actualizando la información...", "Espere por favor");
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
                 imagen_lugar = getStringImagen(bitmap); //Imagen

                 //Obtener el nombre de la imagen
                 String nombreImagen = tvEmail.getText().toString().trim();


                 Map<String, String> parametros = new HashMap<String, String>();
                 parametros.put("nombre_usuario", txtName.getText().toString().trim());
                 parametros.put("apellido_usuario", txtApe.getText().toString().trim());
                 parametros.put("edad", txtEdad.getText().toString().trim());
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

     }else {

         Toast.makeText(getApplicationContext(), "No se ha realizado ningún cambio", Toast.LENGTH_SHORT).show();
     }


 }
 }
    public void showFileChooser() {


        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imagen"), PICK_IMAGE_REQUEST);

    }
    public String getStringImagen(Bitmap bmp) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String  encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivFoto.setTag("bg2");
                //Configuración del mapa de bits en ImageView
                int bwidth = bitmap.getWidth();
                int bheight = bitmap.getHeight();
                int swidth = ivFoto.getWidth();
                int sheight = ivFoto.getHeight();
                int new_width = swidth;
                int new_height = (int) Math.floor((double) bheight * ((double) new_width / (double) bwidth));
                newbitMap = Bitmap.createScaledBitmap(bitmap, new_width, new_height, true);
                ivFoto.setImageBitmap(newbitMap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public String getSHA256(String data) {
        return Hashing.sha256().hashString(data, StandardCharsets.UTF_8).toString();
    }
    public int validarCampos(){

        int respuesta =0;
        String nameImage= String.valueOf(ivFoto.getTag());

        if(//nameImage.equals("bg1")  &&
         txtName.getText().toString().equals("") && txtApe.getText().toString().equals("") && txtEdad.getText().toString().equals(""))
                //&& txtSexo.getText().toString().equals("")
                {
            Toast.makeText(DatosPersonalesUsu.this, "Campos vacíos. Por favor ingrese datos", Toast.LENGTH_SHORT).show();
            //txtEmail.setError("Ingrese el correo eletrónico");
           // txtEmail.requestFocus();
            txtName.setError("Ingrese el nombre");
            txtApe.setError("Ingrese el apellido");
            txtEdad.setError("Ingrese la edad");
            //txtSexo.setError("Seleccione el sexo");
            //txtContraseniaNueva.setError("Ingrese la contraseña");
            //txtConfirmarContrasenia.setError("Ingrese nuevamente la contraseña");
        }
        //else{ if(nameImage.equals("bg1")){Toast.makeText(DatosPersonalesUsu.this, "Ingrese una imagen", Toast.LENGTH_SHORT).show();}
        /*else{ if(txtEmail.getText().toString().equals("")){Toast.makeText(registrar.this, "Ingrese el correo", Toast.LENGTH_SHORT).show();
            txtEmail.setError("Ingrese el correo eletrónico");
            txtEmail.requestFocus();
        }*/

            /*
        else {if(txtName.getText().toString().equals("")){
            txtName.setError("Ingrese el nombre");
            txtName.requestFocus();
        }
        else {if(txtApe.getText().toString().equals("")){
            txtApe.setError("Ingrese el apellido");
            txtApe.requestFocus();
        }
        else {if(txtEdad.getText().toString().equals("")){
            txtEdad.setError("Ingrese la edad");
            txtEdad.requestFocus();
        }

            respuesta=2;
        }

        }}

        if(respuesta==2)
        {
            if(validarNombre()==1&&validarApellido()==1&&validarEdad()==1)
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
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=*])"+     // al menos un caracter especial
                    "(?=\\S+$)" +            // sin espacios en blanco
                    ".{10,16}" +                // al menos 10 caracteres
                    "$");

    private int validarCaracteresContrasenia(){
        int datCorrecto=0;
        String passwordInput = txtContraseniaNueva.getText().toString().trim();
        if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            Toast.makeText(DatosPersonalesUsu.this, "La contraseña es demasiado débil o fuera de rango", Toast.LENGTH_SHORT).show();
            txtContraseniaNueva.setError("Ingrese de 10-16 caracteres (Agregar un caracter especial y sin espacios)");
            txtContraseniaNueva.requestFocus();
        }
        else{
            //pass.setError("Contraseña válida");
            datCorrecto = 1;
        }
        return datCorrecto;
    }



    private int validarContrasenia(){
        int datCorrecto=0;
        String contrasenia = txtContraseniaNueva.getText().toString();
        String confirmContrasenia = txtConfirmarContrasenia.getText().toString();

        // Comparar si son iguales
        if (contrasenia.equals(confirmContrasenia)) {
            //Toast.makeText(registrar.this, "La contraseñas coinciden", Toast.LENGTH_SHORT).show();
            datCorrecto=1;
        } else {
            // Si no, entonces indicamos el error y damos focus
            txtConfirmarContrasenia.setError("Las contraseñas no coinciden, ingrese nuevamente");
            txtConfirmarContrasenia.requestFocus();
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
            txtApe.setError("Apellido demasiado largo. (Mínimo 80 caracteres)");
            txtApe.requestFocus();
        }
        return datCorrecto;
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

    public void moveToGuardar(DatosPersonales item)// Método para Guardar
    {
        verificaDatos();
        Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();

    }

    public void moveToCancelar(DatosPersonales item) //Método para Cancelar
    {
        finish();
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
*/