package com.riobamba.geolam;

import static android.content.Intent.createChooser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Base64;
import android.app.ProgressDialog;
import android.provider.MediaStore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.Proceso;
import com.riobamba.geolam.modelo.Toolbar;
import com.riobamba.geolam.modelo.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class registrar extends AppCompatActivity {
    EditText txtName, txtEmail, pass,txtApe, txtEdad, txtSexo, confirmPass;
    Button btnInsert;
    Button login;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    String listCorreoUsuario;
    String[] items = {"Hombre", "Mujer"};
    ProgressDialog loading, loading2;
    Toolbar toolbar = new Toolbar();
    //Imagen
    private Button btnCargarImagen;
    private ImageView ivFoto;
    private Bitmap bitmap;
    private Bitmap newbitMap;
    private String claveImagen = "foto";
    private String claveNombre = "nombre";
    private int PICK_IMAGE_REQUEST = 1;
    String edadUsu = "";
    private int dia,mes,anio;
    Button btnFechaIngreso,btnCancelar;
    EditText txtFechaingreso;
    String edadCalculada;
    String fechaNac;
    //Items Sexo F y M
    AutoCompleteTextView autoCompleteTxtEdSexo;
    ArrayAdapter<String> adapterItems;
    ArrayList<String> opcionesCorreoUsuario = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        login=findViewById(R.id.txsignup);
        btnCancelar = findViewById(R.id.btnCancelar);

        toolbar.show(this,"Registro",false);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });


        //Calendario Edad

        txtFechaingreso = findViewById(R.id.edFecha);
        txtEdad = findViewById(R.id.ededad);
        btnFechaIngreso = findViewById(R.id.btnFechaIngreso);
        Proceso proc = new Proceso();

        btnFechaIngreso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v==btnFechaIngreso)
                {
                    final Calendar calen = Calendar.getInstance();
                    dia = calen.get(Calendar.DAY_OF_MONTH);
                    mes = calen.get(Calendar.MONTH);
                    anio = calen.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(registrar.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String fecha = dayOfMonth+"/"+(month+1)+"/"+year;
                            DecimalFormat formato2 = new DecimalFormat("#00");
                            fechaNac = year+formato2.format((month+1))+formato2.format(dayOfMonth);
                            txtFechaingreso.setText(fecha);
                            //Edad en en anios y meses
                            edadCalculada = proc.calcularEdad(year,month+1,dayOfMonth);
                            //Edad en anios
                            edadUsu = proc.calcularEdadAnios(year,month+1,dayOfMonth);
                            txtEdad.setText(edadCalculada);
                            txtEdad.setError(null);


                        }
                    },dia,mes,anio);
                    datePickerDialog.show();
                    datePickerDialog.getDatePicker().setMaxDate(new Date().getTime()-(proc.calcularAniosMili(15)));
                    datePickerDialog.getDatePicker().setMinDate(new Date().getTime()-(proc.calcularAniosMili(100)));
                }

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
        txtSexo = findViewById(R.id.edsexo);
        pass = findViewById(R.id.etcontrasenia);
        confirmPass=findViewById(R.id.etconfirmcontrasenia);
        btnInsert = findViewById(R.id.btn_register);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading2 = ProgressDialog.show(registrar.this, "Validando...", "Espere por favor");
                validarCorreoBD();
            }
        });


    }

    private void regresar()
    {
        if (verificarSimilitud() == 0) {
            Intent intent = new Intent(registrar.this, Login.class);
            startActivity(intent);
        } else {
            int icon  = R.drawable.peligro;
            AlertDialog.Builder builder = new AlertDialog.Builder(registrar.this);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        toolbar.getContexto(this);
        toolbar.ejecutarItemSelected(item, this);
        return super.onOptionsItemSelected(item);
    }

    //Bitmap

    private int verificarSimilitud()
    {
        int exis = 0;
        if(ivFoto.getTag().toString().equals("bg2")||
                !txtEmail.getText().toString().equals("")||
                !txtFechaingreso.getText().toString().equals("")||
                !txtApe.getText().toString().equals("")||
                !txtSexo.getText().toString().equals("")||
                !txtEdad.getText().toString().equals("")||
                !pass.getText().toString().equals("")||
                !confirmPass.getText().toString().equals(""))
        {
            exis =1;
        }
        return exis;
    }


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
            if(nameImage.equals("bg1") &&
                    txtEmail.getText().toString().equals("")&&
                    txtName.getText().toString().equals("") &&
                    txtApe.getText().toString().equals("") &&
                    txtEdad.getText().toString().equals("") &&
                    txtSexo.getText().toString().equals("") &&
                    pass.getText().toString().equals("") &&
                    confirmPass.getText().toString().equals("")){

                Toast.makeText(registrar.this, "Campos vacíos. Por favor ingrese datos", Toast.LENGTH_SHORT).show();
                txtEmail.setError("Ingrese el correo eletrónico");
                txtEmail.requestFocus();
                txtName.setError("Ingrese el nombre");
                txtApe.setError("Ingrese el apellido");
                txtEdad.setError("Ingrese la edad");
                txtSexo.setError("Seleccione el sexo");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtSexo.setError(null);
                    }
                },2000);

                pass.setError("Ingrese la contraseña");
                confirmPass.setError("Ingrese nuevamente la contraseña");
            }
            else  {respuesta = 1;}
        return respuesta;
    }

    private int validarImagen()
    {
        int datCorrecto=0;
        String nameImage= String.valueOf(ivFoto.getTag());
        if(nameImage.equals("bg1")){
            Toast.makeText(registrar.this, "Ingrese una imagen", Toast.LENGTH_SHORT).show();
        }
        else {
            datCorrecto=1;
        }
        return datCorrecto;
    }

    private int validarSexo()
    {
        int datCorrecto=0;
        if (txtSexo.getText().toString().equals("")) {
            Toast.makeText(this, "Por favor seleccione el sexo", Toast.LENGTH_SHORT).show();
            txtSexo.setError("Seleccione el sexo");
            txtSexo.requestFocus();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtSexo.setError(null);
                }
            },2000);
        }
        else {
            datCorrecto=1;
        }
        return datCorrecto;
    }

    private int validarEdad()
    {
        int datCorrecto=0;
        if (txtEdad.getText().toString().equals("")) {
            Toast.makeText(this, "Por favor seleccione la edad", Toast.LENGTH_SHORT).show();
            txtEdad.setError("Ingrese la edad");
            txtEdad.requestFocus();
        }
        else {
            datCorrecto=1;
        }
        return datCorrecto;
    }

    private int validarRegistro()
    {
        int respuesta = 0;
            int valNom = validarNombre();
            int valApe = validarApellido();
            int valSex = validarSexo();
            int valEdad = validarEdad();
            int valCon = validarCaracteresContrasenia();
            int valConfCon = validarContrasenia();
            int valIma = validarImagen();
            int valEmail = validarEmail();

            if(valIma == 1 && valNom == 1 && valApe == 1 && valEmail == 1 && valSex == 1 && valEdad == 1 && valCon == 1 && valConfCon == 1)
            {
                respuesta = 1;
            }
        return respuesta;
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
        if (pass.getText().toString().equals("")) {
            pass.setError("Ingrese la contraseña",null);
            pass.requestFocus();
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            Toast.makeText(registrar.this, "La contraseña es demasiado débil o fuera de rango", Toast.LENGTH_SHORT).show();
            pass.setError("Ingrese de 10-16 caracteres (Agregar un caracter especial y sin espacios)",null);
            pass.requestFocus();
        }
        else{
            //pass.setError("Contraseña válida");
            datCorrecto = 1;
        }
        return datCorrecto;
    }



    private int validarContrasenia(){
        int datCorrecto=0;
        String contrasenia = pass.getText().toString();
        String confirmContrasenia = confirmPass.getText().toString();
        if (confirmPass.getText().toString().equals("")) {
            confirmPass.setError("Confirme la contraseña",null);
            confirmPass.requestFocus();

        }else if (contrasenia.equals(confirmContrasenia)) {
            //Toast.makeText(registrar.this, "La contraseñas coinciden", Toast.LENGTH_SHORT).show();
            datCorrecto=1;
        } else {
            // Si no, entonces indicamos el error y damos focus
            confirmPass.setError("Las contraseñas no coinciden, ingrese nuevamente",null);
            confirmPass.requestFocus();
        }
        return datCorrecto;
    }

    public int validarEmail() {
        int emailCorrecto = 0;
                String emailToText = txtEmail.getText().toString();
                if (emailToText.length() < 40) {
                    if(txtEmail.getText().toString().equals("")){
                        Toast.makeText(registrar.this, "Ingrese el correo", Toast.LENGTH_SHORT).show();
                        txtEmail.setError("Ingrese el correo eletrónico");
                        txtEmail.requestFocus();
                    }
                    else if (Pattern.compile(" {1,}").matcher(txtEmail.getText().toString()).find()) {
                        txtEmail.setError("¡Verifique que no haya espacios en blanco!");
                        txtEmail.requestFocus();
                    }else if (Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
                        //Toast.makeText(this, "Correo verificado", Toast.LENGTH_SHORT).show();
                        emailCorrecto = 1;
                    } else {
                        txtEmail.setError("Ingrese un correo válido");
                        txtEmail.requestFocus();

                        //Toast.makeText(this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    txtEmail.setError("Correo demasiado largo. (Máximo 40 caracteres)");
                    txtEmail.requestFocus();
        }
    return emailCorrecto;

    }

    private void validarCorreoBD() {
       // loading2 = ProgressDialog.show(this, "Validando...", "Espere por favor");

        if(validarCampos() == 1)
        {
            if(validarRegistro() == 1)
            {
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
                                }
                                if (validaExistenciaCorreo(opcionesCorreoUsuario) == 1) {
                                    insertarUsusario();
                                    loading2.dismiss();
                                }
                                else{
                                    loading2.dismiss();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                loading2.dismiss();
                            }

                        }, error -> {
                    Toast.makeText(this, "Error del servidor", Toast.LENGTH_SHORT).show();
                    loading2.dismiss();
                });
                stringRequest2.setTag("REQUEST");
                queue2.add(stringRequest2);
            } else loading2.dismiss();
        }else loading2.dismiss();
    }

    private int validaExistenciaCorreo(ArrayList<String> opcionesCorreoUsuario) {
        int correcto=0;
       if( opcionesCorreoUsuario.contains(txtEmail.getText().toString().trim().toLowerCase()))
       {
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
            if(txtApe.getText().toString().equals("")){
                txtApe.setError("Ingrese el apellido");
                txtApe.requestFocus();
            }
            else if (Pattern.compile(" {2,}").matcher(txtApe.getText().toString()).find()) {
                txtApe.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtApe.requestFocus();
            }else if(txtApe.getText().toString().length()<3)
            {
                txtApe.setError("Apellido demasiado corto. (Mínimo 3 caracteres)");
                txtApe.requestFocus();
            }
            else {
                datCorrecto=1;
            }
        }
        else {
            txtApe.setError("Apellido demasiado largo. (Máximo 80 caracteres)");
            txtApe.requestFocus();
        }
        return datCorrecto;
    }
    private int validarNombre(){
        int datCorrecto=0;
        if(txtName.getText().toString().length()<80){
            if(txtName.getText().toString().equals("")){
                txtName.setError("Ingrese el nombre");
                txtName.requestFocus();
            }
            else if (Pattern.compile(" {2,}").matcher(txtName.getText().toString()).find()) {
                txtName.setError("¡Verifique que no haya más de un espacio en blanco!");
                txtName.requestFocus();
            }else if(txtName.getText().toString().length()<3)
            {
                txtName.setError("Nombre demasiado corto. (Mínimo 3 caracteres)");
                txtName.requestFocus();
            }
            else {
                datCorrecto=1;
            }
        }
        else{
            txtName.setError("Nombre demasiado largo. (Máximo 80 caracteres)");
            txtName.requestFocus();
        }
        return datCorrecto;
    }
    private void insertarUsusario() {
         String url = WebService.urlRaiz + WebService.servicioInsertar;
           StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Descartar el diálogo de progreso
                    loading2.dismiss();
                    //Mostrando el mensaje de la respuesta
                    finish();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    Toast.makeText(getApplicationContext(), "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Descartar el diálogo de progreso
                    loading2.dismiss();
                    //Showing toast
                    Toast.makeText(getApplicationContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("email", txtEmail.getText().toString().trim().toLowerCase());
                    parametros.put("nombre_usuario", txtName.getText().toString().trim().toUpperCase());
                    parametros.put("apellido_usuario", txtApe.getText().toString().trim().toUpperCase());
                    parametros.put("edad", edadUsu);
                    parametros.put("sexo", txtSexo.getText().toString().toUpperCase());
                    parametros.put("contrasenia", getSHA256(pass.getText().toString().trim()));
                    parametros.put("fecha_nacimiento", fechaNac);//String.valueOf(fechaNacimiento));

                    //Imagen
                    //Convertir bits a cadena
                    //bitmap = ivFoto.getDrawingCache();
                    String imagen = getStringImagen(bitmap); //Imagen
                    //Obtener el nombre de la imagen
                    String nombreImagen = txtEmail.getText().toString().trim();

                    parametros.put(claveNombre, nombreImagen);
                    parametros.put(claveImagen, imagen);

                    return parametros;
                }
            };
            //Creación de una cola de solicitudes
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