package com.yzgeneration.evc.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;

public class CustomUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String jsonSerialization(Object request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JsonSerializationException, e.getMessage());
        }
    }
}
