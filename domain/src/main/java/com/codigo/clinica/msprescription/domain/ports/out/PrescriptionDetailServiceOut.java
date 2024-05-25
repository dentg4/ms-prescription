package com.codigo.clinica.msprescription.domain.ports.out;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDetailDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailListRequest;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailRequest;

import java.util.List;
import java.util.Optional;

public interface PrescriptionDetailServiceOut {
    PrescriptionDetailDto createDetailOut(PrescriptionDetailRequest request);
    List<PrescriptionDetailDto> createListDetailOut(PrescriptionDetailListRequest requests);
    Optional<PrescriptionDetailDto> findByIdOut(Long id);
    List<PrescriptionDetailDto> getAllOut();
    PrescriptionDetailDto updateOut(Long id, PrescriptionDetailRequest request);
    PrescriptionDetailDto deleteOut(Long id);
}
