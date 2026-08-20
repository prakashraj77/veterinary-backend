package doctor.backend.repository;

import doctor.backend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Find by invoice number
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    // Find all invoices for an owner
    List<Invoice> findByOwnerId(Long ownerId);

    // Find all invoices for a patient
    List<Invoice> findByPatientId(Long patientId);

    // Find by invoice status
    List<Invoice> findByStatus(String status);

    // Find by payment status
    List<Invoice> findByPaymentStatus(String paymentStatus);

    // Find owner invoices by payment status
    List<Invoice> findByOwnerIdAndPaymentStatus(
            Long ownerId,
            String paymentStatus
    );

    // Find patient invoices by payment status
    List<Invoice> findByPatientIdAndPaymentStatus(
            Long patientId,
            String paymentStatus
    );

    // Check duplicate invoice number
    boolean existsByInvoiceNumber(String invoiceNumber);
}