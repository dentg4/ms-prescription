package com.codigo.clinica.msprescription.domain.aggregates.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PrescriptionDetailRequest {

    private Integer amount;

    private String indications;

    private Long prescriptionId;

    private Long medicineId;
}
