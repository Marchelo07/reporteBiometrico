package com.marchelo.excel.biome.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EstilosExcel {

    public static Sheet setWidthColumnGeneric (Sheet sheet){
        if(sheet != null){
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 6000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 4000);
            return sheet;
        }else {
            return null;
        }
    }
    public static Sheet setWidthColumnLaunchTime (Sheet sheet){
        if(sheet != null){
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 6000);
            sheet.setColumnWidth(2, 6000);
            sheet.setColumnWidth(3, 4000);
            sheet.setColumnWidth(4, 4000);
            sheet.setColumnWidth(5, 4000);
            return sheet;
        }else {
            return null;
        }
    }
    public static Sheet setWidthColumnAllRegister (Sheet sheet){
        if(sheet != null){
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 6000);
            sheet.setColumnWidth(2, 6000);
            sheet.setColumnWidth(3, 4000);
            sheet.setColumnWidth(4, 4000);
            sheet.setColumnWidth(5, 4000);
            sheet.setColumnWidth(6, 4000);
            return sheet;
        }else {
            return null;
        }
    }
    public static CellStyle crearStiloCabecera (Workbook workbook ){
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);

        CellStyle style = null;
        style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);

        return style;
    }
}
