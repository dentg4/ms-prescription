package com.codigo.clinica.msprescription.infraestructure.exceptions;

public class JsonConversionException extends RuntimeException{
    public JsonConversionException(String msj, Throwable cause){
        super(msj, cause);
    }
}
