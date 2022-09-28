package com.riobamba.geolam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.riobamba.geolam.Utility.NetworkChangeListener;

import java.io.File;

public class ViewPDF extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    private PDFView pdfView;
private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pedirPermisos();
        pdfView=(PDFView) findViewById(R.id.pdfView);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            file= new File(bundle.getString("path",""));
        }
        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();
    }

    public void pedirPermisos(){
        if(ActivityCompat.checkSelfPermission(ViewPDF.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ViewPDF.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }}



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