package com.codigo.clinica.msprescription.domain.aggregates.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicalRecordDto {
    private Long id;
    private String diagnos;
    private String reference;
    private Timestamp date;
    private Long patientId;
}
