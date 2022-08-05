package com.riobamba.geolam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.riobamba.geolam.modelo.DatosPersonalesAdaptador;
import com.google.common.hash.Hashing;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DatosPersonalesUsuario extends AppCompatActivity {
    EditText txtName, txtApe, txtContraseniaAntigua,
            txtEdad, txtContraseniaNueva, txtConfirmarContrasenia;
    Button btnGuardarCambios, btnCancelar;
    Button btnVerificarContraseniaAntigua;
    String compararContrasenia=null;
    String imagen_lugar;
    TextView tvEmail;
    int contraseniaCorrecta = 0;
//Imagen

    private Button btnCargarImagen;

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


        //Variables
        txtName =findViewById(R.id.etNombre);
        txtApe =findViewById(R.id.etApellido);
        txtEdad=findViewById(R.id.etEdad);
        txtContraseniaAntigua=findViewById(R.id.etContraAntes);
        txtContraseniaNueva= findViewById(R.id.etNuevaContra);
        txtConfirmarContrasenia=findViewById(R.id.etConfirContra);
        ivFotoP=findViewById(R.id.ivPerfil);
        btnGuardarCambios=findViewById(R.id.btnGuardarCambiosUsu);
        btnCancelar=findViewById(R.id.btnCancelar);
        btnCargarImagen=findViewById(R.id.btn_cargarf2);
        tvEmail = findViewById(R.id.tvEmailUsu);
        btnVerificarContraseniaAntigua=findViewById(R.id.btnVerificarContrasenia);
        txtConfirmarContrasenia.setEnabled(false);
        txtContraseniaNueva.setEnabled(false);



        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                modificarDatos();
            }
        });
        btnVerificarContraseniaAntigua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambioContrasenia();
            }
        });
        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.equals(btnCargarImagen) ) {
                    showFileChooser();
                }


            }
        });
MostrarResultado();

    }
    public void showFileChooser() {


        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imagen"), PICK_IMAGE_REQUEST);

    }
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
                                String urlImage=obj.getString("imagen");
                                imagenReturn(urlImage);
                                tvEmail.setText(obj.getString("email"));
                                txtName.setText(obj.getString("nombre_usuario"));
                                txtApe.setText(obj.getString("apellido_usuario"));
                                txtEdad.setText(obj.getString("edad"));
                                compararContrasenia=obj.getString("contrasenia");
                                Toast.makeText(getApplicationContext(), compararContrasenia.toString(), Toast.LENGTH_SHORT).show();
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

    private void imagenReturn(String url) {
        if(url.contains(WebService.imagenRaiz)) {
            urlSinEspacios = url.replace(" ", "%20");
            String data = urlSinEspacios;
            String[] split = data.split(WebService.imagenRaiz);
            ruta = null;
            for (int i = 0; i < split.length; i++) {
                ruta = split[1];
            }
            urlImagenLugar= WebService.urlRaiz+ruta;
            //Toast.makeText(getApplicationContext(), "Item: " + ruta, Toast.LENGTH_SHORT).show();
        }
        else{
            urlImagenLugar=urlSinEspacios;
        }

        //Toast.makeText(actualizar_lugar_medico.this,urlImagenLugar, Toast.LENGTH_SHORT).show();

        RequestQueue request4 = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(urlImagenLugar, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ivFotoP.setImageBitmap(response);

            }
        }, 0, 0,ImageView.ScaleType.CENTER, null,new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();

            }

        });


        request4.add(imageRequest);

    }

    public void modificarDatos() {
        if (validarCampos() == 1) {
            if(cambioContrasenia()==1){
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
                       // parametros.put("contrasenia",getSHA256(txtContraseniaNueva.getText().toString()));
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
                if(cambioContrasenia()==2)
                {
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
                            parametros.put("contrasenia",getSHA256(txtContraseniaNueva.getText().toString()));
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
                else {
                    Toast.makeText(getApplicationContext(), "No se ha realizado ningún cambio", Toast.LENGTH_SHORT).show();
                }

            }



        }
    }
    public String getStringImagen(Bitmap bmp) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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
public int cambioContrasenia(){
        int respuesta=0;
        if(txtContraseniaAntigua.getText().toString().equals("")){
            respuesta=1;
        }
        else
        {
           if(validarContraseniaBD()==1){

               respuesta =2;
               txtContraseniaNueva.setEnabled(true);
               txtConfirmarContrasenia.setEnabled(true);

           }
        }
        return respuesta;
}

public int validarContraseniaBD(){
        if(compararContrasenia.toString().equals(getSHA256(txtContraseniaAntigua.getText().toString()))){
            contraseniaCorrecta= 1;
            //txtContraseniaAntigua.setError("Contraseña correcta");
        }
        else {
            txtContraseniaAntigua.setError("Contraseña incorrecta");
        }

        return contraseniaCorrecta;
}

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
    public void verificaDatos()
    {
        if(cambioContrasenia()==1) {
        }
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


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
