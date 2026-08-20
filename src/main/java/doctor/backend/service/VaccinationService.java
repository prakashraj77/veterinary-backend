package doctor.backend.service;

import doctor.backend.dto.vaccination.VaccinationRequest;
import doctor.backend.dto.vaccination.VaccinationResponse;
import doctor.backend.entity.Patient;
import doctor.backend.entity.Vaccination;
import doctor.backend.repository.PatientRepository;
import doctor.backend.repository.VaccinationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final PatientRepository patientRepository;

    public VaccinationService(
            VaccinationRepository vaccinationRepository,
            PatientRepository patientRepository) {

        this.vaccinationRepository = vaccinationRepository;
        this.patientRepository = patientRepository;
    }

    // =====================================================
    // CREATE VACCINATION
    // =====================================================

    public VaccinationResponse createVaccination(
            VaccinationRequest request) {

        if (request.getPatientId() == null) {
            throw new RuntimeException("Patient ID is required");
        }

        if (request.getVaccineName() == null ||
                request.getVaccineName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Vaccine name is required"
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

        Vaccination vaccination = new Vaccination();

        vaccination.setPatient(patient);
        vaccination.setVaccineName(request.getVaccineName());
        vaccination.setVaccineType(request.getVaccineType());
        vaccination.setManufacturer(request.getManufacturer());
        vaccination.setBatchNumber(request.getBatchNumber());
        vaccination.setVaccinationDate(
                request.getVaccinationDate()
        );
        vaccination.setNextDueDate(
                request.getNextDueDate()
        );
        vaccination.setDosage(request.getDosage());
        vaccination.setRoute(request.getRoute());
        vaccination.setAdministeredBy(
                request.getAdministeredBy()
        );
        vaccination.setNotes(request.getNotes());

        if (request.getStatus() != null &&
                !request.getStatus().trim().isEmpty()) {

            vaccination.setStatus(request.getStatus());

        } else {

            vaccination.setStatus("Scheduled");
        }

        Vaccination savedVaccination =
                vaccinationRepository.save(vaccination);

        return mapToResponse(savedVaccination);
    }

    // =====================================================
    // GET ALL VACCINATIONS
    // =====================================================

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getAllVaccinations() {

        return vaccinationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET VACCINATION BY ID
    // =====================================================

    @Transactional(readOnly = true)
    public VaccinationResponse getVaccinationById(Long id) {

        Vaccination vaccination =
                vaccinationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vaccination not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(vaccination);
    }

    // =====================================================
    // GET VACCINATIONS BY PATIENT
    // =====================================================

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getByPatient(
            Long patientId) {

        return vaccinationRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY STATUS
    // =====================================================

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getByStatus(
            String status) {

        return vaccinationRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY VACCINE NAME
    // =====================================================

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getByVaccineName(
            String vaccineName) {

        return vaccinationRepository
                .findByVaccineName(vaccineName)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET UPCOMING / DUE VACCINATIONS
    // =====================================================

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getDueVaccinations(
            LocalDate date) {

        return vaccinationRepository
                .findByNextDueDateBefore(date.plusDays(1))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET VACCINATIONS BETWEEN DATES
    // =====================================================

    @Transactional(readOnly = true)
    public List<VaccinationResponse>
    getVaccinationsBetweenDates(
            LocalDate startDate,
            LocalDate endDate) {

        return vaccinationRepository
                .findByNextDueDateBetween(
                        startDate,
                        endDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PATIENT VACCINATIONS BY STATUS
    // =====================================================

    @Transactional(readOnly = true)
    public List<VaccinationResponse>
    getPatientVaccinationsByStatus(
            Long patientId,
            String status) {

        return vaccinationRepository
                .findByPatientIdAndStatus(
                        patientId,
                        status
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET VACCINATIONS ON DATE
    // =====================================================

    @Transactional(readOnly = true)
    public List<VaccinationResponse>
    getVaccinationsOnDate(
            LocalDate vaccinationDate) {

        return vaccinationRepository
                .findByVaccinationDate(vaccinationDate)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // UPDATE VACCINATION
    // =====================================================

    public VaccinationResponse updateVaccination(
            Long id,
            VaccinationRequest request) {

        Vaccination vaccination =
                vaccinationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vaccination not found with id: "
                                                + id
                                )
                        );

        if (request.getPatientId() != null) {

            Patient patient =
                    patientRepository.findById(
                            request.getPatientId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Patient not found with id: "
                                            + request.getPatientId()
                            )
                    );

            vaccination.setPatient(patient);
        }

        vaccination.setVaccineName(
                request.getVaccineName()
        );

        vaccination.setVaccineType(
                request.getVaccineType()
        );

        vaccination.setManufacturer(
                request.getManufacturer()
        );

        vaccination.setBatchNumber(
                request.getBatchNumber()
        );

        vaccination.setVaccinationDate(
                request.getVaccinationDate()
        );

        vaccination.setNextDueDate(
                request.getNextDueDate()
        );

        vaccination.setDosage(
                request.getDosage()
        );

        vaccination.setRoute(
                request.getRoute()
        );

        vaccination.setAdministeredBy(
                request.getAdministeredBy()
        );

        vaccination.setStatus(
                request.getStatus()
        );

        vaccination.setNotes(
                request.getNotes()
        );

        Vaccination updatedVaccination =
                vaccinationRepository.save(vaccination);

        return mapToResponse(updatedVaccination);
    }

    // =====================================================
    // DELETE VACCINATION
    // =====================================================

    public void deleteVaccination(Long id) {

        if (!vaccinationRepository.existsById(id)) {

            throw new RuntimeException(
                    "Vaccination not found with id: "
                            + id
            );
        }

        vaccinationRepository.deleteById(id);
    }

    // =====================================================
    // ENTITY → RESPONSE
    // =====================================================

    private VaccinationResponse mapToResponse(
            Vaccination vaccination) {

        VaccinationResponse response =
                new VaccinationResponse();

        response.setId(vaccination.getId());

        if (vaccination.getPatient() != null) {

            response.setPatientId(
                    vaccination.getPatient().getId()
            );

            response.setPatientName(
                    vaccination.getPatient().getName()
            );
        }

        response.setVaccineName(
                vaccination.getVaccineName()
        );

        response.setVaccineType(
                vaccination.getVaccineType()
        );

        response.setManufacturer(
                vaccination.getManufacturer()
        );

        response.setBatchNumber(
                vaccination.getBatchNumber()
        );

        response.setVaccinationDate(
                vaccination.getVaccinationDate()
        );

        response.setNextDueDate(
                vaccination.getNextDueDate()
        );

        response.setDosage(
                vaccination.getDosage()
        );

        response.setRoute(
                vaccination.getRoute()
        );

        response.setAdministeredBy(
                vaccination.getAdministeredBy()
        );

        response.setStatus(
                vaccination.getStatus()
        );

        response.setNotes(
                vaccination.getNotes()
        );

        response.setCreatedAt(
                vaccination.getCreatedAt()
        );

        response.setUpdatedAt(
                vaccination.getUpdatedAt()
        );

        return response;
    }
}