package com.codigo.clinica.msprescription.infraestructure.adapters;

import com.codigo.clinica.msprescription.domain.aggregates.constants.Constants;
import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDetailDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.DetailMedicinePair;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailListRequest;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailRequest;
import com.codigo.clinica.msprescription.infraestructure.dao.MedicineRepository;
import com.codigo.clinica.msprescription.infraestructure.dao.PrescriptionDetailRepository;
import com.codigo.clinica.msprescription.infraestructure.dao.PrescriptionRepository;
import com.codigo.clinica.msprescription.infraestructure.entity.Medicine;
import com.codigo.clinica.msprescription.infraestructure.entity.Prescription;
import com.codigo.clinica.msprescription.infraestructure.entity.PrescriptionDetail;
import com.codigo.clinica.msprescription.infraestructure.redis.RedisService;
import com.codigo.clinica.msprescription.infraestructure.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class PrescriptionDetailAdapterTest {
    @Mock
    private PrescriptionDetailRepository prescriptionDetailRepository;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private PrescriptionDetailAdapter prescriptionDetailAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDetailOut() {
        PrescriptionDetailRequest request= PrescriptionDetailRequest.builder()
                .amount(2).prescriptionId(2l).medicineId(2l).build();

        Prescription prescription=new Prescription();
        prescription.setId(request.getPrescriptionId());

        Medicine medicine=new Medicine();
        medicine.setId(request.getMedicineId());

        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setAmount(request.getAmount());
        prescriptionDetail.setMedicine(medicine);
        prescriptionDetail.setPrescription(prescription);
        prescriptionDetail.setStatus(Constants.STATUS_ACTIVE);

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        when(medicineRepository.findById(anyLong())).thenReturn(Optional.of(medicine));
        when(prescriptionDetailRepository.save(any(PrescriptionDetail.class))).thenReturn(prescriptionDetail);

        PrescriptionDetailDto response= prescriptionDetailAdapter.createDetailOut(request);
        assertNotNull(response);
        assertEquals(medicine.getId(), response.getMedicine().getId());
        assertEquals(prescriptionDetail.getAmount(),response.getAmount());
        assertEquals(prescriptionDetail.getMedicine().getId(),response.getMedicine().getId());
        assertEquals(prescription.getId(), response.getPrescriptionId());
        assertEquals(prescriptionDetail.getStatus(), response.getStatus());
    }

    @Test
    void createListDetailOut() {
        DetailMedicinePair detailMedicinePair=DetailMedicinePair.builder()
                .amount(2).medicineId(1l).indications("indica").build();

        PrescriptionDetailListRequest listRequest = PrescriptionDetailListRequest.builder()
                .prescriptionId(1L)
                .list(List.of(detailMedicinePair))
                .build();

        Prescription prescription = new Prescription();
        prescription.setId(listRequest.getPrescriptionId());

        Medicine medicine = new Medicine();
        medicine.setId(detailMedicinePair.getMedicineId());

        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setAmount(detailMedicinePair.getAmount());
        prescriptionDetail.setMedicine(medicine);
        prescriptionDetail.setPrescription(prescription);

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        when(medicineRepository.findById(anyLong())).thenReturn(Optional.of(medicine));
        when(prescriptionDetailRepository.saveAll(anyList())).thenReturn(List.of(prescriptionDetail));

        List<PrescriptionDetailDto> response = prescriptionDetailAdapter.createListDetailOut(listRequest);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(prescriptionDetail.getAmount(), response.get(0).getAmount());
        assertEquals(prescriptionDetail.getMedicine().getId(), response.get(0).getMedicine().getId());
        assertEquals(prescriptionDetail.getPrescription().getId(), response.get(0).getPrescriptionId());

        verify(prescriptionRepository).findById(anyLong());
        verify(medicineRepository).findById(anyLong());
        verify(prescriptionDetailRepository).saveAll(anyList());
    }

    @Test
    void findByIdOutForRedis() {
        Long id = 1L;
        PrescriptionDetailDto prescriptionDetailDto = new PrescriptionDetailDto();
        prescriptionDetailDto.setAmount(2);
        prescriptionDetailDto.setMedicineId(1L);
        prescriptionDetailDto.setPrescriptionId(1L);

        when(redisService.getFromRedis(anyString())).thenReturn(Util.convertToString(prescriptionDetailDto));

        Optional<PrescriptionDetailDto> response = prescriptionDetailAdapter.findByIdOut(id);

        assertNotNull(response);
        assertEquals(prescriptionDetailDto.getAmount(), response.get().getAmount());
        assertEquals(prescriptionDetailDto.getMedicineId(), response.get().getMedicineId());
        assertEquals(prescriptionDetailDto.getPrescriptionId(), response.get().getPrescriptionId());
    }

    @Test
    void findByIdOutForBD() {
        Long id = 1L;
        Prescription prescription = new Prescription();
        prescription.setId(id);

        Medicine medicine = new Medicine();
        medicine.setId(1L);

        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setId(id);
        prescriptionDetail.setAmount(2);
        prescriptionDetail.setMedicine(medicine);
        prescriptionDetail.setPrescription(prescription);

        when(redisService.getFromRedis(anyString())).thenReturn(null);
        when(prescriptionDetailRepository.findById(anyLong())).thenReturn(Optional.of(prescriptionDetail));

        Optional<PrescriptionDetailDto> response = prescriptionDetailAdapter.findByIdOut(id);

        assertNotNull(response);
        assertTrue(response.isPresent());
        assertEquals(prescriptionDetail.getAmount(), response.get().getAmount());
        assertEquals(prescriptionDetail.getMedicine().getId(), response.get().getMedicine().getId());
        assertEquals(prescriptionDetail.getPrescription().getId(), response.get().getPrescriptionId());

        //verifica si el mock se ha llamado
        verify(redisService).getFromRedis(anyString());
        verify(redisService).saveInRedis(anyString(), anyString(), anyInt());
    }

    @Test
    void getAllOut() {
        Prescription prescription = new Prescription();
        prescription.setId(1L);

        Medicine medicine = new Medicine();
        medicine.setId(1L);

        PrescriptionDetail prescriptionDetail1 = new PrescriptionDetail();
        prescriptionDetail1.setId(1L);
        prescriptionDetail1.setAmount(2);
        prescriptionDetail1.setMedicine(medicine);
        prescriptionDetail1.setPrescription(prescription);

        PrescriptionDetail prescriptionDetail2 = new PrescriptionDetail();
        prescriptionDetail2.setId(2L);
        prescriptionDetail2.setAmount(3);
        prescriptionDetail2.setMedicine(medicine);
        prescriptionDetail2.setPrescription(prescription);

        List<PrescriptionDetail> prescriptionDetails = List.of(prescriptionDetail1, prescriptionDetail2);

        when(prescriptionDetailRepository.findAll()).thenReturn(prescriptionDetails);

        List<PrescriptionDetailDto> response = prescriptionDetailAdapter.getAllOut();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(prescriptionDetail1.getId(), response.get(0).getId());
        assertEquals(prescriptionDetail1.getAmount(), response.get(0).getAmount());
        assertEquals(prescriptionDetail2.getId(), response.get(1).getId());
        assertEquals(prescriptionDetail2.getAmount(), response.get(1).getAmount());

        verify(prescriptionDetailRepository).findAll();
    }

    @Test
    void updateOut() {
        Long id=1l;


        PrescriptionDetailRequest request= PrescriptionDetailRequest
                .builder().amount(5).indications("nueva indicacion")
                .prescriptionId(5l).medicineId(5l).build();

        Prescription prescription = new Prescription();
        prescription.setId(request.getPrescriptionId());

        Medicine medicine = new Medicine();
        medicine.setId(request.getMedicineId());

        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setAmount(1);
        prescriptionDetail.setIndications("indicacion");
        prescriptionDetail.setPrescription(new Prescription());
        prescriptionDetail.setMedicine(new Medicine());

        when(prescriptionDetailRepository.findById(id)).thenReturn(Optional.of(prescriptionDetail));
        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        when(medicineRepository.findById(anyLong())).thenReturn(Optional.of(medicine));
        when(prescriptionDetailRepository.save(any(PrescriptionDetail.class))).thenReturn(prescriptionDetail);

        PrescriptionDetailDto dto = prescriptionDetailAdapter.updateOut(id, request);
        assertNotNull(dto);
        assertEquals(request.getAmount(),dto.getAmount());
        assertEquals(request.getIndications(),dto.getIndications());
        assertEquals(request.getPrescriptionId(),dto.getPrescriptionId());
        assertEquals(request.getMedicineId(),dto.getMedicine().getId());

    }

    @Test
    void deleteOut() {
        Long id=1l;
        PrescriptionDetail detail= new PrescriptionDetail();
        detail.setId(id);
        detail.setAmount(2);
        detail.setStatus(Constants.STATUS_ACTIVE);
        when(prescriptionDetailRepository.findById(anyLong())).thenReturn(Optional.of(detail));
        when(prescriptionDetailRepository.save(any(PrescriptionDetail.class))).thenReturn(prescriptionDetailAdapter.propertyDelete(detail));

        PrescriptionDetailDto dto=prescriptionDetailAdapter.deleteOut(id);
        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(dto.getStatus(),Constants.STATUS_INACTIVE);
    }
}