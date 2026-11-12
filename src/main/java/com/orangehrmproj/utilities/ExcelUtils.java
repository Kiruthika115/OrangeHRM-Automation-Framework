
package com.orangehrmproj.utilities;

import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    public static Object[][] getData(String filePath, String sheetName) {

        Object[][] data = null;

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            int rows = sheet.getLastRowNum();
            int columns = sheet.getRow(0).getLastCellNum();

            data = new Object[rows][columns];

            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= rows; i++) {

                Row row = sheet.getRow(i);

                for (int j = 0; j < columns; j++) {

                    Cell cell = row.getCell(
                        j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK
                    );

                    data[i - 1][j] = formatter.formatCellValue(cell);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to read Excel file: " + filePath, e);
        }

        return data;
    }
}
