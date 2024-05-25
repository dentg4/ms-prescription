package com.codigo.clinica.msprescription.infraestructure.dao;

import com.codigo.clinica.msprescription.infraestructure.entity.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, Long> {
}
