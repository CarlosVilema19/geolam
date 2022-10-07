package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class Proceso {
    double temp = 100000;
    double temp1 = 100000;

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


    public String verCercano(Double[] distancias, String[] lugarCerca, Integer count) {
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

    public String verIdCercano(Double[] distancias, Integer[] idLugar, Integer count) {
        String idLugarDist = "null";
        for(int i = 0; i<count;i++)
        {
            if(distancias[i]< temp1)
            {
                temp1 = distancias[i];
                idLugarDist = "" + idLugar[i];
            }
        }
            return  idLugarDist;
    }

    public Integer obtenerIdLugar(Integer[] idLugar, String[] idMarker, String marker, Integer count, Context ctx)
    {
        int valor = 0;
        for(int i = 0; i<count ; i++)
        {
            if(Objects.equals(marker, idMarker[i]))
            {
                valor = idLugar[i];
            }
        }
//        Toast.makeText(ctx, String.valueOf(valor), Toast.LENGTH_SHORT).show();
//        Toast.makeText(ctx, marker, Toast.LENGTH_SHORT).show();
        return valor;
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

    public String calcularEdadFecha(String fecha, Context ctx)
    {
        String fechaActu;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Date dob = null;
        try {
            dob = dateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
        cal.setGregorianChange(new Date(Long.MIN_VALUE));
        cal.clear();
        cal.set(Calendar.YEAR, 0);
        cal.setTimeInMillis( cal.getTimeInMillis() + new Date().getTime() - dob.getTime());

        Formatter fmtr = new Formatter();
        if (cal.get(Calendar.YEAR) > 0) {
            fmtr.format(String.valueOf(cal.get(Calendar.YEAR)));
        }
        /*if (cal.get(Calendar.MONTH) > 0) {
            fmtr.format("%d meses ", cal.get(Calendar.MONTH));
        }
        if (cal.get(Calendar.DAY_OF_MONTH) > 0) {
            fmtr.format("%d días ", cal.get(Calendar.DAY_OF_MONTH));
        }*/
        fechaActu = fmtr.toString();
        return fechaActu;
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
