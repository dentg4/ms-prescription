package com.codigo.clinica.msprescription.domain.aggregates.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDetailRequest {

    private Integer amount;

    private String indications;

    private Long prescriptionId;

    private Long medicineId;
}
