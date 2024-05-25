package com.codigo.clinica.msprescription.domain.aggregates.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PrescriptionDetailListRequest {

    private Long prescriptionId;
    private List<DetailMedicinePair> list;
}
