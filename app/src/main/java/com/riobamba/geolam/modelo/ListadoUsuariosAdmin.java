package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoUsuariosAdmin implements Serializable {

    String nombreUsuarios, emailUsuarios, imagenUsuarios, descripcionTipo;
    Integer  idTipoUsuarios;

    public ListadoUsuariosAdmin(String nombreUsuarios, String emailUsuarios, String imagenUsuarios, String descripcionTipo, Integer idTipoUsuarios) {
        this.nombreUsuarios = nombreUsuarios;
        this.emailUsuarios = emailUsuarios;
        this.imagenUsuarios = imagenUsuarios;
        this.descripcionTipo = descripcionTipo;
        this.idTipoUsuarios = idTipoUsuarios;
    }

    public String getNombreUsuarios() {
        return nombreUsuarios;
    }

    public String getEmailUsuarios() {
        return emailUsuarios;
    }

    public String getImagenUsuarios() {
        return imagenUsuarios;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public Integer getIdTipoUsuarios() {
        return idTipoUsuarios;
    }
}
