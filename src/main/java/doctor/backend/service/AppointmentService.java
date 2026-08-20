package doctor.backend.service;

import doctor.backend.dto.appointment.AppointmentRequest;
import doctor.backend.dto.appointment.AppointmentResponse;
import doctor.backend.entity.Appointment;
import doctor.backend.entity.Patient;
import doctor.backend.repository.AppointmentRepository;
import doctor.backend.repository.PatientRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    // =====================================================
    // CREATE APPOINTMENT
    // =====================================================

    public AppointmentResponse createAppointment(
            AppointmentRequest request) {

        Patient patient = patientRepository.findById(
                request.getPatientId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Patient not found with id: "
                                + request.getPatientId()
                )
        );

        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );
        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );
        appointment.setAppointmentType(
                request.getAppointmentType()
        );
        appointment.setReason(
                request.getReason()
        );
        appointment.setStatus(
                request.getStatus()
        );
        appointment.setNotes(
                request.getNotes()
        );
        appointment.setDoctorName(
                request.getDoctorName()
        );

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return mapToResponse(savedAppointment);
    }

    // =====================================================
    // GET ALL APPOINTMENTS
    // =====================================================

    public List<AppointmentResponse> getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET APPOINTMENT BY ID
    // =====================================================

    public AppointmentResponse getAppointmentById(Long id) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(appointment);
    }

    // =====================================================
    // GET APPOINTMENTS BY PATIENT
    // =====================================================

    public List<AppointmentResponse> getAppointmentsByPatient(
            Long patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException(
                    "Patient not found with id: " + patientId
            );
        }

        return appointmentRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET APPOINTMENTS BY DATE
    // =====================================================

    public List<AppointmentResponse> getAppointmentsByDate(
            LocalDate date) {

        return appointmentRepository
                .findByAppointmentDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET APPOINTMENTS BY STATUS
    // =====================================================

    public List<AppointmentResponse> getAppointmentsByStatus(
            String status) {

        return appointmentRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET APPOINTMENTS BY DATE AND STATUS
    // =====================================================

    public List<AppointmentResponse> getAppointmentsByDateAndStatus(
            LocalDate date,
            String status) {

        return appointmentRepository
                .findByAppointmentDateAndStatus(
                        date,
                        status
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET APPOINTMENTS BETWEEN DATES
    // =====================================================

    public List<AppointmentResponse> getAppointmentsBetweenDates(
            LocalDate startDate,
            LocalDate endDate) {

        return appointmentRepository
                .findByAppointmentDateBetween(
                        startDate,
                        endDate
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // UPDATE APPOINTMENT
    // =====================================================

    public AppointmentResponse updateAppointment(
            Long id,
            AppointmentRequest request) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found with id: "
                                                + id
                                )
                        );

        Patient patient = patientRepository.findById(
                request.getPatientId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Patient not found with id: "
                                + request.getPatientId()
                )
        );

        appointment.setPatient(patient);

        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );

        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );

        appointment.setAppointmentType(
                request.getAppointmentType()
        );

        appointment.setReason(
                request.getReason()
        );

        appointment.setStatus(
                request.getStatus()
        );

        appointment.setNotes(
                request.getNotes()
        );

        appointment.setDoctorName(
                request.getDoctorName()
        );

        Appointment updatedAppointment =
                appointmentRepository.save(appointment);

        return mapToResponse(updatedAppointment);
    }

    // =====================================================
    // DELETE APPOINTMENT
    // =====================================================

    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {

            throw new RuntimeException(
                    "Appointment not found with id: " + id
            );
        }

        appointmentRepository.deleteById(id);
    }

    // =====================================================
    // ENTITY → RESPONSE
    // =====================================================

    private AppointmentResponse mapToResponse(
            Appointment appointment) {

        AppointmentResponse response =
                new AppointmentResponse();

        response.setId(appointment.getId());

        // =========================
        // Appointment Information
        // =========================

        response.setAppointmentDate(
                appointment.getAppointmentDate()
        );

        response.setAppointmentTime(
                appointment.getAppointmentTime()
        );

        response.setAppointmentType(
                appointment.getAppointmentType()
        );

        response.setReason(
                appointment.getReason()
        );

        response.setStatus(
                appointment.getStatus()
        );

        response.setNotes(
                appointment.getNotes()
        );

        response.setDoctorName(
                appointment.getDoctorName()
        );

        // =========================
        // Patient Information
        // =========================

        Patient patient = appointment.getPatient();

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

            response.setIcon(
                    patient.getIcon()
            );

            // =========================
            // Owner Information
            // =========================

            if (patient.getOwner() != null) {

                response.setOwnerId(
                        patient.getOwner().getId()
                );

                response.setOwnerName(
                        patient.getOwner().getFullName()
                );

                response.setOwnerPhone(
                        patient.getOwner().getPhone()
                );
            }
        }

        // =========================
        // Timestamps
        // =========================

        response.setCreatedAt(
                appointment.getCreatedAt()
        );

        response.setUpdatedAt(
                appointment.getUpdatedAt()
        );

        return response;
    }
}