package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoMapa implements Serializable {

    Float latitud, longitud;
    String nombreLugar, direccionLugar;

    public ListadoMapa(Float latitud, Float longitud, String nombreLugar, String direccionLugar) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreLugar = nombreLugar;
        this.direccionLugar = direccionLugar;
    }

    public Float getLatitud() {
        return latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public String getDireccionLugar() {
        return direccionLugar;
    }
}
