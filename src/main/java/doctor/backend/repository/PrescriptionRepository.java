package doctor.backend.repository;

import doctor.backend.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long> {

    // Get all prescriptions for a patient
    List<Prescription> findByPatientId(Long patientId);

    // Get prescriptions by medical record
    List<Prescription> findByMedicalRecordId(Long medicalRecordId);

    // Get prescriptions by date
    List<Prescription> findByPrescriptionDate(LocalDate prescriptionDate);

    // Get patient prescriptions by date
    List<Prescription> findByPatientIdAndPrescriptionDate(
            Long patientId,
            LocalDate prescriptionDate
    );

    // Get prescriptions between dates
    List<Prescription> findByPrescriptionDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}