package com.kash.service.impl;

import com.kash.constant.ResponseConstant;
import com.kash.dto.Response;
import com.kash.exception.ExcelException;
import com.kash.helper.MyExcelHelper;
import com.kash.model.Product;
import com.kash.repo.ProductRepo;
import com.kash.util.ExcelService;
import com.kash.util.ExcelServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@Slf4j
public class UploadExcelData implements ExcelService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private Response response;


    @Override
    public ExcelServiceType getServiceType() {
        return ExcelServiceType.UPLOAD_EXCEL_DATA_TO_DB;
    }


    @Override
    public <T> Response executeService(T t) throws ExcelException {

        log.info("=>> UploadExcelData:: Inside executeService Method <<=");

        // Getting the MultipartFile from t:
        MultipartFile file = (MultipartFile) t;

        // Creating reference of List<Product>:
        List<Product> allData;

        // Here we are checking Multipart file is a type of Excel file or not:
        if (MyExcelHelper.checkExcelFormat(file)) {

            try {

                // convert Multipart Excel file to list of product:
                List<Product> products = MyExcelHelper.convertExcelToListOfProducts(file.getInputStream());

                // Then simply save into Database:
                allData = this.productRepo.saveAll(products);

            } catch (IOException e) {
                throw new ExcelException("Some IO Exception Occurs");
            }

        } else {
            throw new ExcelException("Please Upload Excel File Only!");
        }



        /*----Simply Return The Response----*/
        response.setStatus(ResponseConstant.STATUS);
        response.setStatusCode(ResponseConstant.STATUS_CODE);
        response.setMessage("Successfully Excel File Data Is Uploaded & Data Is Save To DB");
        response.setData(allData);

        return response;
    }
}
