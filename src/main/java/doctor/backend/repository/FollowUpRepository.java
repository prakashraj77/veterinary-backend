package doctor.backend.repository;

import doctor.backend.entity.FollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FollowUpRepository
        extends JpaRepository<FollowUp, Long> {

    List<FollowUp> findByPatientId(Long patientId);

    List<FollowUp> findByStatus(String status);

    List<FollowUp> findByPatientIdAndStatus(
            Long patientId,
            String status
    );

    List<FollowUp> findByFollowUpDate(
            LocalDate followUpDate
    );

    List<FollowUp> findByFollowUpDateBefore(
            LocalDate date
    );

    List<FollowUp> findByFollowUpDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    List<FollowUp> findByNextFollowUpDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    List<FollowUp> findByNextFollowUpDateBefore(
            LocalDate date
    );

    List<FollowUp> findByAppointmentId(
            Long appointmentId
    );

    List<FollowUp> findByReminderSentFalse();
}