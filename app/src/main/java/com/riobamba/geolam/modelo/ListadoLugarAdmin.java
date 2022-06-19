package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoLugarAdmin implements Serializable {

    String nombreLugar;
    Integer id;

    public ListadoLugarAdmin(String nombreLugar, int id_lugar) {
        this.nombreLugar = nombreLugar;
        this.id = id_lugar;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public Integer getId() {
        return id;
    }
}
