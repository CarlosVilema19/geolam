package com.riobamba.geolam.modelo;

import org.json.JSONObject;

public class ActualizarLugarMedico {

    //////*****


    String id;
    String nombreLugarMedico;
    String direccion;
    String telefono;
    String whatsApp;

    String paginaWeb;
    String latitud;
    String longitud;
    String descripcion;


    public ActualizarLugarMedico(JSONObject object)
    {
        try{
            id= (object.getString("ID_LUGAR"));
            nombreLugarMedico= (object.getString("NOMBRE_LUGAR").toUpperCase());
            direccion=(object.getString("DIRECCION"));
            telefono=(object.getString("TELEFONO"));
            whatsApp=(object.getString("WHATSAPP"));
            paginaWeb=(object.getString("PAGINA_WEB"));
            latitud=(object.getString("LATITUD"));
            longitud=(object.getString("LONGITUD"));
            descripcion=(object.getString("DESCRIPCION"));

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    public String getNombreLugarMedico() {
        return nombreLugarMedico;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getWhatsApp() {
        return whatsApp;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public String getId() {
        return id;
    }

    public String getLugarMedico() {
        return nombreLugarMedico;
    }

    @Override
    public String toString(){

        return nombreLugarMedico;

    }

}
