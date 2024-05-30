package com.codigo.clinica.msprescription.domain.aggregates.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class PrescriptionRequest {

    @NotBlank(message = "El campo fecha es necesario.")
    private Date date;

    @NotBlank(message = "El campo observación es necesario.")
    private String observations;

    @NotBlank(message = "El campo doctor es necesario.")
    private Long doctorId;

    @NotBlank(message = "El campo historial médico es necesario.")
    private Long medicalRecordId;

}
