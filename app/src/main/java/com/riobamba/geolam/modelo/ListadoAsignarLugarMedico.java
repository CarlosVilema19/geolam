package com.riobamba.geolam.modelo;

import org.json.JSONObject;

public class ListadoAsignarLugarMedico {

    String id;
    String nombreLugarMedico;

    public String getId() {
        return id;
    }

    public String getLugarMedico() {
        return nombreLugarMedico;
    }

    public ListadoAsignarLugarMedico(JSONObject object)
    {
        try{
            id= (object.getString("ID_LUGAR"));
            nombreLugarMedico= (object.getString("NOMBRE_LUGAR").toUpperCase());

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    @Override
    public String toString(){

        return nombreLugarMedico;

    }

}
