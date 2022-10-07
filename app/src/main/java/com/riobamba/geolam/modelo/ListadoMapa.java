package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoMapa implements Serializable {

    Float latitud, longitud;
    String nombreLugar, direccionLugar;
    Integer idLugar;

    public ListadoMapa(Float latitud, Float longitud, String nombreLugar, String direccionLugar, Integer idLugar) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreLugar = nombreLugar;
        this.direccionLugar = direccionLugar;
        this.idLugar = idLugar;
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

    public Integer getIdLugar() {
        return idLugar;
    }
}
