package com.fpt.duantn.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

public class FormErrorUtil {
    public static Map<String,String> changeToMapError(BindingResult bindingResult){
        Map<String,String> map = new HashMap<>();
        for (FieldError fieldError:bindingResult.getFieldErrors()){
            map.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return map;
    }
    public static String changeToStringJson(BindingResult bindingResult){
        ObjectMapper objectMapper = new ObjectMapper();
        String errorsJson ="";
        try {
            errorsJson = objectMapper.writeValueAsString( changeToMapError(bindingResult));
        } catch (JsonProcessingException e) {

        }
        return errorsJson;
    }
}
