package com.kash.service.impl;

import com.kash.constant.ExcelSheetConstant;
import com.kash.constant.ResponseConstant;
import com.kash.dto.Response;
import com.kash.exception.ExcelException;
import com.kash.helper.ExcelSheetExprotHelper;
import com.kash.model.Product;
import com.kash.repo.ProductRepo;
import com.kash.util.ExcelService;
import com.kash.util.ExcelServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class DownloadExcelData implements ExcelService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private Response response;


    @Override
    public ExcelServiceType getServiceType() {
        return ExcelServiceType.DOWNLOAD_EXCEL_DATA_FROM_DB;
    }

    @Override
    public <T> Response executeService(T t) throws ExcelException {

        log.info("==> DownloadExcelData:: Inside executeService Method <==");

        //=>> Fetch all the Product from DB:
        List<Product> products = productRepo.findAll();

        //=> If No Product Found, then simply return exception:
        if (products.isEmpty()) {
            throw new ExcelException("No Records Found!");
        }


        InputStreamResource file;
        try {

            //=>> Now Convert The List of product to Excel Sheet: Main Logic
            ByteArrayInputStream byteArrayInputStream = ExcelSheetExprotHelper.dataToExcel(products);

            // Convert ByteArrayInputStream to InputStreamResource:
            file = new InputStreamResource(byteArrayInputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //=> Creating headers:
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ExcelSheetConstant.DEFAULT_DOWNLOAD_EXCEL_FILE_NAME);
        headers.setContentType(MediaType.parseMediaType(ExcelSheetConstant.MEDIA_TYPE_OF_EXCEL_SHEET));




        /*----Simply Return The Response----*/
        response.setStatus(ResponseConstant.STATUS);
        response.setStatusCode(ResponseConstant.STATUS_CODE);
        response.setMessage("Successfully! Download Excel Data!");
        response.setBodyData(file);
        response.setHeaders(headers);


        return response;

    }

}
