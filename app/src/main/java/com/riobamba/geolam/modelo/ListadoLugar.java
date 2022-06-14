package com.riobamba.geolam.modelo;

import android.widget.ImageView;
import android.widget.TextView;

public class ListadoLugar {

    String nombreLugar, direccionLugar, telefonoLugar,imagenLugar;

    public ListadoLugar(String nombreLugar, String direccionLugar, String telefonoLugar, String imagenLugar) {
        this.nombreLugar = nombreLugar;
        this.direccionLugar = direccionLugar;
        this.telefonoLugar = telefonoLugar;
        this.imagenLugar = imagenLugar;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public String getDireccionLugar() {
        return direccionLugar;
    }

    public String getTelefonoLugar() {
        return telefonoLugar;
    }

    public String getImagenLugar() {
        return imagenLugar;
    }
}
