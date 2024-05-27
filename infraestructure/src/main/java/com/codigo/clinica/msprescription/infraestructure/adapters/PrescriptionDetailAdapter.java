package com.codigo.clinica.msprescription.infraestructure.adapters;

import com.codigo.clinica.msprescription.domain.aggregates.constants.Constants;
import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDetailDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.DetailMedicinePair;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailListRequest;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailRequest;
import com.codigo.clinica.msprescription.domain.ports.out.PrescriptionDetailServiceOut;
import com.codigo.clinica.msprescription.infraestructure.dao.MedicineRepository;
import com.codigo.clinica.msprescription.infraestructure.dao.PrescriptionDetailRepository;
import com.codigo.clinica.msprescription.infraestructure.dao.PrescriptionRepository;
import com.codigo.clinica.msprescription.infraestructure.entity.Medicine;
import com.codigo.clinica.msprescription.infraestructure.entity.Prescription;
import com.codigo.clinica.msprescription.infraestructure.entity.PrescriptionDetail;
import com.codigo.clinica.msprescription.infraestructure.mapper.PrescriptionDetailMapper;
import com.codigo.clinica.msprescription.infraestructure.redis.RedisService;
import com.codigo.clinica.msprescription.infraestructure.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionDetailAdapter implements PrescriptionDetailServiceOut {
    private final PrescriptionDetailRepository prescriptionDetailRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineRepository medicineRepository;
    private final RedisService redisService;

    @Value("${ms.redis.expiration_time}")
    private int redisExpirationTime;

    @Override
    public PrescriptionDetailDto createDetailOut(PrescriptionDetailRequest request) {
        PrescriptionDetail prescription = propertyCreate(request);

        return PrescriptionDetailMapper.fromEntity(prescriptionDetailRepository.save(prescription));
    }

    @Override
    public List<PrescriptionDetailDto> createListDetailOut(PrescriptionDetailListRequest request) {
        if(request.getList().isEmpty()) throw new RuntimeException("No hay elemento a guardar.");

        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("Prescripción no encontrada."));

        List<PrescriptionDetail> prescriptionDetails= request.getList().stream().map(detailRequest->{
            Medicine medicine = medicineRepository.findById(detailRequest.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicina no encontrada."));
            return propertyCreateList(prescription, medicine, detailRequest);
        }).toList();

        return prescriptionDetailRepository.saveAll(prescriptionDetails)
                .stream().map(PrescriptionDetailMapper::fromEntity).toList();
    }
    private PrescriptionDetail propertyCreateList(Prescription prescription, Medicine medicine, DetailMedicinePair item){
        PrescriptionDetail detail = new PrescriptionDetail();
        detail.setPrescription(prescription);
        detail.setMedicine(medicine);
        detail.setAmount(item.getAmount());
        detail.setIndications(item.getIndications());
        detail.setStatus(Constants.STATUS_ACTIVE);
        detail.setCreatedBy(Constants.USU_ADMIN);
        detail.setCreatedOn(getTimestamp());
        return detail;
    }

    @Override
    public Optional<PrescriptionDetailDto> findByIdOut(Long id) {
        String redisInfo =  redisService.getFromRedis(Constants.REDIS_GET_PRESCRIPTION_DETAIL+id);
        PrescriptionDetailDto dto;
        if(redisInfo != null){
            dto = Util.convertFromString(redisInfo, PrescriptionDetailDto.class);
        }else{
            dto = PrescriptionDetailMapper.fromEntity(findByIdPrescription(id));

            String dataFromRedis = Util.convertToString(dto);
            redisService.saveInRedis(Constants.REDIS_GET_PRESCRIPTION_DETAIL+id,dataFromRedis,redisExpirationTime);
        }
        return Optional.of(dto);
    }

    @Override
    public List<PrescriptionDetailDto> getAllOut() {
        List<PrescriptionDetail> list = prescriptionDetailRepository.findAll();
        return list.stream().map(PrescriptionDetailMapper::fromEntity).toList();
    }

    @Override
    public PrescriptionDetailDto updateOut(Long id, PrescriptionDetailRequest request) {
        PrescriptionDetail prescription = findByIdPrescription(id);

        return updateRedis(id, prescriptionDetailRepository.save(propertyUpdate(prescription, request)));
    }

    @Override
    public PrescriptionDetailDto deleteOut(Long id) {
        PrescriptionDetail prescription =  findByIdPrescription(id);

        return updateRedis(id, prescriptionDetailRepository.save(propertyDelete(prescription)));

    }

    private PrescriptionDetailDto updateRedis(Long id,PrescriptionDetail entity){
        PrescriptionDetailDto prescriptionDto= PrescriptionDetailMapper.fromEntity(entity);
        String dataRedis=Util.convertToString(prescriptionDto);
        redisService.updateInRedis(Constants.REDIS_GET_PRESCRIPTION_DETAIL+id,dataRedis,redisExpirationTime);
        return prescriptionDto;
    }

    public PrescriptionDetail propertyCreate(PrescriptionDetailRequest request){
        PrescriptionDetail entity= new PrescriptionDetail();
        getEntity(entity, request);
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setCreatedBy(Constants.USU_ADMIN);
        entity.setCreatedOn(getTimestamp());
        return entity;
    }
    private PrescriptionDetail propertyUpdate(PrescriptionDetail entity, PrescriptionDetailRequest request){
        getEntity(entity,request);
        entity.setUpdatedBy(Constants.USU_ADMIN);
        entity.setUpdatedOn(getTimestamp());
        return entity;
    }
    public PrescriptionDetail propertyDelete(PrescriptionDetail entity){
        entity.setStatus(Constants.STATUS_INACTIVE);
        entity.setDeletedBy(Constants.USU_ADMIN);
        entity.setDeletedOn(getTimestamp());
        return entity;
    }
    private void getEntity(PrescriptionDetail entity,PrescriptionDetailRequest request) {

        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("Prescripción no encontrada."));
        Medicine medicine =  medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicina no encontrada."));


        entity.setAmount(request.getAmount());
        entity.setIndications(request.getIndications());
        entity.setPrescription(prescription);
        entity.setMedicine(medicine);
    }
    private Timestamp getTimestamp(){
        long currenTIme = System.currentTimeMillis();
        return new Timestamp(currenTIme);
    }
    private PrescriptionDetail findByIdPrescription(Long id){
        return prescriptionDetailRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Prescripción no encontrada."));
    }
}
