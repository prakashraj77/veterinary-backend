package doctor.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor_profile")
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String qualification;

    private String councilRegistration;

    private String clinicHospital;

    private String phone;

    private String email;

    private String digitalSignatureName;

    private Double consultationFee;

    private Double followUpFee;

    private Integer slotLength;

    public DoctorProfile() {
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getCouncilRegistration() {
        return councilRegistration;
    }

    public void setCouncilRegistration(String councilRegistration) {
        this.councilRegistration = councilRegistration;
    }

    public String getClinicHospital() {
        return clinicHospital;
    }

    public void setClinicHospital(String clinicHospital) {
        this.clinicHospital = clinicHospital;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDigitalSignatureName() {
        return digitalSignatureName;
    }

    public void setDigitalSignatureName(String digitalSignatureName) {
        this.digitalSignatureName = digitalSignatureName;
    }

    public Double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public Double getFollowUpFee() {
        return followUpFee;
    }

    public void setFollowUpFee(Double followUpFee) {
        this.followUpFee = followUpFee;
    }

    public Integer getSlotLength() {
        return slotLength;
    }

    public void setSlotLength(Integer slotLength) {
        this.slotLength = slotLength;
    }
}