package com.marchelo.excel.biome.Service;

import com.marchelo.excel.biome.Models.DtoDetailLaunchTime;
import com.marchelo.excel.biome.Models.DtoDetailUserOnTime;
import com.marchelo.excel.biome.Models.DtoHorario;
import com.marchelo.excel.biome.Models.UserBiometrico;
import com.marchelo.excel.biome.util.UtilDate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneracionReporte {
    public void setInformationReport(List<UserBiometrico> listOntime, List<UserBiometrico> listUserOnLate,
                                     List<UserBiometrico> listAllRegister,
                                     List<UserBiometrico> listLaunchTime) throws IOException, ParseException {
        generarArchivoExcelResult(listOntime, listUserOnLate, listAllRegister, listLaunchTime);
    }

    private void generarArchivoExcelResult(List<UserBiometrico> listOntime, List<UserBiometrico>listUserOnLate,
                                           List<UserBiometrico> listAllRegister,
                                           List<UserBiometrico> listLaunchTime) throws IOException, ParseException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ONTIME");
        generarSheetUserOnTime(workbook, sheet, listOntime);
        sheet = workbook.createSheet("LATE");
        generarSheetUserLate(workbook, sheet, listUserOnLate);
        sheet = workbook.createSheet("TIMBRADAS");
        generarSheetUserAllRegister(workbook, sheet, listAllRegister);
        sheet = workbook.createSheet("ALMUERZO");
        generarSheetUsersLaunchTime(workbook, sheet, listLaunchTime);

        String fechaFile = UtilDate.sformatFile.format(new Date());
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        System.out.println("path");
        System.out.println(path);
        String fileLocation = path.substring(0, path.length() - 1) + "RptBiometrico"+ fechaFile +".xlsx";
        System.out.println("fileLocation");
        System.out.println(fileLocation);

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }

    private void generarSheetUsersLaunchTime(Workbook workbook, Sheet sheet, List<UserBiometrico> listLaunchTime) throws ParseException {
        String[] headerReportLaunch ={"ID","NOMBRE","FECHA","BREAK OUT","BREAK IN","TIME"};
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        Row header = sheet.createRow(0);

        CellStyle style = EstilosExcel.crearStiloCabecera(workbook);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ID");
        headerCell.setCellStyle(style);

        headerCell = header.createCell(1);
        headerCell.setCellValue("NOMBRE");
        headerCell.setCellStyle(style);

        if(listLaunchTime.size() > 0){
            Integer rowNext = 1;
            for(int i=0; i < listLaunchTime.size(); i++){
                UserBiometrico user = listLaunchTime.get(i);
                Row row = sheet.createRow(rowNext);
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getPersonID());

                cell = row.createCell(1);
                cell.setCellValue(user.getName());
                rowNext = rowNext + 1;
            }

            //detalle
            rowNext = rowNext + 1;
            header = sheet.createRow(rowNext);
            //header
            for(int i=0; i< headerReportLaunch.length; i++){
                headerCell = header.createCell(i);
                headerCell.setCellValue(headerReportLaunch[i]);
                headerCell.setCellStyle(style);
            }

            //Recorre informacion
            List<DtoDetailLaunchTime> lista = getInfoDetailUserLaunchTime(listLaunchTime);
            rowNext = rowNext + 1;
            for(int i=0; i < lista.size(); i++){
                DtoDetailLaunchTime user = lista.get(i);
                Row row = sheet.createRow(rowNext);
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getId());
                cell = row.createCell(1);
                cell.setCellValue(user.getNombre());
                cell = row.createCell(2);
                cell.setCellValue(user.getFecha());
                cell = row.createCell(3);
                cell.setCellValue(user.getHoraOutLaunch());
                cell = row.createCell(4);
                cell.setCellValue(user.getHoraInLaunch());
                cell = row.createCell(5);
                cell.setCellValue(user.getTiempoLaunch());
                rowNext = rowNext + 1;
            }

        }else {
            Row row = sheet.createRow(1);
            Cell cell = row.createCell(1);
            cell.setCellValue("No existen registros para mostrar");
        }
    }
    private void generarSheetUserAllRegister(Workbook workbook, Sheet sheet, List<UserBiometrico> listAllRegister){
        String[] headerOnAllRegister ={"ID","NOMBRE","FECHA","CHECK IN", "BREAK OUT","BREAK IN","CHECK OUT"};
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        Row header = sheet.createRow(0);

        CellStyle style = EstilosExcel.crearStiloCabecera(workbook);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ID");
        headerCell.setCellStyle(style);

        headerCell = header.createCell(1);
        headerCell.setCellValue("NOMBRE");
        headerCell.setCellStyle(style);

        if(listAllRegister.size() > 0){
            Integer rowNext = 1;
            for(int i=0; i < listAllRegister.size(); i++){
                UserBiometrico user = listAllRegister.get(i);
                Row row = sheet.createRow(rowNext);
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getPersonID());

                cell = row.createCell(1);
                cell.setCellValue(user.getName());
                rowNext = rowNext + 1;
            }

            //detalle
            rowNext = rowNext + 1;
            header = sheet.createRow(rowNext);
            //header
            for(int i=0; i< headerOnAllRegister.length; i++){
                headerCell = header.createCell(i);
                headerCell.setCellValue(headerOnAllRegister[i]);
                headerCell.setCellStyle(style);
            }
            //Recorre informacion
            List<DtoDetailUserOnTime> lista = getInfoDetailUserAllRegister(listAllRegister);
            rowNext = rowNext + 1;
            for(int i=0; i < lista.size(); i++){
                DtoDetailUserOnTime user = lista.get(i);
                Row row = sheet.createRow(rowNext);
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getId());
                cell = row.createCell(1);
                cell.setCellValue(user.getNombre());
                cell = row.createCell(2);
                cell.setCellValue(user.getFecha());
                cell = row.createCell(3);
                cell.setCellValue(user.getHoraIngreso());
                cell = row.createCell(4);
                cell.setCellValue(user.getHoraOutLaunch());
                cell = row.createCell(5);
                cell.setCellValue(user.getHoraInLaunch());
                cell = row.createCell(6);
                cell.setCellValue(user.getHoraSalida());
                rowNext = rowNext + 1;
            }
        }else{
            Row row = sheet.createRow(1);
            Cell cell = row.createCell(1);
            cell.setCellValue("No existen registros para mostrar");
        }
    }
    private void generarSheetUserOnTime (Workbook workbook, Sheet sheet, List<UserBiometrico> listOntime){
        String[] headerOnTimeUser ={"ID","NOMBRE","FECHA","INGRESO"};

        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        Row header = sheet.createRow(0);

        CellStyle style = EstilosExcel.crearStiloCabecera(workbook);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ID");
        headerCell.setCellStyle(style);
        headerCell = header.createCell(1);
        headerCell.setCellValue("NOMBRE");
        headerCell.setCellStyle(style);

        if(listOntime.size() > 0){
            Integer rowNext = 1;
            for(int i=0; i < listOntime.size(); i++){
                UserBiometrico user = listOntime.get(i);
                Row row = sheet.createRow(rowNext);
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getPersonID());

                cell = row.createCell(1);
                cell.setCellValue(user.getName());
                rowNext = rowNext + 1;
            }
            //TABLE 2
            rowNext = rowNext + 1;
            header = sheet.createRow(rowNext);
            //HEADER
            for(int i=0; i< headerOnTimeUser.length; i++){
                headerCell = header.createCell(i);
                headerCell.setCellValue(headerOnTimeUser[i]);
                headerCell.setCellStyle(style);
            }
            //BODY
            List<DtoDetailUserOnTime> lista = getInfoDetailUserOnTime(listOntime);
            rowNext = rowNext + 1;
            for(int i=0; i < lista.size(); i++){
                DtoDetailUserOnTime user = lista.get(i);
                Row row = sheet.createRow(rowNext);
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getId());
                cell = row.createCell(1);
                cell.setCellValue(user.getNombre());
                cell = row.createCell(2);
                cell.setCellValue(user.getFecha());
                cell = row.createCell(3);
                cell.setCellValue(user.getHoraIngreso());
                rowNext = rowNext + 1;
            }
        }else{
            Row row = sheet.createRow(1);
            Cell cell = row.createCell(1);
            cell.setCellValue("No existen registros para mostrar");
        }
    }
    private void generarSheetUserLate(Workbook workbook, Sheet sheet, List<UserBiometrico> listUserOnLate){
        String[] headerOnLate ={"ID","NOMBRE","FECHA","INGRESO"};
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        Row header = sheet.createRow(0);

        CellStyle style = EstilosExcel.crearStiloCabecera(workbook);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ID");
        headerCell.setCellStyle(style);
        headerCell = header.createCell(1);
        headerCell.setCellValue("NOMBRE");
        headerCell.setCellStyle(style);

        if(listUserOnLate.size()>0){
            Integer rowNext = 1;
            for(int i=0; i < listUserOnLate.size(); i++){
                UserBiometrico user = listUserOnLate.get(i);
                Row row = sheet.createRow(rowNext);
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getPersonID());

                cell = row.createCell(1);
                cell.setCellValue(user.getName());
                rowNext = rowNext + 1;
            }
            //TABLE 2
            rowNext = rowNext + 1;
            header = sheet.createRow(rowNext);
            //HEADER
            for(int i=0; i< headerOnLate.length; i++){
                headerCell = header.createCell(i);
                headerCell.setCellValue(headerOnLate[i]);
                headerCell.setCellStyle(style);
            }
            //BODY
            List<DtoDetailUserOnTime> lista = getInfoDetailUserOnTime(listUserOnLate);
            rowNext = rowNext + 1;
            for(int i=0; i < lista.size(); i++){
                DtoDetailUserOnTime user = lista.get(i);
                Row row = sheet.createRow(rowNext);
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getId());
                cell = row.createCell(1);
                cell.setCellValue(user.getNombre());
                cell = row.createCell(2);
                cell.setCellValue(user.getFecha());
                cell = row.createCell(3);
                cell.setCellValue(user.getHoraIngreso());
                rowNext = rowNext + 1;
            }
        }else{
            Row row = sheet.createRow(1);
            Cell cell = row.createCell(1);
            cell.setCellValue("No existen registros para mostrar");
        }
    }

    private List<DtoDetailLaunchTime> getInfoDetailUserLaunchTime (List<UserBiometrico> listLaucnTime) throws ParseException {
        List<DtoDetailLaunchTime> result = new ArrayList<>();
        for(UserBiometrico it: listLaucnTime){
            List<DtoHorario> listHorarios = it.getDate();
            for(DtoHorario h : listHorarios){
                if(h.getTime().size() == 4){
                    Date tiempoTranscurrido = UtilDate.calcularTiempoTrancurridoTime(h.getTime().get(1),
                            h.getTime().get(2));
                    String timeLaunch = UtilDate.dateFormatHHMM.format(tiempoTranscurrido);
                    result.add(new DtoDetailLaunchTime(it.getPersonID(), it.getName(),
                            h.getFecha(), h.getTime().get(1),h.getTime().get(2), timeLaunch));
                }
            }
        }
        return result;
    }
    private List<DtoDetailUserOnTime> getInfoDetailUserOnTime (List<UserBiometrico> listaUserOnTime){
        List<DtoDetailUserOnTime> result = new ArrayList<>();
        for(UserBiometrico it : listaUserOnTime){
            List<DtoHorario> listHorarios = it.getDate();
            for(DtoHorario h : listHorarios){
                result.add(new DtoDetailUserOnTime(it.getPersonID(), it.getName(),h.getFecha(), h.getTime().get(0)));
            }
        }
        return result;
    }
    private List<DtoDetailUserOnTime> getInfoDetailUserAllRegister (List<UserBiometrico> listaAllRegister){
        List<DtoDetailUserOnTime> result = new ArrayList<>();
        for(UserBiometrico it : listaAllRegister){
            List<DtoHorario> listHorarios = it.getDate();
            for(DtoHorario h : listHorarios){
                if(h.getTime().size() == 4){
                    result.add(new DtoDetailUserOnTime(it.getPersonID(), it.getName(),h.getFecha(), h.getTime().get(0),
                            h.getTime().get(1), h.getTime().get(2), h.getTime().get(3)));
                }
            }
        }
        return result;
    }
}
