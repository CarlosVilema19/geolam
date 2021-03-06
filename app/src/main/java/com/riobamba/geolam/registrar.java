package com.riobamba.geolam;

import static android.content.Intent.createChooser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Base64;
import android.app.ProgressDialog;
import android.provider.MediaStore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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
import com.google.common.hash.Hashing;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class registrar extends AppCompatActivity {
    EditText txtName, txtEmail, pass,txtApe, txtEdad, txtSexo, confirmPass;
    Button btnInsert;
    TextView login;
    String listCorreoUsuario;
    String[] items = {"Hombre", "Mujer"};
    int validarCorreo=0;

    //Imagen
    private Button btnCargarImagen;
    private ImageView ivFoto;
    private Bitmap bitmap;
    private Bitmap newbitMap;
    private String claveImagen = "foto";
    private String claveNombre = "nombre";
    private int PICK_IMAGE_REQUEST = 1;
    int emailCorrecto = 0;

    //Items Sexo F y M
    AutoCompleteTextView autoCompleteTxtEdSexo;
    ArrayAdapter<String> adapterItems;

    ArrayList<String> opcionesCorreoUsuario = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        login=findViewById(R.id.txsignup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });


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
                else {

                  // PICK_IMAGE_REQUEST=1;
                    //insertarUsusario();
                }

            }
        });


        //Items Sexo F y M Autocomplete
        autoCompleteTxtEdSexo = findViewById(R.id.edsexo);
        adapterItems = new ArrayAdapter<String>(this, R.layout.lista_items, items);
        autoCompleteTxtEdSexo.setAdapter(adapterItems);
        autoCompleteTxtEdSexo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
               // Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();

            }

        });

        //otros campos
        txtEmail = findViewById(R.id.etemail);
        txtName = findViewById(R.id.ednombre);
        txtApe = findViewById(R.id.edapellido);
        txtEdad = findViewById(R.id.ededad);
        txtSexo = findViewById(R.id.edsexo);
        pass = findViewById(R.id.etcontrasenia);
        confirmPass=findViewById(R.id.etconfirmcontrasenia);
        btnInsert = findViewById(R.id.btn_register);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validarCampos()==1)
                {
                    insertarUsusario();
                }
             else
                {
                    btnInsert=findViewById(R.id.btn_register);

                }

            }
        });


    }

    //Bitmap


    public String getStringImagen(Bitmap bmp) {


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String  encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
    }

    public int validarCampos(){

        int respuesta =0;
        String nameImage= String.valueOf(ivFoto.getTag());

            if(nameImage.equals("bg1") && txtEmail.getText().toString().equals("") && txtName.getText().toString().equals("") && txtApe.getText().toString().equals("") && txtEdad.getText().toString().equals("")
                && txtSexo.getText().toString().equals("") && pass.getText().toString().equals("") && confirmPass.getText().toString().equals("")){
                Toast.makeText(registrar.this, "Campos vac??os. Por favor ingrese datos", Toast.LENGTH_SHORT).show();
                txtEmail.setError("Ingrese el correo eletr??nico");
                txtEmail.requestFocus();
                txtName.setError("Ingrese el nombre");
                txtApe.setError("Ingrese el apellido");
                txtEdad.setError("Ingrese la edad");
                //txtSexo.setError("Seleccione el sexo");
                pass.setError("Ingrese la contrase??a");
                confirmPass.setError("Ingrese nuevamente la contrase??a");
            }
            else{ if(nameImage.equals("bg1")){Toast.makeText(registrar.this, "Ingrese una imagen", Toast.LENGTH_SHORT).show();}
            else{ if(txtEmail.getText().toString().equals("")){Toast.makeText(registrar.this, "Ingrese el correo", Toast.LENGTH_SHORT).show();
                txtEmail.setError("Ingrese el correo eletr??nico");
                txtEmail.requestFocus();
            }
            else {if(txtName.getText().toString().equals("")){
                txtName.setError("Ingrese el nombre");
                txtName.requestFocus();
            }
            else {if(txtApe.getText().toString().equals("")){
                txtApe.setError("Ingrese el apellido");
                txtApe.requestFocus();
            }
            else {if(txtEdad.getText().toString().equals("")){

            }
            else {if (txtSexo.getText().toString().equals("")) {
                Toast.makeText(this, "Por favor seleccione el sexo", Toast.LENGTH_SHORT).show();
            }
            else {if (pass.getText().toString().equals("")) {
                pass.setError("Ingrese la contrase??a");
                pass.requestFocus();
            }
            else {if(confirmPass.getText().toString().equals("")){
                confirmPass.setError("Confirme la contrase??a");
                confirmPass.requestFocus();

            }
                respuesta=2;
                }

        }}}}}}}

        if(respuesta==2)
        {
            if(validarEmail()==1&&validarNombre()==1&&validarApellido()==1&&validarEdad()==1&& validarCaracteresContrasenia()==1&&validarContrasenia()==1)
            {
                //Toast.makeText(registrar.this, "Dtos verif correctos", Toast.LENGTH_SHORT).show();
                respuesta=1;

            }
            else {
                //Toast.makeText(registrar.this, "Alguno da 0", Toast.LENGTH_SHORT).show();
            }

        }
        else{

            //Toast.makeText(registrar.this, "No coinside alguna funci??n", Toast.LENGTH_SHORT).show();
        }


    //}

        return respuesta;
    }



    private int validarEdad(){
        int datCorrecto=0;
        String Edad = txtEdad.getText().toString();

        // Comparar si est?? en el rango

            int numero = Integer.parseInt(Edad);
        if (numero >= 15 && numero <= 100) {
            //Toast.makeText(registrar.this, "Edad correcta", Toast.LENGTH_SHORT).show();

            datCorrecto=1;
        } else {
            // Si no, entonces indicamos el error y damos focus
            txtEdad.setError("N??mero fuera de rango(15-100 a??os)");
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
        String passwordInput = pass.getText().toString().trim();
        if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            Toast.makeText(registrar.this, "La contrase??a es demasiado d??bil o fuera de rango", Toast.LENGTH_SHORT).show();
            pass.setError("Ingrese de 10-16 caracteres (Agregar un caracter especial y sin espacios)");
            pass.requestFocus();
        }
        else{
            //pass.setError("Contrase??a v??lida");
            datCorrecto = 1;
        }
        return datCorrecto;
    }



    private int validarContrasenia(){
        int datCorrecto=0;
        String contrasenia = pass.getText().toString();
        String confirmContrasenia = confirmPass.getText().toString();

        // Comparar si son iguales
        if (contrasenia.equals(confirmContrasenia)) {
            //Toast.makeText(registrar.this, "La contrase??as coinciden", Toast.LENGTH_SHORT).show();
            datCorrecto=1;
        } else {
            // Si no, entonces indicamos el error y damos focus
            confirmPass.setError("Las contrase??as no coinciden, ingrese nuevamente");
            confirmPass.requestFocus();
            datCorrecto=0;
        }
        return datCorrecto;
    }

    public int validarEmail() {

        if(emailCorrecto==0&&validarCorreoBD()==1) {

            if (validarCorreo == 1) {
                String emailToText = txtEmail.getText().toString();
                if (emailToText.length() < 40) {
                    if (Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()&&validarCorreo==1) {
                        //Toast.makeText(this, "Correo verificado", Toast.LENGTH_SHORT).show();
                        emailCorrecto = 1;
                    } else {
                        txtEmail.setError("Ingrese un correo v??lido");
                        txtEmail.requestFocus();

                        //Toast.makeText(this, "Ingrese un correo v??lido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "??Error! Correo electr??nico", Toast.LENGTH_SHORT).show();

                    txtEmail.setError("Correo demasiado largo. (M??ximo 40 caracteres)");
                    txtEmail.requestFocus();
                }
            } else {
                txtEmail.setError("Correo ya registrado 99999");

            }

        }
    return emailCorrecto;

    }

    private int validarCorreoBD() {

        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url2 = WebService.urlRaiz + WebService.servicioExistenciaCorreo;
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,

                response ->
                {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            listCorreoUsuario = (object.getString("EMAIL"));
                            opcionesCorreoUsuario.add(listCorreoUsuario);
                            if (validaExistenciaCorreo(opcionesCorreoUsuario) == 1) {
                                validarCorreo = 1;
                            } else {

                                validarCorreo = 0;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, error -> {
            Toast.makeText(this, "Error -->" + error.toString(), Toast.LENGTH_SHORT).show();

        });
        stringRequest2.setTag("REQUEST");
        queue2.add(stringRequest2);
        return validarCorreo;
    }

    private int validaExistenciaCorreo(ArrayList<String> opcionesCorreoUsuario) {
        int correcto=0;
       if( opcionesCorreoUsuario.contains(txtEmail.getText().toString().trim()))
       {

           //Toast.makeText(this, "??Error! Usuario ya registrado", Toast.LENGTH_SHORT).show();
           txtEmail.setError("Usuario ya registrado");
           txtEmail.requestFocus();
       }
       else {

           correcto=1;
       }
    return correcto;

    }

    private int validarApellido(){
        int datCorrecto=0;
        if(txtApe.getText().toString().length()<80){

            if(txtApe.getText().toString().length()<3)
            {
                Toast.makeText(this, "??Error! Apellido", Toast.LENGTH_SHORT).show();
                txtApe.setError("Apellido demasiado corto. (M??nimo 3 caracteres)");
                txtApe.requestFocus();
            }
            else {
                datCorrecto=1;
            }

        }
        else {
            Toast.makeText(this, "??Error! Apellido", Toast.LENGTH_SHORT).show();
            txtApe.setError("Apellido demasiado largo. (M??nimo 80 caracteres)");
            txtApe.requestFocus();
        }
        return datCorrecto;
    }
    private int validarNombre(){

        int datCorrecto=0;
        if(txtName.getText().toString().length()<80){
            if(txtName.getText().toString().length()<3)
            {
                Toast.makeText(this, "??Error! Nombre", Toast.LENGTH_SHORT).show();
                txtName.setError("Nombre demasiado corto. (M??nimo 3 caracteres)");
                txtName.requestFocus();
            }
            else {
                datCorrecto=1;
            }


        }
        else{
            Toast.makeText(this, "??Error! Nombre", Toast.LENGTH_SHORT).show();
            txtName.setError("Nombre demasiado largo. (M??nimo 80 caracteres)");
            txtName.requestFocus();
        }
        return datCorrecto;
    }
    private void insertarUsusario() {

        String url = WebService.urlRaiz+WebService.servicioInsertar;
        final ProgressDialog loading = ProgressDialog.show(this, "Creando perfil...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Descartar el di??logo de progreso
                loading.dismiss();
                //Mostrando el mensaje de la respuesta
                Toast.makeText(getApplicationContext(), "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Descartar el di??logo de progreso
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
              ;
                //Obtener el nombre de la imagen
                String nombreImagen = txtEmail.getText().toString().trim();

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("email", txtEmail.getText().toString().trim());
                parametros.put("nombre_usuario", txtName.getText().toString().trim());
                parametros.put("apellido_usuario", txtApe.getText().toString().trim());
                parametros.put("edad", txtEdad.getText().toString().trim());
                parametros.put("sexo", txtSexo.getText().toString());
                parametros.put("contrasenia", getSHA256(pass.getText().toString().trim()));

                //Imagen

                parametros.put(claveNombre, nombreImagen);
                parametros.put(claveImagen, imagen);




                return parametros;
            }
        };
        //Creaci??n de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    public String getSHA256(String data) {
        return Hashing.sha256().hashString(data, StandardCharsets.UTF_8).toString();
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
                //C??mo obtener el mapa de bits de la Galer??a
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);



                ivFoto.setTag("bg2");


                //Configuraci??n del mapa de bits en ImageView
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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}