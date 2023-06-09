package com.marchelo.excel.biome.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.marchelo.excel.biome.Models.DtoHorario;
import com.marchelo.excel.biome.Models.UserBiometrico;
import com.marchelo.excel.biome.util.Error;
import com.marchelo.excel.biome.util.UtilDate;
import com.marchelo.excel.biome.util.UtilString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LecturaBiometrico {

    private static String[] cabecera ={"Person ID","Name","Time","Department","Attendance Status"};
    private static Integer numColums = cabecera.length;
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
    DateFormat outsfd = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    SimpleDateFormat sformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    static DateFormat dateFormatHHMM = new SimpleDateFormat("HH:mm");
    SimpleDateFormat sformatFechaDDMMYYY = new SimpleDateFormat("dd-MM-yyyy");

    public void lecturaArchivo() throws IOException, ParseException {
        System.out.println("Leyendo archivos...");
        buscarArchivoExcel();
    }

    public void buscarArchivoExcel() throws IOException {

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1);

        try{
            String rutaFile =  fileLocation + "Biometrico.xlsx";
            FileInputStream inputStream = new FileInputStream(new File(rutaFile));
            Workbook workbook = new XSSFWorkbook(inputStream);
            String nameSheet = "";
            Integer numsSheets = workbook.getNumberOfSheets();
            for(int i=0; i < numsSheets; i++){
                nameSheet = workbook.getSheetName(i);
                Sheet datos = workbook.getSheetAt(i);
                recorrerSheet(datos, nameSheet);
            }

        }catch (Exception e){
            System.out.println("ERROR: No se puede encontrar el archivo Biometrico.xlsx");
            String error = "ERROR: No se puede encontrar el archivo Biometrico.xlsx";
            Error.escribirLog(fileLocation,error);
            e.printStackTrace();
        }
    }

    private void recorrerSheet(Sheet datos, String nameSheet) throws ParseException, IOException {
        try{
            List<String> lineRow = null;
            System.out.println(datos.getPhysicalNumberOfRows());
            List<UserBiometrico> listInfoBiome = new ArrayList<>();
            for(int i = 1; i < datos.getPhysicalNumberOfRows(); i++){
                XSSFRow row = (XSSFRow) datos.getRow(i);
                lineRow = new ArrayList<String>();
                JsonObject obj = new JsonObject();
                UserBiometrico dto = new UserBiometrico();
                for(int j=0; j< row.getPhysicalNumberOfCells(); j++){
                    if(j <= (numColums - 1) ){
                        if(j == 2){
                            DataFormatter formatter = new DataFormatter();
                            String strfecha = formatter.formatCellValue(row.getCell(j));
                            String fecha = getFechaFormat(new Date(strfecha));
                            lineRow.add(fecha);
                        }else {
                            lineRow.add(row.getCell(j).toString());
                        }
                    }
                }
                dto = verificacionRow(lineRow);
                if(listInfoBiome.size() == 0){
                    listInfoBiome.add(dto);
                }else{
                    listInfoBiome = organizarArrayInformacion(listInfoBiome, dto);
                }
            }
            listInfoBiome.stream().forEach(i->{
                i.setName(UtilString.capitalize(i.getName()));
            });
            Collections.sort(listInfoBiome, new Comparator<UserBiometrico>() {
                @Override
                public int compare(UserBiometrico o1, UserBiometrico o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            Gson gson = new Gson();
            generacionReportes(listInfoBiome, nameSheet);
        }catch (Exception e){
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1);
            String error = "ERROR: Al leer archivo de excel ("+ nameSheet +")";
            Error.escribirLog(fileLocation,error);
        }
    }

    private void generacionReportes(List<UserBiometrico> infoUsersBiome, String nameSheet) throws ParseException, IOException {
        Gson gson = new Gson();
        List<UserBiometrico> listUserOnTime = ProcesarInformacion.usersOnTime(infoUsersBiome);
        List<UserBiometrico> listUserOnLate = ProcesarInformacion.usersOnLate(infoUsersBiome);
        List<UserBiometrico> listRegisterAll = ProcesarInformacion.userCompleteRegister(listUserOnTime);
        List<UserBiometrico> listRegisterFinDe = ProcesarInformacion.userRegisterFinDe(infoUsersBiome);
        List<UserBiometrico> listLauchAtime =  ProcesarInformacion.userLaunchATime(listRegisterAll);
        List<UserBiometrico> listNoTimbra4Veces = ProcesarInformacion.userNoTimbra4Veces(infoUsersBiome);
        List<UserBiometrico> lateHoraAlmuerzo = ProcesarInformacion.userLateLaunch(infoUsersBiome);

        GeneracionReporte reporte = new GeneracionReporte();
        reporte.setInformationReport(listUserOnTime, listUserOnLate, listRegisterAll, listRegisterFinDe, listLauchAtime,
                listNoTimbra4Veces, lateHoraAlmuerzo,nameSheet);
    }
    private List<UserBiometrico> organizarArrayInformacion(List<UserBiometrico> list, UserBiometrico dto){
        List<UserBiometrico> listNewBiome = new ArrayList<>();
        String newDate = dto.getDate().get(0).getFecha();
        String newTime = dto.getDate().get(0).getTime().get(0);

        Optional<UserBiometrico> findUserBio = list.stream().filter(i-> dto.getPersonID().equals(i.getPersonID())).findFirst();
        if(findUserBio.isPresent()){
            list.stream().filter(i-> dto.getPersonID().equals(i.getPersonID()))
                .forEach(u -> {
                    Optional<DtoHorario> horario =  u.getDate().stream().filter(i-> dto.getDate().get(0).getFecha().equals(i.getFecha())).findFirst();
                    if(horario.isPresent()){
                        u.getDate().stream().filter(i-> dto.getDate().get(0).getFecha().equals(i.getFecha()))
                            .forEach( d -> {
                                List<String> tiempos = d.getTime();
                                if(!tiempos.contains(newTime)){
                                    tiempos.add(newTime);
                                }
                                Collections.sort(tiempos);
                                if(tiempos.size() > 4){
                                    d.setTime(limpiarHorio(tiempos));
                                }else{
                                    d.setTime(tiempos);
                                }
                            });
                        Collections.sort(u.getDate(), new Comparator<DtoHorario>() {
                            @Override
                            public int compare(DtoHorario o1, DtoHorario o2) {
                                return o1.getFecha().compareTo(o2.getFecha());
                            }
                        });
                    }else {
                        u.getDate().add(new DtoHorario(newDate, new ArrayList<>(Collections.singleton(newTime))));
                    }
                });
        }else {
            list.add(dto);
        }
        listNewBiome = list;
        return listNewBiome;
    }
    private List<String> limpiarHorio (List<String> horario){
        List<String> newListHorario = new ArrayList<>();
        List<String> listaOld = horario;
        String ingreso = horario.get(0);
        String entrada = horario.get(horario.size() - 1);
        listaOld.remove(0);
        listaOld.remove(horario.size() - 1);
        newListHorario.add(ingreso);
        newListHorario.add(entrada);
        newListHorario.add(listaOld.get(0));
        newListHorario.add(listaOld.get(listaOld.size() - 1));
        Collections.sort(newListHorario);
        return newListHorario;
    }
    private List<DtoHorario> getDtoHorario(String date) throws ParseException {
        List<DtoHorario> listaHorario = new ArrayList<>();
        List<String> listTime = new ArrayList<>();
        String fecha = getFecha(date);
        String time = getHoursMinutes(date);
        listTime.add(time);
        DtoHorario dtoHorario = new DtoHorario();
        dtoHorario.setFecha(fecha);
        dtoHorario.setTime(listTime);
        listaHorario.add(dtoHorario);
        return listaHorario;
    }
    private UserBiometrico verificacionRow (List<String> lineRow) throws ParseException {
        UserBiometrico dto = new UserBiometrico();
        dto.setPersonID(lineRow.get(0));
        dto.setName(lineRow.get(1));
        List<DtoHorario> dtoHorario = getDtoHorario(lineRow.get(2));
        dto.setDate(dtoHorario);
        return dto;
    }
    private String getFechaFormat (Date fecha) throws ParseException {
        Date dateTemp = null;
        String souStrDate;
        String strDate = sdf.format(fecha);
        dateTemp = sdf.parse(strDate);
        souStrDate = outsfd.format(dateTemp);
        return souStrDate;
    }
    public String getFecha (String strFecha) throws ParseException {
        Date date = sformat.parse(strFecha);
        String fecha = sformatFechaDDMMYYY.format(date);
        return fecha;
    }
    public String getHoursMinutes (String strFecha) throws ParseException {
        Date date = sformat.parse(strFecha);
        long time = date.getTime();
        String horario = dateFormatHHMM.format(time);
        return horario;
    }
}
