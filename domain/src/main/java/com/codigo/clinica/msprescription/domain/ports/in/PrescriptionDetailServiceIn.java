package com.codigo.clinica.msprescription.domain.ports.in;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDetailDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailListRequest;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailRequest;

import java.util.List;
import java.util.Optional;

public interface PrescriptionDetailServiceIn {
    PrescriptionDetailDto createDetailIn(PrescriptionDetailRequest request);
    List<PrescriptionDetailDto> createListDetailIn(PrescriptionDetailListRequest requests);
    Optional<PrescriptionDetailDto> findByIdIn(Long id);
    List<PrescriptionDetailDto> getAllIn();
    PrescriptionDetailDto updateIn(Long id, PrescriptionDetailRequest request);
    PrescriptionDetailDto deleteIn(Long id);
}
