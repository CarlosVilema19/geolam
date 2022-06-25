package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class ListadoMedico implements Serializable {

    String nombreMedico, especialidadMedico, descripcionMedico;
    Integer idMedico;

    public ListadoMedico(String nombreMedico, String especialidadMedico, String descripcionMedico, Integer idMedico) {
        this.nombreMedico = nombreMedico;
        this.especialidadMedico = especialidadMedico;
        this.descripcionMedico = descripcionMedico;
        this.idMedico = idMedico;
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
}
