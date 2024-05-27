package com.codigo.clinica.msprescription.domain.aggregates.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDetailListRequest {

    private Long prescriptionId;
    private List<DetailMedicinePair> list;
}
