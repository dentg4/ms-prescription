package com.codigo.clinica.msprescription.domain.ports.in;

import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicineDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.MedicineRequest;

import java.util.List;
import java.util.Optional;

public interface MedicineServiceIn {
    MedicineDto createMedicineIn(MedicineRequest request);
    Optional<MedicineDto> findByIdIn(Long id);
    List<MedicineDto> getAllIn();
    MedicineDto updateIn(Long id, MedicineRequest request);
    MedicineDto deleteIn(Long id);
}
