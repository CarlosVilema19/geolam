package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoMedico implements Serializable {

    String nombreMedico, especialidadMedico, descripcionMedico;
    Integer idMedico;
    String lugarTrabaja;

    public ListadoMedico(String nombreMedico, String especialidadMedico, String descripcionMedico, Integer idMedico, String lugarTrabaja) {
        this.nombreMedico = nombreMedico;
        this.especialidadMedico = especialidadMedico;
        this.descripcionMedico = descripcionMedico;
        this.idMedico = idMedico;
        this.lugarTrabaja = lugarTrabaja;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public String getEspecialidadMedico() {
        return especialidadMedico;
    }

    public String getDescripcionMedico() {
        return descripcionMedico;
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public String getLugarTrabaja() {
        return lugarTrabaja;
    }
}
