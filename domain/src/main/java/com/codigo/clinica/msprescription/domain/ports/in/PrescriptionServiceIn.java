package com.codigo.clinica.msprescription.domain.ports.in;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionRequest;

import java.util.List;
import java.util.Optional;

public interface PrescriptionServiceIn {
    PrescriptionDto createPrescriptionIn(PrescriptionRequest request);
    Optional<PrescriptionDto> findByIdIn(Long id);
    List<PrescriptionDto> getAllIn();
    PrescriptionDto updateIn(Long id, PrescriptionRequest request);
    PrescriptionDto deleteIn(Long id);
}
