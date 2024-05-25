package com.codigo.clinica.msprescription.domain.aggregates.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class PrescriptionRequest {

    private Date date;

    private String observations;

    private Long doctorId;

    private Long medicalRecordId;

//    private List<PrescriptionDetailDto> prescriptionDetails;
}
