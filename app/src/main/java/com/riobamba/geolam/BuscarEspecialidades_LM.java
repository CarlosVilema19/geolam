package com.riobamba.geolam;

import org.json.JSONObject;

public class BuscarEspecialidades_LM {
    String nombreLugar, direccionLugar, telefonoLugar;
    String imagenLugar, tipoLugar, categoriaLugar;
    String id;
    String urlImage;
public BuscarEspecialidades_LM(JSONObject object)
{
    try {
        nombreLugar = object.getString("nombre_lugar");
        /*id=object.getString("id_lugar");

        direccionLugar= object.getString("direccion");
        telefonoLugar=object.getString("telefono");
        urlImage=object.getString("imagen_lugar");
        tipoLugar=object.getString("descripcion_tipo_lugar");
        categoriaLugar=object.getString("descripcion_categoria");
*/
    }
    catch (Exception e)

    {
        e.printStackTrace();

    }
}

    public String getNombreLugar() {
        return nombreLugar.toUpperCase();
    }

    public String getImagenLugar() {
        return imagenLugar;
    }

    public String getId() {
        return id;
    }
    @Override
    public String toString(){
    return  nombreLugar.toUpperCase();
    }
}
