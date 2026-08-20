package doctor.backend.service;

import doctor.backend.dto.notification.NotificationRequest;
import doctor.backend.dto.notification.NotificationResponse;
import doctor.backend.entity.Appointment;
import doctor.backend.entity.Notification;
import doctor.backend.entity.Patient;
import doctor.backend.repository.AppointmentRepository;
import doctor.backend.repository.NotificationRepository;
import doctor.backend.repository.PatientRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository) {

        this.notificationRepository = notificationRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // =====================================================
    // CREATE NOTIFICATION
    // =====================================================

    public NotificationResponse createNotification(
            NotificationRequest request) {

        if (request.getTitle() == null ||
                request.getTitle().trim().isEmpty()) {

            throw new RuntimeException(
                    "Notification title is required"
            );
        }

        Notification notification = new Notification();

        notification.setTitle(
                request.getTitle()
        );

        notification.setMessage(
                request.getMessage()
        );

        notification.setType(
                request.getType() != null &&
                        !request.getType().trim().isEmpty()
                        ? request.getType()
                        : "General"
        );

        notification.setUserId(
                request.getUserId()
        );

        notification.setPriority(
                request.getPriority() != null &&
                        !request.getPriority().trim().isEmpty()
                        ? request.getPriority()
                        : "Normal"
        );

        if (request.getRead() != null) {
            notification.setRead(
                    request.getRead()
            );
        }

        // =========================
        // Patient
        // =========================

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

            notification.setPatient(patient);
        }

        // =========================
        // Appointment
        // =========================

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

            notification.setAppointment(
                    appointment
            );
        }

        Notification saved =
                notificationRepository.save(
                        notification
                );

        return mapToResponse(saved);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllNotifications() {

        return notificationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(
            Long id) {

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(notification);
    }

    // =====================================================
    // GET BY USER
    // =====================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getByUser(
            Long userId) {

        return notificationRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET UNREAD
    // =====================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnread() {

        return notificationRepository
                .findByReadFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET UNREAD BY USER
    // =====================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadByUser(
            Long userId) {

        return notificationRepository
                .findByUserIdAndReadFalse(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET READ
    // =====================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getRead() {

        return notificationRepository
                .findByReadTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY TYPE
    // =====================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getByType(
            String type) {

        return notificationRepository
                .findByType(type)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY PRIORITY
    // =====================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getByPriority(
            String priority) {

        return notificationRepository
                .findByPriority(priority)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY PATIENT
    // =====================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getByPatient(
            Long patientId) {

        return notificationRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY APPOINTMENT
    // =====================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getByAppointment(
            Long appointmentId) {

        return notificationRepository
                .findByAppointmentId(appointmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // MARK AS READ
    // =====================================================

    public NotificationResponse markAsRead(
            Long id) {

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found with id: "
                                                + id
                                )
                        );

        notification.setRead(true);

        Notification updated =
                notificationRepository.save(
                        notification
                );

        return mapToResponse(updated);
    }

    // =====================================================
    // MARK AS UNREAD
    // =====================================================

    public NotificationResponse markAsUnread(
            Long id) {

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found with id: "
                                                + id
                                )
                        );

        notification.setRead(false);

        Notification updated =
                notificationRepository.save(
                        notification
                );

        return mapToResponse(updated);
    }

    // =====================================================
    // MARK ALL USER NOTIFICATIONS AS READ
    // =====================================================

    public void markAllAsRead(Long userId) {

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdAndReadFalse(userId);

        for (Notification notification : notifications) {
            notification.setRead(true);
        }

        notificationRepository.saveAll(
                notifications
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    public void deleteNotification(Long id) {

        if (!notificationRepository.existsById(id)) {

            throw new RuntimeException(
                    "Notification not found with id: "
                            + id
            );
        }

        notificationRepository.deleteById(id);
    }

    // =====================================================
    // DELETE USER NOTIFICATIONS
    // =====================================================

    public void deleteUserNotifications(
            Long userId) {

        notificationRepository.deleteByUserId(
                userId
        );
    }

    // =====================================================
    // MAP ENTITY TO RESPONSE
    // =====================================================

    private NotificationResponse mapToResponse(
            Notification notification) {

        NotificationResponse response =
                new NotificationResponse();

        response.setId(
                notification.getId()
        );

        response.setTitle(
                notification.getTitle()
        );

        response.setMessage(
                notification.getMessage()
        );

        response.setType(
                notification.getType()
        );

        response.setUserId(
                notification.getUserId()
        );

        response.setPriority(
                notification.getPriority()
        );

        response.setRead(
                notification.isRead()
        );

        // =========================
        // Patient
        // =========================

        if (notification.getPatient() != null) {

            response.setPatientId(
                    notification.getPatient().getId()
            );

            /*
             * We intentionally don't call getName()
             * here because the existing Patient entity
             * may use a different field for the patient name.
             */
        }

        // =========================
        // Appointment
        // =========================

        if (notification.getAppointment() != null) {

            response.setAppointmentId(
                    notification
                            .getAppointment()
                            .getId()
            );
        }

        response.setCreatedAt(
                notification.getCreatedAt()
        );

        response.setUpdatedAt(
                notification.getUpdatedAt()
        );

        return response;
    }
}