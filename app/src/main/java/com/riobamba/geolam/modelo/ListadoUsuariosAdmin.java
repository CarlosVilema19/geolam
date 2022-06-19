package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoUsuariosAdmin implements Serializable {

    String nombreUsuarios, emailUsuarios, imagenUsuarios;
    Integer  idTipoUsuarios;

    public ListadoUsuariosAdmin(String nombreUsuarios, String emailUsuarios, String imagenUsuarios, Integer idTipoUsuarios) {
        this.nombreUsuarios = nombreUsuarios;
        this.emailUsuarios = emailUsuarios;
        this.imagenUsuarios = imagenUsuarios;
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

    public Integer getIdTipoUsuarios() {
        return idTipoUsuarios;
    }
}
