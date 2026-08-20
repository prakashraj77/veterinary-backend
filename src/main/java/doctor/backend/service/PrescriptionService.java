package doctor.backend.service;

import doctor.backend.dto.prescription.PrescriptionItemRequest;
import doctor.backend.dto.prescription.PrescriptionItemResponse;
import doctor.backend.dto.prescription.PrescriptionRequest;
import doctor.backend.dto.prescription.PrescriptionResponse;
import doctor.backend.entity.Medicine;
import doctor.backend.entity.MedicalRecord;
import doctor.backend.entity.Owner;
import doctor.backend.entity.Patient;
import doctor.backend.entity.Prescription;
import doctor.backend.entity.PrescriptionItem;
import doctor.backend.repository.MedicalRecordRepository;
import doctor.backend.repository.MedicineRepository;
import doctor.backend.repository.PatientRepository;
import doctor.backend.repository.PrescriptionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicineRepository medicineRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            PatientRepository patientRepository,
            MedicalRecordRepository medicalRecordRepository,
            MedicineRepository medicineRepository) {

        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicineRepository = medicineRepository;
    }

    // =====================================================
    // CREATE PRESCRIPTION
    // =====================================================

    public PrescriptionResponse createPrescription(
            PrescriptionRequest request) {

        if (request.getPatientId() == null) {
            throw new RuntimeException("Patient ID is required");
        }

        if (request.getPrescriptionDate() == null) {
            throw new RuntimeException(
                    "Prescription date is required"
            );
        }

        Patient patient = patientRepository
                .findById(request.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: "
                                        + request.getPatientId()
                        )
                );

        Prescription prescription = new Prescription();

        prescription.setPatient(patient);
        prescription.setPrescriptionDate(
                request.getPrescriptionDate()
        );
        prescription.setDiagnosis(
                request.getDiagnosis()
        );
        prescription.setInstructions(
                request.getInstructions()
        );
        prescription.setNotes(
                request.getNotes()
        );
        prescription.setDoctorName(
                request.getDoctorName()
        );

        // Optional medical record
        if (request.getMedicalRecordId() != null) {

            MedicalRecord medicalRecord =
                    medicalRecordRepository
                            .findById(request.getMedicalRecordId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Medical record not found with id: "
                                                    + request.getMedicalRecordId()
                                    )
                            );

            if (!medicalRecord.getPatient()
                    .getId()
                    .equals(patient.getId())) {

                throw new RuntimeException(
                        "Medical record does not belong to this patient"
                );
            }

            prescription.setMedicalRecord(
                    medicalRecord
            );
        }

        // Add medicines
        if (request.getItems() != null) {

            for (PrescriptionItemRequest itemRequest
                    : request.getItems()) {

                PrescriptionItem item =
                        createPrescriptionItem(itemRequest);

                prescription.addItem(item);
            }
        }

        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        return mapToResponse(savedPrescription);
    }

    // =====================================================
    // CREATE PRESCRIPTION ITEM
    // =====================================================

    private PrescriptionItem createPrescriptionItem(
            PrescriptionItemRequest request) {

        if (request.getMedicineId() == null) {
            throw new RuntimeException(
                    "Medicine ID is required"
            );
        }

        Medicine medicine = medicineRepository
                .findById(request.getMedicineId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Medicine not found with id: "
                                        + request.getMedicineId()
                        )
                );

        PrescriptionItem item =
                new PrescriptionItem();

        item.setMedicine(medicine);
        item.setDosage(request.getDosage());
        item.setFrequency(request.getFrequency());
        item.setDuration(request.getDuration());
        item.setRoute(request.getRoute());
        item.setQuantity(request.getQuantity());
        item.setInstructions(
                request.getInstructions()
        );

        return item;
    }

    // =====================================================
    // GET ALL PRESCRIPTIONS
    // =====================================================

    @Transactional(readOnly = true)
    public List<PrescriptionResponse>
    getAllPrescriptions() {

        return prescriptionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PRESCRIPTION BY ID
    // =====================================================

    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionById(
            Long id) {

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Prescription not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(prescription);
    }

    // =====================================================
    // GET PRESCRIPTIONS BY PATIENT
    // =====================================================

    @Transactional(readOnly = true)
    public List<PrescriptionResponse>
    getPrescriptionsByPatient(Long patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException(
                    "Patient not found with id: " + patientId
            );
        }

        return prescriptionRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PRESCRIPTIONS BY MEDICAL RECORD
    // =====================================================

    @Transactional(readOnly = true)
    public List<PrescriptionResponse>
    getPrescriptionsByMedicalRecord(
            Long medicalRecordId) {

        if (!medicalRecordRepository
                .existsById(medicalRecordId)) {

            throw new RuntimeException(
                    "Medical record not found with id: "
                            + medicalRecordId
            );
        }

        return prescriptionRepository
                .findByMedicalRecordId(medicalRecordId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PRESCRIPTIONS BY DATE
    // =====================================================

    @Transactional(readOnly = true)
    public List<PrescriptionResponse>
    getPrescriptionsByDate(LocalDate date) {

        return prescriptionRepository
                .findByPrescriptionDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PATIENT PRESCRIPTIONS BY DATE
    // =====================================================

    @Transactional(readOnly = true)
    public List<PrescriptionResponse>
    getPrescriptionsByPatientAndDate(
            Long patientId,
            LocalDate date) {

        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException(
                    "Patient not found with id: " + patientId
            );
        }

        return prescriptionRepository
                .findByPatientIdAndPrescriptionDate(
                        patientId,
                        date
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PRESCRIPTIONS BETWEEN DATES
    // =====================================================

    @Transactional(readOnly = true)
    public List<PrescriptionResponse>
    getPrescriptionsBetweenDates(
            LocalDate startDate,
            LocalDate endDate) {

        return prescriptionRepository
                .findByPrescriptionDateBetween(
                        startDate,
                        endDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // UPDATE PRESCRIPTION
    // =====================================================

    public PrescriptionResponse updatePrescription(
            Long id,
            PrescriptionRequest request) {

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Prescription not found with id: "
                                                + id
                                )
                        );

        Patient patient =
                patientRepository.findById(
                        request.getPatientId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: "
                                        + request.getPatientId()
                        )
                );

        prescription.setPatient(patient);

        prescription.setPrescriptionDate(
                request.getPrescriptionDate()
        );

        prescription.setDiagnosis(
                request.getDiagnosis()
        );

        prescription.setInstructions(
                request.getInstructions()
        );

        prescription.setNotes(
                request.getNotes()
        );

        prescription.setDoctorName(
                request.getDoctorName()
        );

        // Update medical record
        prescription.setMedicalRecord(null);

        if (request.getMedicalRecordId() != null) {

            MedicalRecord medicalRecord =
                    medicalRecordRepository
                            .findById(request.getMedicalRecordId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Medical record not found with id: "
                                                    + request.getMedicalRecordId()
                                    )
                            );

            if (!medicalRecord.getPatient()
                    .getId()
                    .equals(patient.getId())) {

                throw new RuntimeException(
                        "Medical record does not belong to this patient"
                );
            }

            prescription.setMedicalRecord(
                    medicalRecord
            );
        }

        // Replace prescription items
        prescription.getItems().clear();

        if (request.getItems() != null) {

            for (PrescriptionItemRequest itemRequest
                    : request.getItems()) {

                PrescriptionItem item =
                        createPrescriptionItem(itemRequest);

                prescription.addItem(item);
            }
        }

        Prescription updatedPrescription =
                prescriptionRepository.save(prescription);

        return mapToResponse(updatedPrescription);
    }

    // =====================================================
    // DELETE PRESCRIPTION
    // =====================================================

    public void deletePrescription(Long id) {

        if (!prescriptionRepository.existsById(id)) {

            throw new RuntimeException(
                    "Prescription not found with id: " + id
            );
        }

        prescriptionRepository.deleteById(id);
    }

    // =====================================================
    // ENTITY → RESPONSE
    // =====================================================

    private PrescriptionResponse mapToResponse(
            Prescription prescription) {

        PrescriptionResponse response =
                new PrescriptionResponse();

        response.setId(prescription.getId());

        response.setPrescriptionDate(
                prescription.getPrescriptionDate()
        );

        response.setDiagnosis(
                prescription.getDiagnosis()
        );

        response.setInstructions(
                prescription.getInstructions()
        );

        response.setNotes(
                prescription.getNotes()
        );

        response.setDoctorName(
                prescription.getDoctorName()
        );

        response.setCreatedAt(
                prescription.getCreatedAt()
        );

        response.setUpdatedAt(
                prescription.getUpdatedAt()
        );

        // =========================
        // Patient
        // =========================

        Patient patient =
                prescription.getPatient();

        if (patient != null) {

            response.setPatientId(
                    patient.getId()
            );

            response.setPatientName(
                    patient.getName()
            );

            response.setSpecies(
                    patient.getSpecies()
            );

            response.setBreed(
                    patient.getBreed()
            );

            response.setGender(
                    patient.getGender()
            );

            response.setIcon(
                    patient.getIcon()
            );

            // =========================
            // Owner
            // =========================

            Owner owner =
                    patient.getOwner();

            if (owner != null) {

                response.setOwnerId(
                        owner.getId()
                );

                response.setOwnerName(
                        owner.getFullName()
                );

                response.setOwnerPhone(
                        owner.getPhone()
                );
            }
        }

        // =========================
        // Medical Record
        // =========================

        MedicalRecord medicalRecord =
                prescription.getMedicalRecord();

        if (medicalRecord != null) {

            response.setMedicalRecordId(
                    medicalRecord.getId()
            );
        }

        // =========================
        // Items
        // =========================

        List<PrescriptionItemResponse> itemResponses =
                new ArrayList<>();

        if (prescription.getItems() != null) {

            for (PrescriptionItem item
                    : prescription.getItems()) {

                PrescriptionItemResponse itemResponse =
                        new PrescriptionItemResponse();

                itemResponse.setId(
                        item.getId()
                );

                Medicine medicine =
                        item.getMedicine();

                if (medicine != null) {

                    itemResponse.setMedicineId(
                            medicine.getId()
                    );

                    itemResponse.setMedicineName(
                            medicine.getName()
                    );
                }

                itemResponse.setDosage(
                        item.getDosage()
                );

                itemResponse.setFrequency(
                        item.getFrequency()
                );

                itemResponse.setDuration(
                        item.getDuration()
                );

                itemResponse.setRoute(
                        item.getRoute()
                );

                itemResponse.setQuantity(
                        item.getQuantity()
                );

                itemResponse.setInstructions(
                        item.getInstructions()
                );

                itemResponses.add(itemResponse);
            }
        }

        response.setItems(itemResponses);

        return response;
    }
}