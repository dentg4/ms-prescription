package com.codigo.clinica.msprescription.domain.ports.out;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionRequest;

import java.util.List;
import java.util.Optional;

public interface PrescriptionServiceOut {
    PrescriptionDto createPrescriptionOut(PrescriptionRequest request);
    Optional<PrescriptionDto> findByIdOut(Long id);
    List<PrescriptionDto> getAllOut();
    PrescriptionDto updateOut(Long id, PrescriptionRequest request);
    PrescriptionDto deleteOut(Long id);
}
