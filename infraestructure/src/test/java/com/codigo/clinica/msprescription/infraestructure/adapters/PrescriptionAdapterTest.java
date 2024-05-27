package com.codigo.clinica.msprescription.infraestructure.adapters;

import com.codigo.clinica.msprescription.domain.aggregates.constants.Constants;
import com.codigo.clinica.msprescription.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicalRecordDto;
import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionRequest;
import com.codigo.clinica.msprescription.infraestructure.client.ClientMsPatient;
import com.codigo.clinica.msprescription.infraestructure.client.ClientMsStaff;
import com.codigo.clinica.msprescription.infraestructure.dao.PrescriptionRepository;
import com.codigo.clinica.msprescription.infraestructure.entity.Prescription;
import com.codigo.clinica.msprescription.infraestructure.mapper.PrescriptionMapper;
import com.codigo.clinica.msprescription.infraestructure.redis.RedisService;
import com.codigo.clinica.msprescription.infraestructure.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class PrescriptionAdapterTest {
    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private ClientMsStaff clientMsStaff;

    @Mock
    private ClientMsPatient clientMsPatient;

    @Mock
    private RedisService redisService;
    
    @InjectMocks
    private PrescriptionAdapter prescriptionAdapter;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPrescriptionOut() {
        Prescription prescription = new Prescription();
        prescription.setMedicalRecordId(1l);
        prescription.setDoctorId(2l);
        prescription.setObservations("Observaciones");
        String dateString = "2020-01-01";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            prescription.setDate(formatter.parse(dateString));
        }catch (ParseException e){
            e.printStackTrace();
            fail("error al convertit la fecha");
        }

        PrescriptionRequest request= PrescriptionRequest.builder()
                .date(prescription.getDate()).doctorId(prescription.getDoctorId())
                .medicalRecordId(prescription.getMedicalRecordId()).build();
        MedicalRecordDto medicalRecordDto= MedicalRecordDto.builder()
                        .id(prescription.getMedicalRecordId()).diagnos("ss").build();
        DoctorDto doctorDto =  DoctorDto.builder()
                        .id(prescription.getDoctorId()).name("s").build();

        when(clientMsPatient.getMedicalRecord(anyLong())).thenReturn(ResponseEntity.ok(medicalRecordDto));
        when(clientMsStaff.getDoctorById(anyLong())).thenReturn(ResponseEntity.ok(doctorDto));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescriptionAdapter.propertyCreate(request));

        PrescriptionDto prescriptionDto=prescriptionAdapter.createPrescriptionOut(request);
        assertNotNull(prescriptionDto);
        assertEquals(prescription.getDate(),prescriptionDto.getDate());
        assertNotNull(prescriptionDto.getCreatedBy());
        assertNotNull(prescriptionDto.getCreateOn());
        assertEquals(doctorDto, prescriptionDto.getDoctor());
        assertEquals(medicalRecordDto, prescriptionDto.getMedicalRecord());
    }

    @Test
    void findByIdOutForRedis() {
        Long id= 1L;
        Prescription prescription = new Prescription();
        prescription.setObservations("observaciones");
        PrescriptionDto prescriptionDto= PrescriptionMapper.fromEntity(prescription);

        when(redisService.getFromRedis(anyString())).thenReturn(Util.convertToString(prescriptionDto));

        Optional<PrescriptionDto> response= prescriptionAdapter.findByIdOut(id);

        assertNotNull(response);
        assertEquals(prescriptionDto.getObservations(),response.get().getObservations());

    }
    @Test
    void findByIdOutForBD() {
        Long id= 1L;
        Prescription prescription = new Prescription();
        prescription.setObservations("observaciones");
        prescription.setMedicalRecordId(1l);
        prescription.setDoctorId(2l);

        MedicalRecordDto medicalRecordDto= MedicalRecordDto.builder().id(prescription.getMedicalRecordId()).build();
        DoctorDto doctorDto= DoctorDto.builder().id(prescription.getDoctorId()).build();


        when(redisService.getFromRedis(anyString())).thenReturn(null);
        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        when(clientMsStaff.getDoctorById(anyLong())).thenReturn(ResponseEntity.ok(doctorDto));
        when(clientMsPatient.getMedicalRecord(anyLong())).thenReturn(ResponseEntity.ok(medicalRecordDto));

        Optional<PrescriptionDto> response= prescriptionAdapter.findByIdOut(id);
        assertNotNull(response);
        assertEquals(medicalRecordDto,response.get().getMedicalRecord());
        assertEquals(doctorDto,response.get().getDoctor());
    }

    @Test
    void getAllOut() {
        Prescription prescription = new Prescription();
        prescription.setObservations("observaciones");

        Prescription prescription1 = new Prescription();
        prescription1.setObservations("observaciones2");
        List<Prescription> prescriptions= new ArrayList<>();
        prescriptions.add(prescription);
        prescriptions.add(prescription1);
        when(prescriptionRepository.findAll()).thenReturn(prescriptions);
        List<PrescriptionDto> prescriptionDtos= prescriptions.stream().map(PrescriptionMapper::fromEntity).toList();

        List<PrescriptionDto> response= prescriptionAdapter.getAllOut();
        assertNotNull(response);
        assertEquals(prescriptionDtos.size(),response.size());

    }

    @Test
    void updateOut() {
        Long id=1l;
        Prescription prescription = new Prescription();
        prescription.setObservations("observaciones");
        prescription.setMedicalRecordId(1l);
        prescription.setDoctorId(1l);

        PrescriptionRequest request= PrescriptionRequest.builder()
                .observations("nueva observacion").medicalRecordId(2l).doctorId(2l).build();

        MedicalRecordDto medicalRecordDto= MedicalRecordDto.builder().id(request.getMedicalRecordId()).build();
        DoctorDto doctorDto= DoctorDto.builder().id(request.getDoctorId()).build();

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        when(clientMsStaff.getDoctorById(anyLong())).thenReturn(ResponseEntity.ok(doctorDto));
        when(clientMsPatient.getMedicalRecord(anyLong())).thenReturn(ResponseEntity.ok(medicalRecordDto));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescriptionAdapter.propertyUpdate(prescription, request));

        PrescriptionDto response= prescriptionAdapter.updateOut(id, request);
        assertNotNull(response);
        assertEquals(request.getMedicalRecordId(), response.getMedicalRecord().getId());
        assertEquals(request.getDoctorId(), response.getDoctor().getId());
        assertEquals(request.getObservations(), response.getObservations());
        assertEquals(medicalRecordDto, response.getMedicalRecord());
        assertEquals(doctorDto, response.getDoctor());


    }

    @Test
    void deleteOut() {
        Long id= 1L;
        Prescription prescription = new Prescription();
        prescription.setId(id);
        prescription.setObservations("observaciones");
        prescription.setStatus(Constants.STATUS_INACTIVE);

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        PrescriptionDto prescriptionDto=prescriptionAdapter.deleteOut(id);

        assertNotNull(prescriptionDto);
        assertEquals(id,prescriptionDto.getId());
        assertEquals(prescriptionDto.getObservations(), prescriptionDto.getObservations());
        assertEquals(prescriptionDto.getStatus(),Constants.STATUS_INACTIVE);

    }
}