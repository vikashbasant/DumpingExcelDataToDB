package com.kash.util;

import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ExcelServiceFactory {

    private final Map<ExcelServiceType, ExcelService> serviceMap = new EnumMap<>(ExcelServiceType.class);

    public ExcelServiceFactory(Set<ExcelService> actionSet) {
        actionSet.forEach(this::createAction);
    }

    private void createAction(ExcelService excelService) {
        serviceMap.put(excelService.getServiceType(), excelService);
    }

    public ExcelService getService(ExcelServiceType type) {
        return Optional.ofNullable(serviceMap.get(type)).orElseThrow(() -> new RuntimeException("Action (" + type +
                ") is not implemented yet"));
    }
}
