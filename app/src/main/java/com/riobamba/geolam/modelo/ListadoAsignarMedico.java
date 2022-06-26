package com.riobamba.geolam.modelo;

import org.json.JSONObject;

public class ListadoAsignarMedico {

    private String id;
    private String nombre;
    private String apellido;
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public ListadoAsignarMedico(JSONObject object)
    {
        try{
            id= (object.getString("ID_MEDICO"));
            nombre= (object.getString("NOMBRE_MEDICO").toUpperCase());
            apellido=(object.getString("APELLIDO_MEDICO").toUpperCase());
        }catch(Exception e){

            e.printStackTrace();

        }

    }
    @Override
    public String toString(){

        return nombre+" "+apellido;

    }


}
