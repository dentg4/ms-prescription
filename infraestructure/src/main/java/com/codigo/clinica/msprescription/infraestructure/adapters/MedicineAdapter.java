package com.codigo.clinica.msprescription.infraestructure.adapters;

import com.codigo.clinica.msprescription.domain.aggregates.constants.Constants;
import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicineDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.MedicineRequest;
import com.codigo.clinica.msprescription.domain.ports.out.MedicineServiceOut;
import com.codigo.clinica.msprescription.infraestructure.dao.MedicineRepository;
import com.codigo.clinica.msprescription.infraestructure.entity.Medicine;
import com.codigo.clinica.msprescription.infraestructure.mapper.MedicineMapper;
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
public class MedicineAdapter implements MedicineServiceOut {
    private final MedicineRepository medicineRepository;
    private final RedisService redisService;

    @Value("${ms.redis.expiration_time}")
    private int redisExpirationTime;

    @Override
    public MedicineDto createMedicineOut(MedicineRequest request) {
        Medicine medicines = propertyCreate(request);

        return MedicineMapper.fromEntity(medicineRepository.save(medicines));
    }

    @Override
    public Optional<MedicineDto> findByIdOut(Long id) {
        String redisInfo =  redisService.getFromRedis(Constants.REDIS_GET_MEDICINE+id);
        MedicineDto dto;
        if(redisInfo != null){
            dto = Util.convertFromString(redisInfo, MedicineDto.class);
        }else{
            dto = MedicineMapper.fromEntity(findByIdMedicine(id));

            String dataFromRedis = Util.convertToString(dto);
            redisService.saveInRedis(Constants.REDIS_GET_MEDICINE+id,dataFromRedis,redisExpirationTime);
        }
        return Optional.of(dto);
    }

    @Override
    public List<MedicineDto> getAllOut() {
        List<Medicine> list = medicineRepository.findAll();
        return list.stream().map(MedicineMapper::fromEntity).toList();
    }

    @Override
    public MedicineDto updateOut(Long id, MedicineRequest request) {
        Medicine medicine = findByIdMedicine(id);

        return updateRedis(id, medicineRepository.save(propertyUpdate(medicine, request)));
    }

    @Override
    public MedicineDto deleteOut(Long id) {
        Medicine medicine =  findByIdMedicine(id);

        return updateRedis(id, medicineRepository.save(propertyDelete(medicine)));

    }

    private MedicineDto updateRedis(Long id,Medicine entity){
        MedicineDto medicineDto= MedicineMapper.fromEntity(entity);
        String dataRedis=Util.convertToString(medicineDto);
        redisService.updateInRedis(Constants.REDIS_GET_MEDICINE+id,dataRedis,redisExpirationTime);
        return medicineDto;
    }

    public Medicine propertyCreate(MedicineRequest request){
        Medicine entity= new Medicine();
        getEntity(entity, request);
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setCreatedBy(Constants.USU_ADMIN);
        entity.setCreatedOn(getTimestamp());
        return entity;
    }
    private Medicine propertyUpdate(Medicine entity, MedicineRequest request){
        getEntity(entity,request);
        entity.setUpdatedBy(Constants.USU_ADMIN);
        entity.setUpdatedOn(getTimestamp());
        return entity;
    }
    private Medicine propertyDelete(Medicine entity){
        entity.setStatus(Constants.STATUS_INACTIVE);
        entity.setDeletedBy(Constants.USU_ADMIN);
        entity.setDeletedOn(getTimestamp());
        return entity;
    }
    private void getEntity(Medicine entity,MedicineRequest request) {

        entity.setName(request.getName());
        entity.setDescription(request.getDescription());

    }
    private Timestamp getTimestamp(){
        long currenTIme = System.currentTimeMillis();
        return new Timestamp(currenTIme);
    }
    private Medicine findByIdMedicine(Long id){
        return medicineRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Medicina no encontrada."));
    }
}
