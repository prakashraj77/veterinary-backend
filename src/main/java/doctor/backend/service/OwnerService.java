package doctor.backend.service;

import doctor.backend.entity.Owner;
import doctor.backend.dto.owner.OwnerRequest;
import doctor.backend.dto.owner.OwnerResponse;
import doctor.backend.repository.OwnerRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    // Create Owner
    public OwnerResponse createOwner(OwnerRequest request) {

        Owner owner = new Owner();

        owner.setFullName(request.getFullName());
        owner.setPhone(request.getPhone());
        owner.setEmail(request.getEmail());
        owner.setAddress(request.getAddress());
        owner.setCity(request.getCity());
        owner.setState(request.getState());
        owner.setPincode(request.getPincode());
        owner.setEmergencyContact(request.getEmergencyContact());
        owner.setNotes(request.getNotes());

        Owner savedOwner = ownerRepository.save(owner);

        return mapToResponse(savedOwner);
    }

    // Get all Owners
    public List<OwnerResponse> getAllOwners() {

        return ownerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get Owner by ID
    public OwnerResponse getOwnerById(Long id) {

        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Owner not found with id: " + id
                        )
                );

        return mapToResponse(owner);
    }

    // Update Owner
    public OwnerResponse updateOwner(
            Long id,
            OwnerRequest request) {

        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Owner not found with id: " + id
                        )
                );

        owner.setFullName(request.getFullName());
        owner.setPhone(request.getPhone());
        owner.setEmail(request.getEmail());
        owner.setAddress(request.getAddress());
        owner.setCity(request.getCity());
        owner.setState(request.getState());
        owner.setPincode(request.getPincode());
        owner.setEmergencyContact(request.getEmergencyContact());
        owner.setNotes(request.getNotes());

        Owner updatedOwner = ownerRepository.save(owner);

        return mapToResponse(updatedOwner);
    }

    // Delete Owner
    public void deleteOwner(Long id) {

        if (!ownerRepository.existsById(id)) {
            throw new RuntimeException(
                    "Owner not found with id: " + id
            );
        }

        ownerRepository.deleteById(id);
    }

    // Convert Entity → Response DTO
    private OwnerResponse mapToResponse(Owner owner) {

        OwnerResponse response = new OwnerResponse();

        response.setId(owner.getId());
        response.setFullName(owner.getFullName());
        response.setPhone(owner.getPhone());
        response.setEmail(owner.getEmail());
        response.setAddress(owner.getAddress());
        response.setCity(owner.getCity());
        response.setState(owner.getState());
        response.setPincode(owner.getPincode());
        response.setEmergencyContact(owner.getEmergencyContact());
        response.setNotes(owner.getNotes());
        response.setCreatedAt(owner.getCreatedAt());
        response.setUpdatedAt(owner.getUpdatedAt());

        return response;
    }
}