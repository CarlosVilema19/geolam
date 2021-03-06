package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoOpinion implements Serializable {

    String nombreUsuario, fechaOpinion, comentario, imagenUsuario;
    Integer  idOpinion;
    Float calificacion;
    String email;

    public ListadoOpinion(String nombreUsuario, String fechaOpinion, String comentario,
                          String imagenUsuario, Integer idOpinion, Float calificacion, String email) {
        this.nombreUsuario = nombreUsuario;
        this.fechaOpinion = fechaOpinion;
        this.comentario = comentario;
        this.imagenUsuario = imagenUsuario;
        this.idOpinion = idOpinion;
        this.calificacion = calificacion;
        this.email = email;
    }


    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getFechaOpinion() {
        return fechaOpinion;
    }

    public String getComentario() {
        return comentario;
    }

    public String getImagenUsuario() {
        return imagenUsuario;
    }

    public Integer getIdOpinion() {
        return idOpinion;
    }

    public Float getCalificacion() {
        return calificacion;
    }

    public String getEmail() {
        return email;
    }
}
