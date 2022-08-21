package com.riobamba.geolam.modelo;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;

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

    public String calcularEdad(int anio, int mes, int dia)
    {
        Period period;
        String meses;
        LocalDate nacimiento, actual;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nacimiento = LocalDate.of(anio, mes, dia);
            actual = LocalDate.now();
            // using period
            period = Period.between(nacimiento, actual);
            if(period.getMonths()==1) {meses = " mes";}
            else{meses = " meses";}
            return period.getYears()+" años" /*+ period.getMonths()+meses*/;
        } else return "";
    }

    public String calcularEdadAnios(int anio, int mes, int dia)
    {
        Period period;
        String meses;
        LocalDate nacimiento, actual;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nacimiento = LocalDate.of(anio, mes, dia);
            actual = LocalDate.now();
            // using period
            period = Period.between(nacimiento, actual);
            if(period.getMonths()==1) {meses = " mes";}
            else{meses = " meses";}
            return String.valueOf(period.getYears());
        } else return "";
    }

    public  long calcularAniosMili(int anios)
    {
        return ((long) anios *365*24*60*60*1000)+
                ((long) (anios / 4) *24*60*60*1000)+
                (24*60*60*1000);
    }


    public Double verDistanciaCercano(Double[] distancias, Integer count) {
        for(int i = 0; i<count;i++)
        {
            if(distancias[i]< temp)
            {
                temp = distancias[i];
            }
        }
        return  temp;
    }

    //Proceso para ordenar los lugares de atencion medica
    public void ordenarDistancia(String[] data, Integer[] numOrden) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length - 1; j++) {
                if (Float.parseFloat(data[j]) > Float.parseFloat(data[j + 1])) {
                    String temp = data[j];
                    int num = numOrden[j];
                    data[j] = data[j + 1];
                    numOrden[j] = numOrden[j+1];
                    data[j + 1] = temp;
                    numOrden[j+1] = num;
                }
            }
        }
    }


}
