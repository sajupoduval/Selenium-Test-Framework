package com.app.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtility {

    public static List<String[]> getSheetData(String filePath, String sheetName) throws FileNotFoundException {
        //Data variable is defined as list of arrays of string
        List<String[]> data = new ArrayList<>();
//        validLoginData
        try(FileInputStream fis = new FileInputStream(filePath);Workbook workbook = new XSSFWorkbook(fis)){

            System.out.println("Number of sheets: " + workbook.getNumberOfSheets());
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                System.out.println("Sheet: " + workbook.getSheetName(i));
            }

            Sheet sheet = workbook.getSheet(sheetName);
            if(sheet==null){
                throw new IllegalArgumentException("Sheet "+ sheetName + " doesn't exists");
            }

        for(Row row: sheet){
            if(row.getRowNum()==0){
                continue;
            }
            //Read all cells in the row
            List<String> rowData = new ArrayList<>();
            for(Cell cell:row){
                rowData.add(getCellValue(cell));
            }
            //Converts rowDate to string
            data.add(rowData.toArray(new String[0]));

            }

        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }

    }
}
