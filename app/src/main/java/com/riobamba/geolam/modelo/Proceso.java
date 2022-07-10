package com.riobamba.geolam.modelo;

public class Proceso {

    public double obtenerDistancia(Double latitudUsu, Double longitudUsu, Double latitudLug, Double longitudLug)
    {
        Double latitud = convertirRadianes(latitudUsu) - convertirRadianes(latitudLug);
        Double longitud =convertirRadianes(longitudUsu) -convertirRadianes(longitudLug);

        Double a = Math.pow(Math.sin(latitud/2),2)  +
                Math.cos(convertirRadianes(latitudLug))*
                Math.cos(convertirRadianes(latitudUsu))*
                        Math.pow(Math.sin(longitud/2),2);

        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return 6371*c;

    }

    public double convertirRadianes(Double valor)
    {
       return  (Math.PI / 180) * valor;
    }



}
