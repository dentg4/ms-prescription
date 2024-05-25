package com.codigo.clinica.msprescription.domain.impl;

import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicineDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.MedicineRequest;
import com.codigo.clinica.msprescription.domain.ports.in.MedicineServiceIn;
import com.codigo.clinica.msprescription.domain.ports.out.MedicineServiceOut;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MedicineServiceImpl implements MedicineServiceIn {

    private final MedicineServiceOut medicineServiceOut;

    @Override
    public MedicineDto createMedicineIn(MedicineRequest request) {
        return medicineServiceOut.createMedicineOut(request);
    }

    @Override
    public Optional<MedicineDto> findByIdIn(Long id) {
        return medicineServiceOut.findByIdOut(id);
    }

    @Override
    public List<MedicineDto> getAllIn() {
        return medicineServiceOut.getAllOut();
    }

    @Override
    public MedicineDto updateIn(Long id, MedicineRequest request) {
        return medicineServiceOut.updateOut(id, request);
    }

    @Override
    public MedicineDto deleteIn(Long id) {
        return medicineServiceOut.deleteOut(id);
    }
}
