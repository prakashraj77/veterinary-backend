package doctor.backend.service;

import doctor.backend.dto.followup.FollowUpRequest;
import doctor.backend.dto.followup.FollowUpResponse;
import doctor.backend.entity.Appointment;
import doctor.backend.entity.FollowUp;
import doctor.backend.entity.Patient;
import doctor.backend.repository.AppointmentRepository;
import doctor.backend.repository.FollowUpRepository;
import doctor.backend.repository.PatientRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public FollowUpService(
            FollowUpRepository followUpRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository) {

        this.followUpRepository = followUpRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // =====================================================
    // CREATE FOLLOW-UP
    // =====================================================

    public FollowUpResponse createFollowUp(
            FollowUpRequest request) {

        if (request.getPatientId() == null) {
            throw new RuntimeException("Patient ID is required");
        }

        Patient patient = patientRepository
                .findById(request.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: "
                                        + request.getPatientId()
                        )
                );

        FollowUp followUp = new FollowUp();

        followUp.setPatient(patient);

        // Appointment is optional
        if (request.getAppointmentId() != null) {

            Appointment appointment =
                    appointmentRepository
                            .findById(request.getAppointmentId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Appointment not found with id: "
                                                    + request.getAppointmentId()
                                    )
                            );

            followUp.setAppointment(appointment);
        }

        followUp.setFollowUpDate(
                request.getFollowUpDate()
        );

        followUp.setNextFollowUpDate(
                request.getNextFollowUpDate()
        );

        followUp.setReason(
                request.getReason()
        );

        followUp.setSymptoms(
                request.getSymptoms()
        );

        followUp.setFindings(
                request.getFindings()
        );

        followUp.setTreatment(
                request.getTreatment()
        );

        followUp.setRecommendations(
                request.getRecommendations()
        );

        followUp.setNotes(
                request.getNotes()
        );

        followUp.setDoctorName(
                request.getDoctorName()
        );

        if (request.getStatus() != null
                && !request.getStatus().trim().isEmpty()) {

            followUp.setStatus(
                    request.getStatus()
            );

        } else {

            followUp.setStatus("Scheduled");
        }

        if (request.getReminderSent() != null) {

            followUp.setReminderSent(
                    request.getReminderSent()
            );

        } else {

            followUp.setReminderSent(false);
        }

        FollowUp savedFollowUp =
                followUpRepository.save(followUp);

        return mapToResponse(savedFollowUp);
    }

    // =====================================================
    // GET ALL FOLLOW-UPS
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse> getAllFollowUps() {

        return followUpRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET FOLLOW-UP BY ID
    // =====================================================

    @Transactional(readOnly = true)
    public FollowUpResponse getFollowUpById(Long id) {

        FollowUp followUp =
                followUpRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Follow-up not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(followUp);
    }

    // =====================================================
    // GET BY PATIENT
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse> getByPatient(
            Long patientId) {

        return followUpRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY STATUS
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse> getByStatus(
            String status) {

        return followUpRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PATIENT FOLLOW-UPS BY STATUS
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse>
    getPatientFollowUpsByStatus(
            Long patientId,
            String status) {

        return followUpRepository
                .findByPatientIdAndStatus(
                        patientId,
                        status
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY FOLLOW-UP DATE
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse>
    getFollowUpsOnDate(LocalDate date) {

        return followUpRepository
                .findByFollowUpDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BEFORE DATE
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse>
    getFollowUpsBefore(LocalDate date) {

        return followUpRepository
                .findByFollowUpDateBefore(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BETWEEN DATES
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse>
    getFollowUpsBetween(
            LocalDate startDate,
            LocalDate endDate) {

        return followUpRepository
                .findByFollowUpDateBetween(
                        startDate,
                        endDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET UPCOMING FOLLOW-UPS
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse>
    getUpcomingFollowUps(
            LocalDate startDate,
            LocalDate endDate) {

        return followUpRepository
                .findByNextFollowUpDateBetween(
                        startDate,
                        endDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET OVERDUE FOLLOW-UPS
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse>
    getOverdueFollowUps(LocalDate date) {

        return followUpRepository
                .findByNextFollowUpDateBefore(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY APPOINTMENT
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse>
    getByAppointment(Long appointmentId) {

        return followUpRepository
                .findByAppointmentId(appointmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PENDING REMINDERS
    // =====================================================

    @Transactional(readOnly = true)
    public List<FollowUpResponse>
    getPendingReminders() {

        return followUpRepository
                .findByReminderSentFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // UPDATE FOLLOW-UP
    // =====================================================

    public FollowUpResponse updateFollowUp(
            Long id,
            FollowUpRequest request) {

        FollowUp followUp =
                followUpRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Follow-up not found with id: "
                                                + id
                                )
                        );

        if (request.getPatientId() != null) {

            Patient patient =
                    patientRepository
                            .findById(request.getPatientId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Patient not found with id: "
                                                    + request.getPatientId()
                                    )
                            );

            followUp.setPatient(patient);
        }

        if (request.getAppointmentId() != null) {

            Appointment appointment =
                    appointmentRepository
                            .findById(request.getAppointmentId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Appointment not found with id: "
                                                    + request.getAppointmentId()
                                    )
                            );

            followUp.setAppointment(appointment);
        }

        followUp.setFollowUpDate(
                request.getFollowUpDate()
        );

        followUp.setNextFollowUpDate(
                request.getNextFollowUpDate()
        );

        followUp.setReason(
                request.getReason()
        );

        followUp.setStatus(
                request.getStatus()
        );

        followUp.setSymptoms(
                request.getSymptoms()
        );

        followUp.setFindings(
                request.getFindings()
        );

        followUp.setTreatment(
                request.getTreatment()
        );

        followUp.setRecommendations(
                request.getRecommendations()
        );

        followUp.setNotes(
                request.getNotes()
        );

        followUp.setDoctorName(
                request.getDoctorName()
        );

        if (request.getReminderSent() != null) {

            followUp.setReminderSent(
                    request.getReminderSent()
            );
        }

        FollowUp updatedFollowUp =
                followUpRepository.save(followUp);

        return mapToResponse(updatedFollowUp);
    }

    // =====================================================
    // MARK REMINDER AS SENT
    // =====================================================

    public FollowUpResponse markReminderAsSent(Long id) {

        FollowUp followUp =
                followUpRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Follow-up not found with id: "
                                                + id
                                )
                        );

        followUp.setReminderSent(true);

        FollowUp updatedFollowUp =
                followUpRepository.save(followUp);

        return mapToResponse(updatedFollowUp);
    }

    // =====================================================
    // DELETE
    // =====================================================

    public void deleteFollowUp(Long id) {

        if (!followUpRepository.existsById(id)) {

            throw new RuntimeException(
                    "Follow-up not found with id: " + id
            );
        }

        followUpRepository.deleteById(id);
    }

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    private FollowUpResponse mapToResponse(
            FollowUp followUp) {

        FollowUpResponse response =
                new FollowUpResponse();

        response.setId(
                followUp.getId()
        );

        if (followUp.getPatient() != null) {

            response.setPatientId(
                    followUp.getPatient().getId()
            );

            response.setPatientName(
                    followUp.getPatient().getName()
            );
        }

        if (followUp.getAppointment() != null) {

            response.setAppointmentId(
                    followUp.getAppointment().getId()
            );
        }

        response.setFollowUpDate(
                followUp.getFollowUpDate()
        );

        response.setNextFollowUpDate(
                followUp.getNextFollowUpDate()
        );

        response.setReason(
                followUp.getReason()
        );

        response.setStatus(
                followUp.getStatus()
        );

        response.setSymptoms(
                followUp.getSymptoms()
        );

        response.setFindings(
                followUp.getFindings()
        );

        response.setTreatment(
                followUp.getTreatment()
        );

        response.setRecommendations(
                followUp.getRecommendations()
        );

        response.setNotes(
                followUp.getNotes()
        );

        response.setDoctorName(
                followUp.getDoctorName()
        );

        response.setReminderSent(
                followUp.getReminderSent()
        );

        response.setCreatedAt(
                followUp.getCreatedAt()
        );

        response.setUpdatedAt(
                followUp.getUpdatedAt()
        );

        return response;
    }
}