package com.riobamba.geolam.modelo;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public class ListadoLugar implements Serializable {

    String nombreLugar, direccionLugar, telefonoLugar;
    String imagenLugar;
    Integer id;
    String distancia;

    public ListadoLugar(String nombreLugar, String direccionLugar, String telefonoLugar, String imagenLugar, int id_lugar, String distancia) {
        this.nombreLugar = nombreLugar;
        this.direccionLugar = direccionLugar;
        this.telefonoLugar = telefonoLugar;
        this.imagenLugar = imagenLugar;
        this.id = id_lugar;
        this.distancia = distancia;
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

    public Integer getId() {
        return id;
    }

    public String getDistancia() { return distancia; }
}
