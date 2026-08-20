package doctor.backend.controller;

import doctor.backend.config.AdminIntegrationProperties;
import doctor.backend.dto.auth.DoctorStatusUpdateRequest;
import doctor.backend.exception.ForbiddenException;
import doctor.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Called by the Zenve admin backend whenever an admin approves or rejects a
 * doctor's registration, so this app can allow (or keep blocking) login.
 *
 * Not meant to be called from a browser: it's protected by a shared secret
 * header rather than the normal doctor JWT auth, and is excluded from the
 * public CORS/security rules in SecurityConfig.
 */
@RestController
@RequestMapping("/api/internal/doctors")
public class InternalDoctorStatusController {

    private static final String SECRET_HEADER = "X-Internal-Secret";

    private final AuthService authService;
    private final AdminIntegrationProperties properties;

    public InternalDoctorStatusController(AuthService authService, AdminIntegrationProperties properties) {
        this.authService = authService;
        this.properties = properties;
    }

    @PostMapping("/status")
    public ResponseEntity<Void> updateStatus(
            @RequestHeader(value = SECRET_HEADER, required = false) String secret,
            @Valid @RequestBody DoctorStatusUpdateRequest request
    ) {
        if (properties.getInternalSecret() == null || !properties.getInternalSecret().equals(secret)) {
            throw new ForbiddenException("Invalid internal secret");
        }

        authService.updateApprovalStatus(request.getEmail(), request.getStatus(), request.getReason());

        return ResponseEntity.ok().build();
    }
}
