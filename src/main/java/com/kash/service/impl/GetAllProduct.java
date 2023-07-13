package com.kash.service.impl;

import com.kash.constant.ResponseConstant;
import com.kash.dto.Response;
import com.kash.exception.ExcelException;
import com.kash.model.Product;
import com.kash.repo.ProductRepo;
import com.kash.util.ExcelService;
import com.kash.util.ExcelServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GetAllProduct implements ExcelService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private Response response;


    @Override
    public ExcelServiceType getServiceType() {
        return ExcelServiceType.FETCH_ALL_PRODUCTS;
    }

    @Override
    public <T> Response executeService(T t) throws ExcelException {

        log.info("=>> GetAllProduct:: Inside executeService Method <<=");

        //=>> Fetch all the Product from DB:
        List<Product> allProducts = this.productRepo.findAll();

        //=> If No Product Found, then simply return exception:
        if (allProducts.isEmpty()) {
            throw new ExcelException("No products Found");
        }


        /*----Simply Return The Response----*/
        response.setStatus(ResponseConstant.STATUS);
        response.setStatusCode(ResponseConstant.STATUS_CODE);
        response.setMessage("Successfully Fetch All Records Of The Product From DB");
        response.setData(allProducts);

        return response;
    }
}
