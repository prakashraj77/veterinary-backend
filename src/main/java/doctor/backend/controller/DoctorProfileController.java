package doctor.backend.controller;

import doctor.backend.entity.DoctorProfile;
import doctor.backend.service.DoctorProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor-profile")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorProfileController {

    private final DoctorProfileService service;

    public DoctorProfileController(
            DoctorProfileService service
    ) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<DoctorProfile> getProfile() {

        return ResponseEntity.ok(
                service.getProfile()
        );
    }

    @PutMapping
    public ResponseEntity<DoctorProfile> updateProfile(
            @RequestBody DoctorProfile profile
    ) {

        return ResponseEntity.ok(
                service.saveProfile(profile)
        );
    }
}