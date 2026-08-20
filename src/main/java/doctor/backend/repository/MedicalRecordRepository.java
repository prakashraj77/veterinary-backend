package doctor.backend.repository;

import doctor.backend.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, Long> {

    // Get all medical records for a patient
    List<MedicalRecord> findByPatientId(Long patientId);

    // Get records for a specific date
    List<MedicalRecord> findByVisitDate(LocalDate visitDate);

    // Get records for a patient on a specific date
    List<MedicalRecord> findByPatientIdAndVisitDate(
            Long patientId,
            LocalDate visitDate
    );

    // Get records between two dates
    List<MedicalRecord> findByVisitDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}