package com.riobamba.geolam.modelo;

import org.json.JSONObject;

public class ListadoAsignarEspecialidad {
    public String getId() {
        return id;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    String id;
    String especialidad;

    public ListadoAsignarEspecialidad(JSONObject object)
    {
        try{
            id= (object.getString("ID_ESPECIALIDAD"));
            especialidad= (object.getString("DESCRIPCION_ESPECIALIDAD").toUpperCase());

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    @Override
    public String toString(){

        return especialidad;

    }

}
