package com.codigo.clinica.msprescription.infraestructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Util {

    private Util() {
    }

    public static <T> String convertToString(T objectTo){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(objectTo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertFromString(String json, Class<T> classType){
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            return objectMapper.readValue(json, classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static  <T> T validateResponse(ResponseEntity<T> reponse){
        if(reponse.getStatusCode() == HttpStatus.OK){
            return reponse.getBody();
        }else if (reponse.getStatusCode() == HttpStatus.NOT_FOUND){
            throw new RuntimeException("Registro no encontrado.");
        }else{
            throw new RuntimeException("Un error desconocido ha sucedido.");
        }
    }
}
