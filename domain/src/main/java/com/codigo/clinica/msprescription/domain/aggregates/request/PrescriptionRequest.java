package com.codigo.clinica.msprescription.domain.aggregates.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class PrescriptionRequest {

    @NotNull(message = "El campo fecha es necesario.")
    private Date date;

    @NotBlank(message = "El campo observación es necesario.")
    private String observations;

    @NotNull(message = "El campo doctor es necesario.")
    private Long doctorId;

    @NotNull(message = "El campo historial médico es necesario.")
    private Long medicalRecordId;

}
