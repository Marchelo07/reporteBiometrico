package com.marchelo.excel.biome.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class UtilDate {

    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
    DateFormat outsfd = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    public static DateFormat dateFormatHHMM = new SimpleDateFormat("HH:mm");
    public static DateFormat dateFormatmmss = new SimpleDateFormat("mm:ss");
    public static DateFormat dateFormatHHmmss = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat sformatFile = new SimpleDateFormat("MM-dd-yyyy_HH_mm_ss");
    public static SimpleDateFormat sformatFechaDDMMYYY = new SimpleDateFormat("dd-MM-yyyy");




    public static Integer calcularTiempoTrancurrido3 (String horaInicio, String horaFin) throws ParseException {
        Date dateInicio = dateFormatHHMM.parse(horaInicio);
        Date dateFin = dateFormatHHMM.parse(horaFin);
        long milliseconds = dateFin.getTime() - dateInicio.getTime();
        Integer minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        return minutes;
    }

    public static Date calcularTiempoTrancurridoTime (String horaInicio, String horaFin) throws ParseException {
        Date dateInicio = dateFormatHHMM.parse(horaInicio);
        Date dateFin = dateFormatHHMM.parse(horaFin);
        long milliseconds = dateFin.getTime() - dateInicio.getTime();
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, seconds);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    private void fechaBetween(){
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.add(Calendar.DATE, -7);
        Date nowMinus15 = c.getTime();

        System.out.println("FECHA DE HOY: " + sformatFechaDDMMYYY.format(now) + "  FECHA HACE 7 DÍAS:  "+ sformatFechaDDMMYYY.format(nowMinus15));
    }

    private void fecha (){
        String myTime = "10:30";
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date date = null;
        try {
            date = sdf.parse(myTime);
            LocalTime t = LocalTime.parse( "17:40" ) ;
            System.out.println(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTime = sdf.format(date);

        System.out.println(formattedTime);
    }

}
