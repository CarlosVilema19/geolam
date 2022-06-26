package com.riobamba.geolam.modelo;

import org.json.JSONObject;

public class ListadoCategoria {


    private String id;
    private String descripcion;

    public String getId() {
        return id;
    }
    public String getDescripcion() {
        return descripcion;
    }


public ListadoCategoria(JSONObject object)
{
    try{
        id= (object.getString("ID_CATEGORIA"));
        descripcion= (object.getString("DESCRIPCION_CATEGORIA"));
    }catch(Exception e){

        e.printStackTrace();

    }

}

@Override
    public String toString(){

        return descripcion;
    //id.toString()+"-"+descripcion;
    }








}
