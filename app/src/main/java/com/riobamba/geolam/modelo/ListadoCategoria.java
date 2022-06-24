package com.riobamba.geolam.modelo;

import org.json.JSONObject;

public class ListadoCategoria {
/*
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ListadoCategoria(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }
    public ListadoCategoria() {
    }
    @Override
    public String toString(){
        return descripcion;
    }
    */

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

        return id.toString()+"-"+descripcion;
    }








}
