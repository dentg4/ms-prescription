package com.codigo.clinica.msprescription.application.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponse {
    private String error;
    private String message;
}
