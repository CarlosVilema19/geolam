package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class DatosPersonales implements Serializable {

    String nombreUsuarios, apellidoUsuarios, emailUsuarios, imagenUsuarios, descripcionTipo;
    Integer  idTipoUsuarios;
    Integer edad;

    public DatosPersonales(String nombreUsuarios,String apellidoUsuarios, String emailUsuarios, String imagenUsuarios, String descripcionTipo, Integer idTipoUsuarios, Integer edad) {
        this.nombreUsuarios = nombreUsuarios;
        this.apellidoUsuarios = apellidoUsuarios;
        this.emailUsuarios = emailUsuarios;
        this.imagenUsuarios = imagenUsuarios;
        this.descripcionTipo = descripcionTipo;
        this.idTipoUsuarios = idTipoUsuarios;
        this.edad = edad;
    }

    public String getNombreUsuarios() {
        return nombreUsuarios;
    }

    public String getApellidoUsuarios() {
        return apellidoUsuarios;
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

    public Integer getEdad() {
        return edad;
    }
}
