package com.tbd.DeliveryMedicamentos.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverJsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir a JSON", e);
        }
    }
}
