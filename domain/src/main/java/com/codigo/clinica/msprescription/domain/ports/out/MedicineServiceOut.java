package com.codigo.clinica.msprescription.domain.ports.out;

import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicineDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.MedicineRequest;

import java.util.List;
import java.util.Optional;

public interface MedicineServiceOut {
    MedicineDto createMedicineOut(MedicineRequest request);
    Optional<MedicineDto> findByIdOut(Long id);
    List<MedicineDto> getAllOut();
    MedicineDto updateOut(Long id, MedicineRequest request);
    MedicineDto deleteOut(Long id);
}
