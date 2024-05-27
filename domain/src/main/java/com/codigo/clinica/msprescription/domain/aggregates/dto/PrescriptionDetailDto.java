package com.codigo.clinica.msprescription.domain.aggregates.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrescriptionDetailDto {
    private Long id;

    private Integer amount;

    private String indications;

    private PrescriptionDto prescription;

    private MedicineDto medicine;

    private Long prescriptionId;
    private Long medicineId;

    private Integer status;
    private String createdBy;
    private Timestamp createOn;
    private String updatedBy;
    private Timestamp updatedOn;
    private String deletedBy;
    private Timestamp deletedOn;

}
