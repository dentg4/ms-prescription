package com.codigo.clinica.msprescription.infraestructure.adapters;

import com.codigo.clinica.msprescription.domain.aggregates.constants.Constants;
import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicineDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.MedicineRequest;
import com.codigo.clinica.msprescription.infraestructure.dao.MedicineRepository;
import com.codigo.clinica.msprescription.infraestructure.entity.Medicine;
import com.codigo.clinica.msprescription.infraestructure.mapper.MedicineMapper;
import com.codigo.clinica.msprescription.infraestructure.redis.RedisService;
import com.codigo.clinica.msprescription.infraestructure.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MedicineAdapterTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private MedicineAdapter medicineAdapter;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMedicineOut() {
        Medicine medicine = new Medicine();
        medicine.setName("Ibuprofeno");
        medicine.setDescription("Dolores etc.");
        MedicineRequest request= MedicineRequest.builder()
                .name(medicine.getName()).description(medicine.getDescription()).build();

        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicineAdapter.propertyCreate(request));

        MedicineDto medicineDto=medicineAdapter.createMedicineOut(request);

        assertNotNull(medicineDto);
        assertEquals(medicine.getName(),medicineDto.getName());
        assertEquals(medicine.getDescription(),medicineDto.getDescription());

        assertNotNull(medicineDto.getStatus());
        assertNotNull(medicineDto.getCreatedBy());
        assertNotNull(medicineDto.getCreateOn());

        verify(medicineRepository).save(any(Medicine.class));

    }

    @Test
    void findByIdOutForRedis() {
        Long id= 1L;
        Medicine medicine = new Medicine();
        medicine.setName("Ibuprofeno");
        medicine.setDescription("Dolores etc.");
        MedicineDto medicineDto= MedicineMapper.fromEntity(medicine);

        when(redisService.getFromRedis(anyString())).thenReturn(Util.convertToString(medicineDto));

        Optional<MedicineDto> response= medicineAdapter.findByIdOut(id);

        assertTrue(response.isPresent());
        assertEquals(medicineDto.getName(),response.get().getName());
        assertEquals(medicineDto.getDescription(),response.get().getDescription());

        verify(redisService).getFromRedis(anyString());
    }
    @Test
    void findByIdOutForBD() {
        Long id= 1L;
        Medicine medicine = new Medicine();
        medicine.setName("Ibuprofeno");
        medicine.setDescription("Dolores etc.");
        MedicineDto medicineDto= MedicineMapper.fromEntity(medicine);

        when(redisService.getFromRedis(anyString())).thenReturn(null);
        when(medicineRepository.findById(anyLong())).thenReturn(Optional.of(medicine));

        Optional<MedicineDto> response= medicineAdapter.findByIdOut(id);
        assertNotNull(response);
        assertTrue(response.isPresent());
        assertEquals(medicineDto.getName(),response.get().getName());
        assertEquals(medicineDto.getDescription(),response.get().getDescription());

        verify(redisService).getFromRedis(anyString());
        verify(medicineRepository).findById(anyLong());
    }

    @Test
    void getAllOutNoList(){
        when(medicineRepository.findAll()).thenReturn(Collections.emptyList());
        List<MedicineDto> response = medicineAdapter.getAllOut();
        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(medicineRepository).findAll();
    }

    @Test
    void getAllOut() {
        Medicine medicine = new Medicine();
        medicine.setName("Ibuprofeno");
        medicine.setDescription("Dolores etc.");
        Medicine medicine2 = new Medicine();
        medicine.setName("Ibuprofeno2");
        medicine.setDescription("Dolores etc.2");
        List<Medicine> medicineList= new ArrayList<>();
        medicineList.add(medicine);
        medicineList.add(medicine2);

        when(medicineRepository.findAll()).thenReturn(medicineList);
        List<MedicineDto> medicineDtos=medicineList.stream().map(MedicineMapper::fromEntity).toList();
        List<MedicineDto> response= medicineAdapter.getAllOut();
        assertNotNull(response);
        assertEquals(medicineDtos.size(),response.size());
    }

    @Test
    void updateOut() {
        Long id= 1L;
        Medicine medicine = new Medicine();
        medicine.setName("Ibuprofeno");
        medicine.setDescription("Dolores etc.");

        MedicineRequest request= MedicineRequest.builder()
                        .name("nuevo nombre").description("nueva description").build();

        when(medicineRepository.findById(anyLong())).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);

        MedicineDto medicineDto=medicineAdapter.updateOut(id, request);
        assertNotNull(medicineDto);
        assertEquals(request.getName(),medicineDto.getName());
        assertEquals(request.getDescription(),medicineDto.getDescription());
    }

    @Test
    void deleteOut() {
        Long id= 1L;
        Medicine medicine = new Medicine();
        medicine.setName("Ibuprofeno");
        medicine.setDescription("Dolores etc.");
        medicine.setStatus(Constants.STATUS_INACTIVE);

        when(medicineRepository.findById(anyLong())).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);

        MedicineDto medicineDto=medicineAdapter.deleteOut(id);

        assertNotNull(medicineDto);
        assertEquals(medicineDto.getName(),medicine.getName());
        assertEquals(medicineDto.getDescription(),medicine.getDescription());
        assertEquals(medicineDto.getStatus(),Constants.STATUS_INACTIVE);
    }
}