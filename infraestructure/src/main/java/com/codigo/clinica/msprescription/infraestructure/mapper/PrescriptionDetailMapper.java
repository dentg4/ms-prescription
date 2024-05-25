package com.codigo.clinica.msprescription.infraestructure.mapper;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDetailDto;
import com.codigo.clinica.msprescription.infraestructure.entity.PrescriptionDetail;

public class PrescriptionDetailMapper {
    public static PrescriptionDetailDto fromEntity(PrescriptionDetail prescriptionDetail) {
        return PrescriptionDetailDto.builder()
                .id(prescriptionDetail.getId())
                .amount(prescriptionDetail.getAmount())
                .indications(prescriptionDetail.getIndications())
                .prescriptionId(prescriptionDetail.getPrescription().getId())
                .medicine(MedicineMapper.fromEntity(prescriptionDetail.getMedicine()))
                .build();
    }
}
