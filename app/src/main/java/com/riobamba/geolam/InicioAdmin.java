package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.riobamba.geolam.Utility.NetworkChangeListener;
import com.riobamba.geolam.modelo.Toolbar;

import java.util.ArrayList;

public class InicioAdmin extends AppCompatActivity {
Button btnGestionLugar, btnGestionUsuario, btnBuscarEspeLugar, btnReportes, btnBuscarLugar;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    ViewFlipper carrusel,carrusel2,carrusel3;
    ImageView imageView, imageView2, imageView3;
    Button btnGestionL;
    Button next,next2;
    Button previous, previous2;
    Boolean band1=false;
    Boolean band2=false;

    ArrayList<String> idImages1= new ArrayList<>();
    ArrayList<String> idImages2= new ArrayList<>();
    ArrayList<String> idImages3= new ArrayList<>();

    Boolean band3=false;
    Boolean band4=false;

    Toolbar toolbar = new Toolbar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_admin);
       // btnGestionUsuario= findViewById(R.id.btnGestionUsuario);
       // btnGestionL= findViewById(R.id.btnGestionL);
       // btnReportes=findViewById(R.id.btnReportes);
//        btnGestionL.setVisibility(View.INVISIBLE);
        carrusel = findViewById(R.id.carrusel);
        carrusel2 = findViewById(R.id.carrusel2);
        carrusel3 = findViewById(R.id.carrusel3);
        next = (Button) findViewById(R.id.btnNext);
        previous = (Button) findViewById(R.id.btnPrevious);

        next2 = (Button) findViewById(R.id.btnNext2);
        previous2 = (Button) findViewById(R.id.btnPrevious2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                band1 = true;
                animacionCarrusel(true);
                   // if (v == next) {
                        carrusel.setInAnimation(InicioAdmin.this, R.anim.slide_in_right);
                        carrusel.setOutAnimation(InicioAdmin.this, R.anim.slide_out_left);
                        carrusel.showNext();


                   // }
            }
        });
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                band3 = true;
                animacionCarrusel2(true);
                // if (v == next) {
                carrusel2.setInAnimation(InicioAdmin.this, R.anim.slide_in_right);
                carrusel2.setOutAnimation(InicioAdmin.this, R.anim.slide_out_left);
                carrusel2.showNext();


                // }
            }
        });


        band1=false;
        band2=false;
        band3=false;
        band4=false;
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                band2 = true;
                animacionCarrusel(band2);
                //if (v == previous) {
               carrusel.setInAnimation(InicioAdmin.this, android.R.anim.slide_in_left);
               carrusel.setOutAnimation(InicioAdmin.this,android.R.anim.slide_out_right);
                    carrusel.showPrevious();
                   ;
                //}
            }

        });

        previous2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                band4 = true;
                animacionCarrusel2(band4);
                //if (v == previous) {
                carrusel2.setInAnimation(InicioAdmin.this, android.R.anim.slide_in_left);
                carrusel2.setOutAnimation(InicioAdmin.this,android.R.anim.slide_out_right);
                carrusel2.showPrevious();
                ;
                //}
            }

        });

        int images[]={R.drawable.carru_tip,R.drawable.carru_lug,R.drawable.carru_esp,
                R.drawable.carru_med, R.drawable.carru_le, R.drawable.carru_mle};


        String listIDImage1;
            for (int i = 0; i < images.length; i++) {
                flipperImages(images[i]);
                listIDImage1= String.valueOf(images[i]);
                idImages1.add(listIDImage1);

            //Toast.makeText(InicioAdmin.this, idImages1.get(i).toString(), Toast.LENGTH_SHORT).show();


        }


        int images2[]={R.drawable.gesusers, R.drawable.reporte_lugares};
        String listIDImage2;
        for (int i = 0; i < images2.length; i++) {
            flipperImages2(images2[i]);
            listIDImage2= String.valueOf(images2[i]);
            idImages2.add(listIDImage2);

            //Toast.makeText(InicioAdmin.this, idImages2.get(i).toString(), Toast.LENGTH_SHORT).show();


        }

        int images3[]={R.drawable.gu};
        String listIDImage3;
        for (int i = 0; i < images3.length; i++) {
            flipperImages3(images3[i]);
            listIDImage3= String.valueOf(images3[i]);
            idImages3.add(listIDImage3);

           // Toast.makeText(InicioAdmin.this, idImages3.get(i).toString(), Toast.LENGTH_SHORT).show();
        }


        carrusel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(InicioAdmin.this,carrusel.getCurrentView().getBackground().equals(R.drawable.medico), Toast.LENGTH_SHORT).show();
                //if(((Integer) carrusel.getCurrentView().getId())==2131165428)
                //Toast.makeText(InicioAdmin.this,carrusel.getCurrentView().getTag().toString(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(InicioAdmin.this,carrusel.getCurrentView().getBackground().toString(), Toast.LENGTH_SHORT).show();
                //btnGestionL.setVisibility(View.VISIBLE);
                // if(carrusel.getCurrentView().getBackground().toString()==2131165428)
               String idTag=carrusel.getCurrentView().getTag().toString();
                if(idTag.equals(idImages1.get(0))){
                   // finishAndRemoveTask();
                    Intent intent = new Intent(InicioAdmin.this, IngresoTipologia.class);
                    startActivity(intent);}
                else{if(idTag.equals(idImages1.get(1))){
                    Intent intent = new Intent(InicioAdmin.this, IngresoLugarMedico.class);
                        startActivity(intent);}
                    else { if(idTag.equals(idImages1.get(2))){
                        Intent intent = new Intent(InicioAdmin.this, IngresoEspecialidad.class);
                            startActivity(intent);}
                        else{ if(idTag.equals(idImages1.get(3))){
                            Intent intent = new Intent(InicioAdmin.this, IngresoMedico.class);
                                startActivity(intent); }
                            else{ if(idTag.equals(idImages1.get(4))){
                                Intent intent = new Intent(InicioAdmin.this, AsignarEspecialidad.class);
                                    startActivity(intent); }
                                else{ if(idTag.equals(idImages1.get(5))){
                                    Intent intent = new Intent(InicioAdmin.this, AsignarMedico.class);
                                        startActivity(intent);
                                        //animacionCarrusel(false);

                                } }
                            } } } }

            }
        });

        carrusel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(InicioAdmin.this,carrusel.getCurrentView().getBackground().equals(R.drawable.medico), Toast.LENGTH_SHORT).show();
                //if(((Integer) carrusel.getCurrentView().getId())==2131165428)
                //Toast.makeText(InicioAdmin.this,carrusel2.getCurrentView().getTag().toString(), Toast.LENGTH_SHORT).show();
                //btnGestionL.setVisibility(View.VISIBLE);
                // if(carrusel.getCurrentView().getBackground().toString()==2131165428)
                String idTag2=carrusel2.getCurrentView().getTag().toString();

                if(idTag2.equals(idImages2.get(0))){
                    // finishAndRemoveTask();
                    Intent intent = new Intent(InicioAdmin.this, ReporteGraficoUsuarios.class);
                    startActivity(intent);}
                else{if(idTag2.equals(idImages2.get(1))){
                    Intent intent = new Intent(InicioAdmin.this, ReporteGraficoTop.class);
                    startActivity(intent);}
               }

            }
        });

        carrusel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(InicioAdmin.this,carrusel.getCurrentView().getBackground().equals(R.drawable.medico), Toast.LENGTH_SHORT).show();
                //if(((Integer) carrusel.getCurrentView().getId())==2131165428)
               // Toast.makeText(InicioAdmin.this,carrusel3.getCurrentView().getTag().toString(), Toast.LENGTH_SHORT).show();
                //btnGestionL.setVisibility(View.VISIBLE);
                // if(carrusel.getCurrentView().getBackground().toString()==2131165428)
                String idTag3=carrusel3.getCurrentView().getTag().toString();

                if(idTag3.equals(idImages3.get(0))){
                    // finishAndRemoveTask();
                    Intent intent = new Intent(InicioAdmin.this, RegistroAdmin.class);
                    startActivity(intent);}

            }
        });

        carrusel.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
              //  String a= carrusel.getCurrentView().getTag().toString();
                //activarBoton(a);
            }
        });
        toolbar.show(this, "Hola, administrador", false);




        /*btnReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InicioAdmin.this, ReportesAdmin.class);
                startActivity(intent);

            }
        });*/


       /* btnBuscarEspeLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioAdmin.this, Buscar_Especialidades.class);
                startActivity(intent);
            }
        });*/
      /*  btnGestionUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioAdmin.this, RegistroAdmin.class);
                startActivity(intent);
            }
        });*/
        /*btnBuscarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioAdmin.this, Busqueda.class);
                startActivity(intent);
            }
        });*/
    }

    private void flipperImages3(int image) {
        imageView3= new ImageView(this);

        imageView3.setTag(image);
        // if(image.equals("res/drawable/medic.png")){}

        //if (image==1) {
        // imageView.setTag("bg1");
       // Toast.makeText(InicioAdmin.this, imageView3.getTag().toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(InicioAdmin.this, carrusel.getDisplay().toString(), Toast.LENGTH_SHORT).show();
        imageView3.setBackgroundResource(image);
        carrusel3.addView(imageView3);
        /*
        carrusel3.startFlipping();
        carrusel3.setFlipInterval(3000);
        carrusel3.setAutoStart(true);
        carrusel3.setInAnimation(this, R.anim.slide_in_right);
        carrusel3.setOutAnimation(this, R.anim.slide_out_left);
        */

    }

    private void flipperImages2(int image) {

        imageView2= new ImageView(this);

        imageView2.setTag(image);
        // if(image.equals("res/drawable/medic.png")){}

        //if (image==1) {
        // imageView.setTag("bg1");
        //Toast.makeText(InicioAdmin.this, imageView2.getTag().toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(InicioAdmin.this, carrusel.getDisplay().toString(), Toast.LENGTH_SHORT).show();
        imageView2.setBackgroundResource(image);
        carrusel2.addView(imageView2);
        animacionCarrusel2(false);
/*
        carrusel2.startFlipping();
        carrusel2.setFlipInterval(3000);
        carrusel2.setAutoStart(true);
        carrusel2.setInAnimation(this, R.anim.slide_in_right);
        carrusel2.setOutAnimation(this, R.anim.slide_out_left);
        */

    }

    public void activarBoton(String a){
        String b="2131165286";
        if(a.equals(b)) {
            //btnGestionL.setVisibility(View.VISIBLE);
        }
        else
        {
            //btnGestionL.setVisibility(View.INVISIBLE);
        }
    }




    public void flipperImages(int image){
        imageView= new ImageView(this);

        imageView.setTag(image);
       // if(image.equals("res/drawable/medic.png")){}

        //if (image==1) {
           // imageView.setTag("bg1");
       // Toast.makeText(InicioAdmin.this, imageView.getTag().toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(InicioAdmin.this, carrusel.getDisplay().toString(), Toast.LENGTH_SHORT).show();
            imageView.setBackgroundResource(image);
            carrusel.addView(imageView);
            animacionCarrusel(false);
           // carrusel.setFlipInterval(3000);

            //carrusel.setAutoStart(true);
            //carrusel.setInAnimation(this, android.R.anim_.slide_in_left);
            //carrusel.setOutAnimation(this, android.R.anim_.slide_out_right);


        //carrusel.getCurrentView();




        // Toast.makeText(InicioAdmin.this, imageView.getDrawable().toString(), Toast.LENGTH_SHORT).show();
           /* imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   // if (imageView.getTag().toString().equals("bg1")) {
                       // if(imageView.getDrawable().toStri){}
                        Intent intent = new Intent(InicioAdmin.this, GestionLugares.class);
                        startActivity(intent);
                    //} else {
                        Toast.makeText(InicioAdmin.this, "Otro", Toast.LENGTH_SHORT).show();
                    //}
                }
            });*/
            {

            }
        //}
    }

    private void animacionCarrusel(boolean b) {
        if (b==true) {
            carrusel.setFlipInterval(0);
            carrusel.setAutoStart(false);
            carrusel.stopFlipping();
           // carrusel.setInAnimation(this, 0 );
           // carrusel.setOutAnimation(this,0);

        }
        else {
            carrusel.startFlipping();
            carrusel.setFlipInterval(3000);
            carrusel.setAutoStart(true);
            carrusel.setInAnimation(this, R.anim.slide_in_right);
            carrusel.setOutAnimation(this, R.anim.slide_out_left);
        }
    }


    private void animacionCarrusel2(boolean b) {
        if (b==true) {
            carrusel2.setFlipInterval(0);
            carrusel2.setAutoStart(false);
            carrusel2.stopFlipping();
            // carrusel.setInAnimation(this, 0 );
            // carrusel.setOutAnimation(this,0);

        }
        else {
            carrusel2.startFlipping();
            carrusel2.setFlipInterval(3000);
            carrusel2.setAutoStart(true);
            carrusel2.setInAnimation(this, R.anim.slide_in_right);
            carrusel2.setOutAnimation(this, R.anim.slide_out_left);
        }
    }

    @Override public void onBackPressed() { }  //Anula la flecha de regreso del telefono

    @Override  // Muestra un mensaje para salir de la app
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea salir del Administrador de Geolam?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

        return super.onKeyDown(keyCode, event);
    }





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