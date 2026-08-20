package doctor.backend.service;

import doctor.backend.dto.medicalrecord.MedicalRecordRequest;
import doctor.backend.dto.medicalrecord.MedicalRecordResponse;
import doctor.backend.entity.MedicalRecord;
import doctor.backend.entity.Owner;
import doctor.backend.entity.Patient;
import doctor.backend.repository.MedicalRecordRepository;
import doctor.backend.repository.PatientRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordService(
            MedicalRecordRepository medicalRecordRepository,
            PatientRepository patientRepository) {

        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    // =====================================================
    // CREATE MEDICAL RECORD
    // =====================================================

    public MedicalRecordResponse createMedicalRecord(
            MedicalRecordRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: "
                                        + request.getPatientId()
                        )
                );

        MedicalRecord record = new MedicalRecord();

        record.setPatient(patient);
        record.setVisitDate(request.getVisitDate());
        record.setChiefComplaint(request.getChiefComplaint());
        record.setSymptoms(request.getSymptoms());
        record.setDiagnosis(request.getDiagnosis());
        record.setClinicalFindings(request.getClinicalFindings());
        record.setTreatment(request.getTreatment());
        record.setWeight(request.getWeight());
        record.setTemperature(request.getTemperature());
        record.setNotes(request.getNotes());
        record.setDoctorName(request.getDoctorName());

        MedicalRecord savedRecord =
                medicalRecordRepository.save(record);

        return mapToResponse(savedRecord);
    }

    // =====================================================
    // GET ALL MEDICAL RECORDS
    // =====================================================

    public List<MedicalRecordResponse> getAllMedicalRecords() {

        return medicalRecordRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET MEDICAL RECORD BY ID
    // =====================================================

    public MedicalRecordResponse getMedicalRecordById(Long id) {

        MedicalRecord record =
                medicalRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Medical record not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(record);
    }

    // =====================================================
    // GET RECORDS BY PATIENT
    // =====================================================

    public List<MedicalRecordResponse> getRecordsByPatient(
            Long patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException(
                    "Patient not found with id: " + patientId
            );
        }

        return medicalRecordRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET RECORDS BY DATE
    // =====================================================

    public List<MedicalRecordResponse> getRecordsByDate(
            LocalDate date) {

        return medicalRecordRepository
                .findByVisitDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PATIENT RECORDS BY DATE
    // =====================================================

    public List<MedicalRecordResponse> getRecordsByPatientAndDate(
            Long patientId,
            LocalDate date) {

        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException(
                    "Patient not found with id: " + patientId
            );
        }

        return medicalRecordRepository
                .findByPatientIdAndVisitDate(
                        patientId,
                        date
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET RECORDS BETWEEN DATES
    // =====================================================

    public List<MedicalRecordResponse> getRecordsBetweenDates(
            LocalDate startDate,
            LocalDate endDate) {

        return medicalRecordRepository
                .findByVisitDateBetween(
                        startDate,
                        endDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // UPDATE MEDICAL RECORD
    // =====================================================

    public MedicalRecordResponse updateMedicalRecord(
            Long id,
            MedicalRecordRequest request) {

        MedicalRecord record =
                medicalRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Medical record not found with id: "
                                                + id
                                )
                        );

        Patient patient =
                patientRepository.findById(request.getPatientId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found with id: "
                                                + request.getPatientId()
                                )
                        );

        record.setPatient(patient);
        record.setVisitDate(request.getVisitDate());
        record.setChiefComplaint(request.getChiefComplaint());
        record.setSymptoms(request.getSymptoms());
        record.setDiagnosis(request.getDiagnosis());
        record.setClinicalFindings(request.getClinicalFindings());
        record.setTreatment(request.getTreatment());
        record.setWeight(request.getWeight());
        record.setTemperature(request.getTemperature());
        record.setNotes(request.getNotes());
        record.setDoctorName(request.getDoctorName());

        MedicalRecord updatedRecord =
                medicalRecordRepository.save(record);

        return mapToResponse(updatedRecord);
    }

    // =====================================================
    // DELETE MEDICAL RECORD
    // =====================================================

    public void deleteMedicalRecord(Long id) {

        if (!medicalRecordRepository.existsById(id)) {
            throw new RuntimeException(
                    "Medical record not found with id: " + id
            );
        }

        medicalRecordRepository.deleteById(id);
    }

    // =====================================================
    // ENTITY → RESPONSE DTO
    // =====================================================

    private MedicalRecordResponse mapToResponse(
            MedicalRecord record) {

        MedicalRecordResponse response =
                new MedicalRecordResponse();

        Patient patient = record.getPatient();

        // Record information
        response.setId(record.getId());
        response.setVisitDate(record.getVisitDate());
        response.setChiefComplaint(
                record.getChiefComplaint()
        );
        response.setSymptoms(record.getSymptoms());
        response.setDiagnosis(record.getDiagnosis());
        response.setClinicalFindings(
                record.getClinicalFindings()
        );
        response.setTreatment(record.getTreatment());
        response.setWeight(record.getWeight());
        response.setTemperature(record.getTemperature());
        response.setNotes(record.getNotes());
        response.setDoctorName(record.getDoctorName());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());

        // Patient information
        if (patient != null) {

            response.setPatientId(patient.getId());
            response.setPatientName(patient.getName());
            response.setSpecies(patient.getSpecies());
            response.setBreed(patient.getBreed());
            response.setGender(patient.getGender());
            response.setIcon(patient.getIcon());

            // Owner information
            Owner owner = patient.getOwner();

            if (owner != null) {
                response.setOwnerId(owner.getId());
                response.setOwnerName(owner.getFullName());
                response.setOwnerPhone(owner.getPhone());
            }
        }

        return response;
    }
}