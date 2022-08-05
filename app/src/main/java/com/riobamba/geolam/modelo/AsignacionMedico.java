package com.riobamba.geolam.modelo;

import java.io.Serializable;

public class AsignacionMedico implements Serializable {

    String nombreLugar, especialidad, medico;
    Integer idLugar, idEspecialidad, idMedico;

    public AsignacionMedico(String nombreLugar, String especialidad, String medico, Integer idLugar, Integer idEspecialidad, Integer idMedico) {
        this.nombreLugar = nombreLugar;
        this.especialidad = especialidad;
        this.medico = medico;
        this.idLugar = idLugar;
        this.idEspecialidad = idEspecialidad;
        this.idMedico = idMedico;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getMedico() {
        return medico;
    }

    public Integer getIdLugar() {
        return idLugar;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public Integer getIdMedico() {
        return idMedico;
    }
}
