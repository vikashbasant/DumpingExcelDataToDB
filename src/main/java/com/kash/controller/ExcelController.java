package com.kash.controller;

import com.kash.dto.Response;
import com.kash.exception.ExcelException;
import com.kash.util.ExcelService;
import com.kash.util.ExcelServiceFactory;
import com.kash.util.ExcelServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
@CrossOrigin("*")
@Slf4j
public class ExcelController {

    @Autowired
    private ExcelServiceFactory factory;

    @PostMapping("/product/upload")
    public ResponseEntity<Response> upload(@RequestParam("file") MultipartFile file) throws ExcelException {

        log.info("===: ExcelController:: Inside downloadExcelFileOfProduct Method :===");
        ExcelService service = factory.getService(ExcelServiceType.UPLOAD_EXCEL_DATA_TO_DB);
        Response response = service.executeService(file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/products")
    public ResponseEntity<Response> getAllProducts() throws ExcelException {
        log.info("===: ExcelController:: Inside getAllProducts Method :===");
        ExcelService service = factory.getService(ExcelServiceType.FETCH_ALL_PRODUCTS);
        Response response = service.executeService("");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> downloadExcelFileOfProduct() throws ExcelException {

        log.info("===: ExcelController:: Inside downloadExcelFileOfProduct Method :===");
        ExcelService service = factory.getService(ExcelServiceType.DOWNLOAD_EXCEL_DATA_FROM_DB);
        Response response = service.executeService("");

        return ResponseEntity
                .ok()
                .headers((HttpHeaders) response.getHeaders())
                .body(response.getBodyData());

    }


}
