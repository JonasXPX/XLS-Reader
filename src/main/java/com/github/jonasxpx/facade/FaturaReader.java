package com.github.jonasxpx.facade;

import java.io.File;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import static java.lang.String.format;


public class FaturaReader extends Thread {

    @Override
    public void run() {
         try (Workbook workbook = WorkbookFactory.create(new File("extrato.xls"))){
            
            Integer cellCount = workbook.getNumCellStyles();
            Integer sheetsCount = workbook.getNumberOfSheets();

            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            
            while(sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                System.out.println(format("Sheet name: %s", sheet.getSheetName()));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
