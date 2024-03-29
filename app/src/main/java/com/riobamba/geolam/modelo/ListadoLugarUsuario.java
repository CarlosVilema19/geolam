package com.riobamba.geolam.modelo;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public class ListadoLugarUsuario implements Serializable {

    String nombreLugar, direccionLugar, telefonoLugar;
    String imagenLugar, informacionLugar, categoriaLugar, tipologiaLugar;
    Integer idLugar;
    String whastapp, paginaWeb;
    Float calificacion, latitud, longitud;
    Integer favorito;
    Integer estadoOpi, numOpi;

    public ListadoLugarUsuario(String nombreLugar, String direccionLugar, String telefonoLugar,
                               String imagenLugar, String informacionLugar, String categoriaLugar,
                               String tipologiaLugar, Integer idLugar, String whastapp,String paginaWeb,
                               Float calificacion, Integer favorito, Integer estadoOpi, Integer numOpi,
                               Float latitud, Float longitud) {
        this.nombreLugar = nombreLugar;
        this.direccionLugar = direccionLugar;
        this.telefonoLugar = telefonoLugar;
        this.imagenLugar = imagenLugar;
        this.informacionLugar = informacionLugar;
        this.categoriaLugar = categoriaLugar;
        this.tipologiaLugar = tipologiaLugar;
        this.idLugar = idLugar;
        this.whastapp=whastapp;
        this.paginaWeb = paginaWeb;
        this.calificacion = calificacion;
        this.favorito = favorito;
        this.estadoOpi = estadoOpi;
        this.numOpi = numOpi;
        this.latitud = latitud;
        this.longitud = longitud;

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

    public Integer getIdLugar() {
        return idLugar;
    }

    public String getWhastapp() {
        return whastapp;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public Float getCalificacion() {
        return calificacion;
    }

    public Integer getFavorito() {
        return favorito;
    }

    public Integer getEstadoOpi() {
        return estadoOpi;
    }

    public Integer getNumOpi() {
        return numOpi;
    }

    public Float getLatitud() {
        return latitud;
    }

    public Float getLongitud() {
        return longitud;
    }
}
