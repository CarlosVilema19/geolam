package com.riobamba.geolam.modelo;

import org.json.JSONObject;

public class ListadoTipologia {
    private Integer id;
    private String descripcion;

    public Integer getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public ListadoTipologia(JSONObject object)
    {
        try{
            id= object.getInt("ID_TIPOLOGIA_LUGAR");
            descripcion= (object.getString("DESCRIPCION_TIPO_LUGAR").toUpperCase());
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
