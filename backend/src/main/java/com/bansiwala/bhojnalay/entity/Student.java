package com.bansiwala.bhojnalay.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 15)
    private String mobile;

    @Column(length = 100)
    private String email;

    @Column(name = "qr_code_uuid", unique = true, nullable = false)
    private UUID qrCodeUuid;

    @Column(name = "fingerprint_template", columnDefinition = "TEXT")
    private String fingerprintTemplate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "amount_paid", nullable = false)
    private double amountPaid = 0;

    @Column(name = "payment_cleared", nullable = false)
    private boolean paymentCleared = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Student() {}

    public Student(Long id, String name, String mobile, String email, UUID qrCodeUuid,
                   String fingerprintTemplate, Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.qrCodeUuid = qrCodeUuid;
        this.fingerprintTemplate = fingerprintTemplate;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (qrCodeUuid == null) qrCodeUuid = UUID.randomUUID();
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UUID getQrCodeUuid() { return qrCodeUuid; }
    public void setQrCodeUuid(UUID qrCodeUuid) { this.qrCodeUuid = qrCodeUuid; }

    public String getFingerprintTemplate() { return fingerprintTemplate; }
    public void setFingerprintTemplate(String fingerprintTemplate) { this.fingerprintTemplate = fingerprintTemplate; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public boolean isPaymentCleared() { return paymentCleared; }
    public void setPaymentCleared(boolean paymentCleared) { this.paymentCleared = paymentCleared; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
