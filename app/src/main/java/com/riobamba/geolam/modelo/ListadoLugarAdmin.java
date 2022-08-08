package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoLugarAdmin implements Serializable {

    String nombreLugar;
    Integer id;
    String imagen;

    public ListadoLugarAdmin(String nombreLugar, int id_lugar, String imagen) {
        this.nombreLugar = nombreLugar;
        this.id = id_lugar;
        this.imagen = imagen;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public Integer getId() {
        return id;
    }

    public String getImagen() {
        return imagen;
    }
}
