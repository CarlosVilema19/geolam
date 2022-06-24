package com.riobamba.geolam.modelo;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public class ListadoLugarUsuario implements Serializable {

    String nombreLugar, direccionLugar, telefonoLugar;
    String imagenLugar, informacionLugar, categoriaLugar, tipologiaLugar;

    public ListadoLugarUsuario(String nombreLugar, String direccionLugar, String telefonoLugar, String imagenLugar, String informacionLugar, String categoriaLugar, String tipologiaLugar) {
        this.nombreLugar = nombreLugar;
        this.direccionLugar = direccionLugar;
        this.telefonoLugar = telefonoLugar;
        this.imagenLugar = imagenLugar;
        this.informacionLugar = informacionLugar;
        this.categoriaLugar = categoriaLugar;
        this.tipologiaLugar = tipologiaLugar;
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

    public String getInformacionLugar() {
        return informacionLugar;
    }

    public String getCategoriaLugar() {
        return categoriaLugar;
    }

    public String getTipologiaLugar() {
        return tipologiaLugar;
    }
}
