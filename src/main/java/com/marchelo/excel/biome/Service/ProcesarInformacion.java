package com.marchelo.excel.biome.Service;

import com.marchelo.excel.biome.Models.DtoHorario;
import com.marchelo.excel.biome.Models.UserBiometrico;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProcesarInformacion {
    private static String HORA_INGRESO = "09:00";
    static DateFormat dateFormatHHMM = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat sformatFechaDDMMYYY = new SimpleDateFormat("dd-MM-yyyy");

    public static List<UserBiometrico> usersOnTime (List<UserBiometrico> infoUsersBiome){
        List<UserBiometrico> listaUsers = new ArrayList<>();
        boolean userPuntal = true;
        for(UserBiometrico it : infoUsersBiome){
            userPuntal = true;
            for (DtoHorario h : it.getDate()){
                if(validarHorarioIngreso(h)){
                    userPuntal = false;
                    break;
                }
            }
            if(userPuntal){
                listaUsers.add(new UserBiometrico(it.getPersonID(), it.getName(), it.getDate()));
            }
        }
        return listaUsers;
    }
    public static List<UserBiometrico> usersOnLate (List<UserBiometrico> infoUsersBiome){
        List<UserBiometrico> listaUsers = new ArrayList<>();
        List<DtoHorario> listHorarioInpuntal = null;
        for(UserBiometrico it : infoUsersBiome){
            listHorarioInpuntal = new ArrayList<>();
            for (DtoHorario h : it.getDate()){
                if(validarHorarioIngreso(h)){
                    listHorarioInpuntal.add(new DtoHorario(h.getFecha(), h.getTime()));
                }
            }
            if(listHorarioInpuntal.size() >0){
                listaUsers.add(new UserBiometrico(it.getPersonID(), it.getName(), listHorarioInpuntal));
            }
        }
        return listaUsers;
    }
    public static List<UserBiometrico> userCompleteRegister(List<UserBiometrico> infoUsersBiome) throws ParseException {
        List<UserBiometrico> result = new ArrayList<>();
        boolean userRegisterAll = true;
        for(UserBiometrico it : infoUsersBiome){
            userRegisterAll = true;
            for (DtoHorario h : it.getDate()){
                if( !isFinDeSemana(h.getFecha()) && !validarRegistroNumeroTimbradas(h)){
                    userRegisterAll = false;
                    break;
                }
            }
            if(userRegisterAll){
                result.add(new UserBiometrico(it.getPersonID(), it.getName(), it.getDate()));
            }
        }
        return result;
    }
    public static List<UserBiometrico> userRegisterFinDe(List<UserBiometrico> infoUsersBiome) throws ParseException {
        List<UserBiometrico> result = new ArrayList<>();
        for(UserBiometrico it : infoUsersBiome){
            List<DtoHorario> listFinde = new ArrayList<>();
            for (DtoHorario h : it.getDate()){
                if( isFinDeSemana(h.getFecha())){
                    listFinde.add(h);
                }
            }
            if(listFinde.size() > 0){
                result.add(new UserBiometrico(it.getPersonID(), it.getName(), listFinde));
            }
        }
        return result;
    }
    public static List<UserBiometrico> userLaunchATime(List<UserBiometrico> infoUsersBiome) throws ParseException {
        List<UserBiometrico> result = new ArrayList<>();
        boolean userLaunch = true;
        for(UserBiometrico it : infoUsersBiome){
            userLaunch = true;
            for (DtoHorario h : it.getDate()){
                if(!validHorarioAlmuerzo(h)){
                    userLaunch = false;
                    break;
                }
            }
            if(userLaunch){
                result.add(new UserBiometrico(it.getPersonID(), it.getName(), it.getDate()));
            }
        }
        return result;
    }
    public static List<UserBiometrico> userNoTimbra4Veces(List<UserBiometrico> infoUsersBiome) throws ParseException{
        List<UserBiometrico> result = new ArrayList<>();
        for(UserBiometrico it : infoUsersBiome){
            List<DtoHorario> horarios = new ArrayList<>();
            for (DtoHorario h : it.getDate()){
                if(h.getTime().size() <=3 && !isFinDeSemana(h.getFecha())){
                    horarios.add(h);
                }
            }
            if(horarios.size()>0){
                result.add(new UserBiometrico(it.getPersonID(), it.getName(), horarios));
            }
        }
        return result;
    }

    private static boolean validHorarioAlmuerzo (DtoHorario horario) throws ParseException {
        boolean llegaHoraSucess = true;
        if(horario.getTime().size() == 4){
            String horaSalida = horario.getTime().get(1);
            String horaIngreso = horario.getTime().get(2);
            Double tiempoTrancurrido = calcularTiempoTrancurrido(horaSalida, horaIngreso);
            if(tiempoTrancurrido > 1.0){
                llegaHoraSucess = false;
            }
        }
        return llegaHoraSucess;
    }
    private static Double calcularTiempoTrancurrido (String horaInicio, String horaFin) throws ParseException {
        Date dateInicio = dateFormatHHMM.parse(horaInicio);
        Date dateFin = dateFormatHHMM.parse(horaFin);
        Double tiempotranscurrido = 0.0;

        if(dateInicio.getTime() < dateFin.getTime()){
            int tempresta = (int) (dateFin.getTime() - dateInicio.getTime());
            tiempotranscurrido = Math.rint(((double) tempresta / 3600000) * 100) / 100;
        } else if (dateInicio.getTime() < dateFin.getTime()) {
            tiempotranscurrido = 0.0;
        }else {
            tiempotranscurrido = 0.0;
        }
        return tiempotranscurrido;
    }
    private static boolean isFinDeSemana(String fecha) throws ParseException {
        boolean isFinDe = false;
        Date dateFecha = sformatFechaDDMMYYY.parse(fecha);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFecha);
        Integer numDia = calendar.get(Calendar.DAY_OF_WEEK);
        if(numDia == 1 || numDia == 7){
            isFinDe = true;
        }
        return isFinDe;
    }
    private static boolean validarRegistroNumeroTimbradas(DtoHorario horario) throws ParseException {
        boolean allRegister = true;
        if(horario.getTime().size() <= 3){
            allRegister = false;
        }
        return allRegister;
    }
    private static boolean validarHorarioIngreso (DtoHorario horario){
        LocalTime horaIngresoGano = LocalTime.parse(HORA_INGRESO) ;
        boolean isLate = false;
        LocalTime regIngreso = LocalTime.parse(horario.getTime().get(0));
        if( regIngreso.getHour() > horaIngresoGano.getHour() ){
            isLate = true;
        } else if (regIngreso.getHour() == horaIngresoGano.getHour()) {
            if(regIngreso.getMinute() >= 1){
                isLate = true;
            }
        }
        return isLate;
    }
}
