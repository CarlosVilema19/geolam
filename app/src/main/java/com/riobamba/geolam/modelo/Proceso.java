package com.riobamba.geolam.modelo;

public class Proceso {

    public double obtenerDistancia(Double latitudUsu, Double longitudUsu, Float latitudLug, Float longitudLug)
    {
        double latitud = convertirRadianes(latitudUsu) - convertirRadianes((double) latitudLug);
        double longitud =convertirRadianes(longitudUsu) -convertirRadianes((double)longitudLug);

        double a = Math.pow(Math.sin(latitud/2),2)  +
                Math.cos(convertirRadianes((double)latitudLug))*
                Math.cos(convertirRadianes(latitudUsu))*
                        Math.pow(Math.sin(longitud/2),2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return 6371*c;

    }

    public double convertirRadianes(Double valor)
    {
       return  (Math.PI / 180) * valor;
    }



}
