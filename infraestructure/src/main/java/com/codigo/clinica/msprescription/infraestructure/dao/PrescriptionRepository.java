package com.codigo.clinica.msprescription.infraestructure.dao;

import com.codigo.clinica.msprescription.infraestructure.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
