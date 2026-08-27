package com.app.utilities;

import org.testng.annotations.DataProvider;

import java.io.FileNotFoundException;
import java.util.List;

public class DataProviders {

    private static final String FILE_PATH = System.getProperty("user.dir")+"/src/test/resources/testdata/TestData.xlsx";

    @DataProvider(name="validLoginData")
    public static Object[][] validLoginData() throws FileNotFoundException {
        return getSheetData("validLoginData");
    }

    @DataProvider(name="InValidLoginData")
    public static Object[][] InValidLoginData() throws FileNotFoundException {
        return getSheetData("InValidLoginData");
    }


    public static Object[][] getSheetData(String sheetName) throws FileNotFoundException {

        List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH,sheetName);
        Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];

        for(int i=0; i<sheetData.size(); i++){
            data[i] = sheetData.get(i);
        }
        return data;
    }
}
