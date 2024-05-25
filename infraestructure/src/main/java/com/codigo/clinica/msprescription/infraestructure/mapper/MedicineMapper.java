package com.codigo.clinica.msprescription.infraestructure.mapper;

import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicineDto;
import com.codigo.clinica.msprescription.infraestructure.entity.Medicine;

public class MedicineMapper {
    public static MedicineDto fromEntity(Medicine medicine) {
        return MedicineDto.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .description(medicine.getDescription())
                .status(medicine.getStatus())
                .createdBy(medicine.getCreatedBy())
                .createOn(medicine.getCreatedOn())
                .updatedBy(medicine.getUpdatedBy())
                .updatedOn(medicine.getUpdatedOn())
                .deletedBy(medicine.getDeletedBy())
                .deletedOn(medicine.getDeletedOn())
                .build();
    }
}
