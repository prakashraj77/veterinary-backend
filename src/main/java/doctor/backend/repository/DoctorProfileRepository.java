package doctor.backend.repository;

import doctor.backend.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorProfileRepository
        extends JpaRepository<DoctorProfile, Long> {
}