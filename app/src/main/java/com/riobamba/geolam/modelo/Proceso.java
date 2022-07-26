package com.riobamba.geolam.modelo;

import android.content.Intent;

import java.text.DecimalFormat;

public class Proceso {
    double temp = 100000;

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


    public String verCercano(Double[] distancias, String[] lugarCerca, Integer count ) {
        String lugarDist = "null";
        String dist;
        DecimalFormat formato2 = new DecimalFormat("#0.0");
        for(int i = 0; i<count;i++)
        {
            if(distancias[i]< temp)
            {
                temp = distancias[i];
                dist = formato2.format(temp);
                lugarDist = lugarCerca[i] + " a "+ dist + " Km";
            }
        }
        return  lugarDist;
    }
}
