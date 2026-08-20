package doctor.backend.repository;

import doctor.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByOwnerId(Long ownerId);

    List<Patient> findBySpecies(String species);

    List<Patient> findByNameContainingIgnoreCase(String name);

    List<Patient> findByStatus(String status);

    Optional<Patient> findTopBySpeciesIgnoreCaseAndPetIdIsNotNullOrderByPetIdDesc(
            String species
    );

    Optional<Patient> findByPetId(String petId);

    boolean existsByPetId(String petId);
}