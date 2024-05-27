package com.codigo.clinica.msprescription.infraestructure.mapper;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDetailDto;
import com.codigo.clinica.msprescription.infraestructure.entity.PrescriptionDetail;

public class PrescriptionDetailMapper {
    public static PrescriptionDetailDto fromEntity(PrescriptionDetail prescriptionDetail) {
        return PrescriptionDetailDto.builder()
                .id(prescriptionDetail.getId())
                .amount(prescriptionDetail.getAmount())
                .indications(prescriptionDetail.getIndications())
                .prescriptionId(prescriptionDetail.getPrescription()==null?null:prescriptionDetail.getPrescription().getId())
                .medicine(prescriptionDetail.getMedicine()==null?null:MedicineMapper.fromEntity(prescriptionDetail.getMedicine()))
                .status(prescriptionDetail.getStatus())
                .createdBy(prescriptionDetail.getCreatedBy())
                .createOn(prescriptionDetail.getCreatedOn())
                .updatedBy(prescriptionDetail.getUpdatedBy())
                .updatedOn(prescriptionDetail.getUpdatedOn())
                .deletedBy(prescriptionDetail.getDeletedBy())
                .deletedOn(prescriptionDetail.getDeletedOn())
                .build();
    }
}
