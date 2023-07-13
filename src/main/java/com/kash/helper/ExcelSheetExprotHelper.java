package com.kash.helper;

import com.kash.constant.ExcelSheetConstant;
import com.kash.model.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelSheetExprotHelper {



    public static ByteArrayInputStream dataToExcel(List<Product> products) throws IOException {

        // Create Work Book:
        XSSFWorkbook workbook = new XSSFWorkbook();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            // Create Sheet:
            XSSFSheet sheet = workbook.createSheet(ExcelSheetConstant.SHEET_NAME);

            // Set Header of Excel File is Bold:
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Create Row: Header in Excel Sheet:
            Row row = sheet.createRow(0);
            for (int i = 0; i < (ExcelSheetConstant.HEADERS).length; i++) {
                // Create Cell:
                Cell cell = row.createCell(i);
                // set the value into cell:
                cell.setCellValue(ExcelSheetConstant.HEADERS[i]);
                cell.setCellStyle(headerCellStyle);

            }

            // Create Row for value:
            int rowIndex = 1;
            for (Product product : products) {

                // Create Row:
                XSSFRow dataRow = sheet.createRow(rowIndex);
                // increase rowIndex by 1:
                rowIndex++;

                // Create a Cell and Set the value to it:
                dataRow.createCell(0).setCellValue(product.getProductId());
                dataRow.createCell(1).setCellValue(product.getProductName());
                dataRow.createCell(2).setCellValue(product.getProductDesc());
                dataRow.createCell(3).setCellValue(product.getProductPrice());
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to import data in excel");
            return new ByteArrayInputStream(out.toByteArray());
        } finally {

            workbook.close();
            out.close();


        }

    }
}
