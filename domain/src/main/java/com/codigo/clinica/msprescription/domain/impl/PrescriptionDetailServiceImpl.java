package com.codigo.clinica.msprescription.domain.impl;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDetailDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailListRequest;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailRequest;
import com.codigo.clinica.msprescription.domain.ports.in.PrescriptionDetailServiceIn;
import com.codigo.clinica.msprescription.domain.ports.out.PrescriptionDetailServiceOut;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PrescriptionDetailServiceImpl implements PrescriptionDetailServiceIn {
    private final PrescriptionDetailServiceOut prescriptionDetailServiceOut;

    @Override
    public PrescriptionDetailDto createDetailIn(PrescriptionDetailRequest request) {
        return prescriptionDetailServiceOut.createDetailOut(request);
    }

    @Override
    public List<PrescriptionDetailDto> createListDetailIn(PrescriptionDetailListRequest requests) {
        return prescriptionDetailServiceOut.createListDetailOut(requests);
    }

    @Override
    public Optional<PrescriptionDetailDto> findByIdIn(Long id) {
        return prescriptionDetailServiceOut.findByIdOut(id);
    }

    @Override
    public List<PrescriptionDetailDto> getAllIn() {
        return prescriptionDetailServiceOut.getAllOut();
    }

    @Override
    public PrescriptionDetailDto updateIn(Long id, PrescriptionDetailRequest request) {
        return prescriptionDetailServiceOut.updateOut(id, request);
    }

    @Override
    public PrescriptionDetailDto deleteIn(Long id) {
        return prescriptionDetailServiceOut.deleteOut(id);
    }
}
