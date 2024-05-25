package com.codigo.clinica.msprescription.infraestructure.adapters;

import com.codigo.clinica.msprescription.domain.aggregates.constants.Constants;
import com.codigo.clinica.msprescription.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicalRecordDto;
import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionRequest;
import com.codigo.clinica.msprescription.domain.ports.out.PrescriptionServiceOut;
import com.codigo.clinica.msprescription.infraestructure.client.ClientMsPatient;
import com.codigo.clinica.msprescription.infraestructure.client.ClientMsStaff;
import com.codigo.clinica.msprescription.infraestructure.dao.PrescriptionRepository;
import com.codigo.clinica.msprescription.infraestructure.entity.Prescription;
import com.codigo.clinica.msprescription.infraestructure.mapper.PrescriptionMapper;
import com.codigo.clinica.msprescription.infraestructure.redis.RedisService;
import com.codigo.clinica.msprescription.infraestructure.util.Util;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionAdapter implements PrescriptionServiceOut {
    private final PrescriptionRepository prescriptionRepository;
    private final ClientMsStaff clientMsStaff;
    private final ClientMsPatient clientMsPatient;
    private final RedisService redisService;

    @Value("${ms.redis.expiration_time}")
    private int redisExpirationTime;

    @Override
    public PrescriptionDto createPrescriptionOut(PrescriptionRequest request) {
        MedicalRecordDto medicalRecordDto = validateResponse(clientMsPatient.getMedicalRecord(request.getMedicalRecordId()));
        DoctorDto doctorDto = validateResponse(clientMsStaff.getDoctorById(request.getDoctorId()));

        Prescription prescription = propertyCreate(request);

        return PrescriptionMapper.fromEntity(prescriptionRepository.save(prescription), medicalRecordDto, doctorDto);
    }

    @Override
    public Optional<PrescriptionDto> findByIdOut(Long id) {
        String redisInfo =  redisService.getFromRedis(Constants.REDIS_GET_PRESCRIPTION+id);
        PrescriptionDto dto;
        if(redisInfo != null){
            dto = Util.convertFromString(redisInfo, PrescriptionDto.class);
        }else{
            Prescription prescription=findByIdPrescription(id);
            MedicalRecordDto medicalRecordDto = validateResponse(clientMsPatient.getMedicalRecord(prescription.getMedicalRecordId()));
            DoctorDto doctorDto = validateResponse(clientMsStaff.getDoctorById(prescription.getDoctorId()));
            dto = PrescriptionMapper.fromEntity(prescription, medicalRecordDto,doctorDto);

            String dataFromRedis = Util.convertToString(dto);
            redisService.saveInRedis(Constants.REDIS_GET_PRESCRIPTION+id,dataFromRedis,redisExpirationTime);
        }
        return Optional.of(dto);
    }

    @Override
    public List<PrescriptionDto> getAllOut() {
        List<Prescription> list = prescriptionRepository.findAll();
        return list.stream().map(PrescriptionMapper::fromEntity).toList();
    }

    @Override
    public PrescriptionDto updateOut(Long id, PrescriptionRequest request) {
        Prescription prescription = findByIdPrescription(id);
        MedicalRecordDto medicalRecordDto = validateResponse(clientMsPatient.getMedicalRecord(request.getMedicalRecordId()));
        DoctorDto doctorDto = validateResponse(clientMsStaff.getDoctorById(request.getDoctorId()));

        PrescriptionDto prescriptionDto=PrescriptionMapper.fromEntity(
                prescriptionRepository.save(propertyUpdate(prescription, request)),
                medicalRecordDto, doctorDto);

        return updateRedis(id, prescriptionDto);
    }

    @Override
    public PrescriptionDto deleteOut(Long id) {
        Prescription prescription =  findByIdPrescription(id);
        PrescriptionDto prescriptionDto=PrescriptionMapper.fromEntity(prescriptionRepository.save(propertyDelete(prescription)));
        return updateRedis(id, prescriptionDto);
    }

    private PrescriptionDto updateRedis(Long id,PrescriptionDto entityDto){
        String dataRedis=Util.convertToString(entityDto);
        redisService.updateInRedis(Constants.REDIS_GET_PRESCRIPTION+id,dataRedis,redisExpirationTime);
        return entityDto;
    }

    public Prescription propertyCreate(PrescriptionRequest request){
        Prescription entity= new Prescription();

        getEntity(entity, request);
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setCreatedBy(Constants.USU_ADMIN);
        entity.setCreatedOn(getTimestamp());
        return entity;
    }
    public Prescription propertyUpdate(Prescription entity, PrescriptionRequest request){
        getEntity(entity,request);
        entity.setUpdatedBy(Constants.USU_ADMIN);
        entity.setUpdatedOn(getTimestamp());
        return entity;
    }
    private Prescription propertyDelete(Prescription entity){
        entity.setStatus(Constants.STATUS_INACTIVE);
        entity.setDeletedBy(Constants.USU_ADMIN);
        entity.setDeletedOn(getTimestamp());
        return entity;
    }
    private void getEntity(Prescription entity,PrescriptionRequest request) {

        entity.setDate(request.getDate());
        entity.setObservations(request.getObservations());
        entity.setMedicalRecordId(request.getMedicalRecordId());
        entity.setDoctorId(request.getDoctorId());

    }

    private Timestamp getTimestamp(){
        long currenTIme = System.currentTimeMillis();
        return new Timestamp(currenTIme);
    }
    private Prescription findByIdPrescription(Long id){
        return prescriptionRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Prescripción no encontrada."));
    }

    private <T> T validateResponse(ResponseEntity<T> reponse){
        if(reponse.getStatusCode() == HttpStatus.OK){
            return reponse.getBody();
        }else{
            throw new RuntimeException("Error al obtener registro.");
        }
    }
}
