package doctor.backend.service;

import doctor.backend.entity.DoctorProfile;
import doctor.backend.repository.DoctorProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorProfileService {

    private final DoctorProfileRepository repository;

    public DoctorProfileService(
            DoctorProfileRepository repository
    ) {
        this.repository = repository;
    }

    public DoctorProfile getProfile() {

        return repository
                .findById(1L)
                .orElseGet(() -> {
                    DoctorProfile profile =
                            new DoctorProfile();

                    return repository.save(profile);
                });
    }

    public DoctorProfile saveProfile(
            DoctorProfile profile
    ) {

        DoctorProfile existing =
                repository
                        .findById(1L)
                        .orElseGet(
                                DoctorProfile::new
                        );

        existing.setFullName(
                profile.getFullName()
        );

        existing.setQualification(
                profile.getQualification()
        );

        existing.setCouncilRegistration(
                profile.getCouncilRegistration()
        );

        existing.setClinicHospital(
                profile.getClinicHospital()
        );

        existing.setPhone(
                profile.getPhone()
        );

        existing.setEmail(
                profile.getEmail()
        );

        existing.setDigitalSignatureName(
                profile.getDigitalSignatureName()
        );

        existing.setConsultationFee(
                profile.getConsultationFee()
        );

        existing.setFollowUpFee(
                profile.getFollowUpFee()
        );

        existing.setSlotLength(
                profile.getSlotLength()
        );

        return repository.save(existing);
    }
}