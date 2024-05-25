package com.codigo.clinica.msprescription.domain.impl;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionRequest;
import com.codigo.clinica.msprescription.domain.ports.in.PrescriptionServiceIn;
import com.codigo.clinica.msprescription.domain.ports.out.PrescriptionServiceOut;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionServiceIn {
    private final PrescriptionServiceOut prescriptionServiceOut;

    @Override
    public PrescriptionDto createPrescriptionIn(PrescriptionRequest request) {
        return prescriptionServiceOut.createPrescriptionOut(request);
    }

    @Override
    public Optional<PrescriptionDto> findByIdIn(Long id) {
        return prescriptionServiceOut.findByIdOut(id);
    }

    @Override
    public List<PrescriptionDto> getAllIn() {
        return prescriptionServiceOut.getAllOut();
    }

    @Override
    public PrescriptionDto updateIn(Long id, PrescriptionRequest request) {
        return prescriptionServiceOut.updateOut(id, request);
    }

    @Override
    public PrescriptionDto deleteIn(Long id) {
        return prescriptionServiceOut.deleteOut(id);
    }
}
