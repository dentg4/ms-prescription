package com.codigo.clinica.msprescription.infraestructure.mapper;

import com.codigo.clinica.msprescription.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicalRecordDto;
import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDto;
import com.codigo.clinica.msprescription.infraestructure.entity.Prescription;

public class PrescriptionMapper {
    public static PrescriptionDto fromEntity(Prescription entity) {
        return PrescriptionDto.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .observations(entity.getObservations())
                .medicalRecordId(entity.getMedicalRecordId())
                .doctorId(entity.getDoctorId())
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .createOn(entity.getCreatedOn())
                .updatedBy(entity.getUpdatedBy())
                .updatedOn(entity.getUpdatedOn())
                .deletedBy(entity.getDeletedBy())
                .deletedOn(entity.getDeletedOn())
                .build();
    }
    public static PrescriptionDto fromEntity(Prescription entity, MedicalRecordDto medicalRecord, DoctorDto doctor){
        return PrescriptionDto.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .observations(entity.getObservations())
                .medicalRecord(medicalRecord)
                .doctor(doctor)
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .createOn(entity.getCreatedOn())
                .updatedBy(entity.getUpdatedBy())
                .updatedOn(entity.getUpdatedOn())
                .deletedBy(entity.getDeletedBy())
                .deletedOn(entity.getDeletedOn())
                .build();
    }
}
