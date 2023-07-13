package com.kash.helper;

import com.kash.constant.ExcelSheetConstant;
import com.kash.model.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyExcelHelper {

    //=>> Excel File Name:
    private static String sheetName;


    // Check the file is of an Excel type or not:
    public static boolean checkExcelFormat(MultipartFile file) {

        // Get the contentType of Excel file:
        String contentType = file.getContentType();

        // setting sheet Name:
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        int lastDotIndex = originalFilename.lastIndexOf('.');
        sheetName = originalFilename.substring(0, lastDotIndex);

        assert contentType != null;
        return contentType.equals(ExcelSheetConstant.CONTENT_TYPE_OF_EXCEL_SHEET);

    }


    // Convert Excel Data to a list of products:
    public static List<Product> convertExcelToListOfProducts(InputStream is) {

        // Create an object of List<Product>:
        List<Product> products = new ArrayList<>();

        try {

            // getting a workbook sheet from InputStream is:
            XSSFWorkbook workbook = new XSSFWorkbook(is);

            // getting the sheet from workbook:
            XSSFSheet sheet = workbook.getSheet(sheetName);

            int rowNumber = 0;
            // Getting Iterator of a sheet:
            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {

                // getting the First Row, that is Headers:
                Row row = iterator.next();

                if (rowNumber == 0) {
                    rowNumber++;

                    // For First i.e Header. So don't save into DB
                    continue;
                }

                // Creating an object of Product for internal uses:
                Product product = new Product();

                // Fetch the cell one-by-one:
                Iterator<Cell> cells = row.iterator();
                int cellId=0;
                while (cells.hasNext()) {

                    // getting the cell:
                    Cell cell = cells.next();

                    switch (cellId) {
                        case 0:
                            product.setProductId((int)cell.getNumericCellValue());
                            break;
                        case 1:
                            product.setProductName(cell.getStringCellValue());
                            break;
                        case 2:
                            product.setProductDesc(cell.getStringCellValue());
                            break;
                        case 3:
                            product.setProductPrice(cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }

                    cellId++;
                }

                products.add(product);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}
