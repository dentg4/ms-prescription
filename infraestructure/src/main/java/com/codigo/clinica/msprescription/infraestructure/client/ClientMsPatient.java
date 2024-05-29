package com.codigo.clinica.msprescription.infraestructure.client;

import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicalRecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-patient")
public interface ClientMsPatient {

    @GetMapping("/api/v1/ms-patient/record/find/{id}")
    ResponseEntity<MedicalRecordDto> getMedicalRecord(@PathVariable Long id);
}
