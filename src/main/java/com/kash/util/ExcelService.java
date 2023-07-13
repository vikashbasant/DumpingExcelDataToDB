package com.kash.util;

import com.kash.dto.Response;
import com.kash.exception.ExcelException;

public interface ExcelService {

    ExcelServiceType getServiceType ();

    <T> Response executeService (T t) throws ExcelException;
}
