package com.codigo.clinica.msprescription.infraestructure.dao;

import com.codigo.clinica.msprescription.infraestructure.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
