package com.codigo.clinica.msprescription.infraestructure.exceptions;

public class ResponseValidationException extends RuntimeException {
    public ResponseValidationException(String message) {
        super(message);
    }
}