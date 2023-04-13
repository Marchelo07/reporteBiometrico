package com.marchelo.excel.biome.util;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Error {

    public static void escribirLog(String rutaArchivo, String mensaje) {

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;
        try {
            String fecha = UtilDate.sformatFile.format(new Date());
            rutaArchivo = rutaArchivo.concat("Error_").concat(fecha).concat(".txt");
            fh = new FileHandler(rutaArchivo, true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info(mensaje);
            fh.close();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
