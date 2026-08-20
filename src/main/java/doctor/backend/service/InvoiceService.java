package doctor.backend.service;

import doctor.backend.dto.invoice.InvoiceRequest;
import doctor.backend.dto.invoice.InvoiceResponse;
import doctor.backend.entity.Invoice;
import doctor.backend.repository.InvoiceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    // =====================================================
    // CREATE INVOICE
    // =====================================================

    public InvoiceResponse createInvoice(InvoiceRequest request) {

        if (request.getOwnerId() == null) {
            throw new RuntimeException("Owner ID is required");
        }

        if (request.getPatientId() == null) {
            throw new RuntimeException("Patient ID is required");
        }

        BigDecimal subtotal = valueOrZero(request.getSubtotal());
        BigDecimal tax = valueOrZero(request.getTax());
        BigDecimal discount = valueOrZero(request.getDiscount());
        BigDecimal paidAmount = valueOrZero(request.getPaidAmount());

        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Subtotal cannot be negative");
        }

        if (tax.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Tax cannot be negative");
        }

        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Discount cannot be negative");
        }

        if (paidAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Paid amount cannot be negative");
        }

        BigDecimal totalAmount =
                subtotal
                        .add(tax)
                        .subtract(discount);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                    "Total amount cannot be negative"
            );
        }

        if (paidAmount.compareTo(totalAmount) > 0) {
            throw new RuntimeException(
                    "Paid amount cannot be greater than total amount"
            );
        }

        BigDecimal dueAmount =
                totalAmount.subtract(paidAmount);

        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(
                generateInvoiceNumber()
        );

        invoice.setOwnerId(
                request.getOwnerId()
        );

        invoice.setPatientId(
                request.getPatientId()
        );

        invoice.setInvoiceDate(
                request.getInvoiceDate() != null
                        ? request.getInvoiceDate()
                        : LocalDate.now()
        );

        invoice.setDueDate(
                request.getDueDate()
        );

        invoice.setSubtotal(subtotal);
        invoice.setTax(tax);
        invoice.setDiscount(discount);
        invoice.setTotalAmount(totalAmount);
        invoice.setPaidAmount(paidAmount);
        invoice.setDueAmount(dueAmount);

        invoice.setPaymentStatus(
                calculatePaymentStatus(
                        totalAmount,
                        paidAmount
                )
        );

        invoice.setStatus("ACTIVE");

        invoice.setNotes(
                request.getNotes()
        );

        Invoice saved =
                invoiceRepository.save(invoice);

        return mapToResponse(saved);
    }

    // =====================================================
    // GET ALL INVOICES
    // =====================================================

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {

        return invoiceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET INVOICE BY ID
    // =====================================================

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long id) {

        Invoice invoice =
                invoiceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invoice not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(invoice);
    }

    // =====================================================
    // GET BY INVOICE NUMBER
    // =====================================================

    @Transactional(readOnly = true)
    public InvoiceResponse getByInvoiceNumber(
            String invoiceNumber) {

        Invoice invoice =
                invoiceRepository
                        .findByInvoiceNumber(invoiceNumber)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invoice not found: "
                                                + invoiceNumber
                                )
                        );

        return mapToResponse(invoice);
    }

    // =====================================================
    // GET BY OWNER
    // =====================================================

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getByOwnerId(
            Long ownerId) {

        return invoiceRepository
                .findByOwnerId(ownerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY PATIENT
    // =====================================================

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getByPatientId(
            Long patientId) {

        return invoiceRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY STATUS
    // =====================================================

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getByStatus(
            String status) {

        return invoiceRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET BY PAYMENT STATUS
    // =====================================================

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getByPaymentStatus(
            String paymentStatus) {

        return invoiceRepository
                .findByPaymentStatus(paymentStatus)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET OWNER + PAYMENT STATUS
    // =====================================================

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getByOwnerAndPaymentStatus(
            Long ownerId,
            String paymentStatus) {

        return invoiceRepository
                .findByOwnerIdAndPaymentStatus(
                        ownerId,
                        paymentStatus
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // GET PATIENT + PAYMENT STATUS
    // =====================================================

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getByPatientAndPaymentStatus(
            Long patientId,
            String paymentStatus) {

        return invoiceRepository
                .findByPatientIdAndPaymentStatus(
                        patientId,
                        paymentStatus
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // UPDATE PAYMENT
    // =====================================================

    public InvoiceResponse updatePayment(
            Long id,
            BigDecimal paidAmount) {

        if (paidAmount == null) {
            throw new RuntimeException(
                    "Paid amount is required"
            );
        }

        if (paidAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                    "Paid amount cannot be negative"
            );
        }

        Invoice invoice =
                invoiceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invoice not found with id: "
                                                + id
                                )
                        );

        if (paidAmount.compareTo(
                invoice.getTotalAmount()) > 0) {

            throw new RuntimeException(
                    "Paid amount cannot be greater than total amount"
            );
        }

        invoice.setPaidAmount(paidAmount);

        invoice.setDueAmount(
                invoice.getTotalAmount()
                        .subtract(paidAmount)
        );

        invoice.setPaymentStatus(
                calculatePaymentStatus(
                        invoice.getTotalAmount(),
                        paidAmount
                )
        );

        Invoice updated =
                invoiceRepository.save(invoice);

        return mapToResponse(updated);
    }

    // =====================================================
    // CANCEL INVOICE
    // =====================================================

    public InvoiceResponse cancelInvoice(Long id) {

        Invoice invoice =
                invoiceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invoice not found with id: "
                                                + id
                                )
                        );

        invoice.setStatus("CANCELLED");

        Invoice updated =
                invoiceRepository.save(invoice);

        return mapToResponse(updated);
    }

    // =====================================================
    // DELETE INVOICE
    // =====================================================

    public void deleteInvoice(Long id) {

        if (!invoiceRepository.existsById(id)) {

            throw new RuntimeException(
                    "Invoice not found with id: " + id
            );
        }

        invoiceRepository.deleteById(id);
    }

    // =====================================================
    // GENERATE INVOICE NUMBER
    // =====================================================

    private String generateInvoiceNumber() {

        String invoiceNumber;

        do {
            invoiceNumber =
                    "INV-" +
                            System.currentTimeMillis();

        } while (
                invoiceRepository
                        .existsByInvoiceNumber(
                                invoiceNumber
                        )
        );

        return invoiceNumber;
    }

    // =====================================================
    // PAYMENT STATUS
    // =====================================================

    private String calculatePaymentStatus(
            BigDecimal totalAmount,
            BigDecimal paidAmount) {

        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            return "UNPAID";
        }

        if (paidAmount.compareTo(totalAmount) >= 0) {
            return "PAID";
        }

        return "PARTIALLY_PAID";
    }

    // =====================================================
    // NULL → ZERO
    // =====================================================

    private BigDecimal valueOrZero(BigDecimal value) {

        return value != null
                ? value
                : BigDecimal.ZERO;
    }

    // =====================================================
    // ENTITY → RESPONSE
    // =====================================================

    private InvoiceResponse mapToResponse(
            Invoice invoice) {

        InvoiceResponse response =
                new InvoiceResponse();

        response.setId(
                invoice.getId()
        );

        response.setInvoiceNumber(
                invoice.getInvoiceNumber()
        );

        response.setOwnerId(
                invoice.getOwnerId()
        );

        response.setPatientId(
                invoice.getPatientId()
        );

        response.setInvoiceDate(
                invoice.getInvoiceDate()
        );

        response.setDueDate(
                invoice.getDueDate()
        );

        response.setSubtotal(
                invoice.getSubtotal()
        );

        response.setTax(
                invoice.getTax()
        );

        response.setDiscount(
                invoice.getDiscount()
        );

        response.setTotalAmount(
                invoice.getTotalAmount()
        );

        response.setPaidAmount(
                invoice.getPaidAmount()
        );

        response.setDueAmount(
                invoice.getDueAmount()
        );

        response.setStatus(
                invoice.getStatus()
        );

        response.setPaymentStatus(
                invoice.getPaymentStatus()
        );

        response.setNotes(
                invoice.getNotes()
        );

        response.setCreatedAt(
                invoice.getCreatedAt()
        );

        response.setUpdatedAt(
                invoice.getUpdatedAt()
        );

        return response;
    }
}