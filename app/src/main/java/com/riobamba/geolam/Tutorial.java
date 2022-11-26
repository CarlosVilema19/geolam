package com.riobamba.geolam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.modelo.Toolbar;

public class Tutorial extends AppCompatActivity {

    private VideoView videoView, videoView2, videoView3;
    Toolbar toolbar = new Toolbar();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        toolbar.show(this, "Tutorial", true);


        videoView=findViewById(R.id.vv1);
        videoView2=findViewById(R.id.vv2);
        videoView3=findViewById(R.id.vv3);
        imageView=findViewById(R.id.gift);
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.prin));
        MediaController mc= new MediaController(this);
        videoView.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer pMp) {
               mc.show();
            }
        });

        videoView.setMediaController(mc);
        mc.setAnchorView(videoView);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                // get media controller background layout
                LinearLayout viewGroupLevel1 = (LinearLayout)  mc.getChildAt(0);
                //Set your color with desired transparency here:
                viewGroupLevel1.setBackgroundColor(getResources().getColor(R.color.colorOnPrimaryLightDemo));
            }
        });


        videoView2.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.busq));
        MediaController mc2= new MediaController(this);
        videoView2.setMediaController(mc2);
        mc2.setAnchorView(videoView2);
        videoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                // get media controller background layout
                LinearLayout viewGroupLevel1 = (LinearLayout)  mc2.getChildAt(0);
                //Set your color with desired transparency here:
                viewGroupLevel1.setBackgroundColor(getResources().getColor(R.color.colorOnPrimaryLightDemo));
            }
        });

        videoView3.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.geo));
        MediaController mc3= new MediaController(this);
        videoView3.setMediaController(mc3);
        mc3.setAnchorView(videoView3);
        videoView3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                // get media controller background layout
                LinearLayout viewGroupLevel1 = (LinearLayout)  mc3.getChildAt(0);
                //Set your color with desired transparency here:
                viewGroupLevel1.setBackgroundColor(getResources().getColor(R.color.colorOnPrimaryLightDemo));
            }
        });



        //mc.setBackgroundTintMode(null);
        //mc.setBackgroundColor(Color.rgb(255,241,250));
        //mc.startLayoutAnimation();
        //mc.requestFocus();


/*
        mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setBackgroundResource(0);
            }
        });*/
        String urlGif = "https://media.giphy.com/media/MyWrJJIdAfoJuEPlLP/giphy.gif";
        //Agregar implementacion Glide dentro de archivo build.gradle.

        Uri uri = Uri.parse(urlGif);
        Glide.with(getApplicationContext()).load(uri).centerCrop().into(imageView);

           /* if(videoView.isPlaying()){
                videoView2.pause();
                videoView3.pause();
            }
            else {

                if(videoView2.isPlaying()){
                    videoView.pause();
                    videoView3.pause();
                }
                else{

                    if(videoView3.isPlaying()){
                        videoView.pause();
                        videoView2.pause();
                    }
                }
            }*/
        }





    //Funcion para ejecutar las instrucciones de los items -- proviene de la clase Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        toolbar.getContexto(this);
        toolbar.ejecutarItemSelected(item, this);
        return super.onOptionsItemSelected(item);
    }

}