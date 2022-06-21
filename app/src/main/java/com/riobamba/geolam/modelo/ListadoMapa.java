package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoMapa implements Serializable {

    Double latitud, longitud;
    String nombreLugar, direccionLugar;

    public ListadoMapa(Double latitud, Double longitud, String nombreLugar, String direccionLugar) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreLugar = nombreLugar;
        this.direccionLugar = direccionLugar;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public String getDireccionLugar() {
        return direccionLugar;
    }
}
