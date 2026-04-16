package com.bansiwala.bhojnalay.dto;

import com.bansiwala.bhojnalay.entity.Student;
import java.time.LocalDateTime;
import java.util.UUID;

public class StudentResponse {
    private Long id;
    private String name;
    private String mobile;
    private String email;
    private UUID qrCodeUuid;
    private Boolean hasFingerprintRegistered;
    private Boolean isActive;
    private double amountPaid;
    private int thalisCleared;
    private LocalDateTime createdAt;

    public static StudentResponse from(Student student) {
        StudentResponse dto = new StudentResponse();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setMobile(student.getMobile());
        dto.setEmail(student.getEmail());
        dto.setQrCodeUuid(student.getQrCodeUuid());
        dto.setHasFingerprintRegistered(
            student.getFingerprintTemplate() != null && !student.getFingerprintTemplate().isBlank());
        dto.setIsActive(student.getIsActive());
        dto.setAmountPaid(student.getAmountPaid());
        dto.setThalisCleared(student.getThalisCleared());
        dto.setCreatedAt(student.getCreatedAt());
        return dto;
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

    public Boolean getHasFingerprintRegistered() { return hasFingerprintRegistered; }
    public void setHasFingerprintRegistered(Boolean hasFingerprintRegistered) { this.hasFingerprintRegistered = hasFingerprintRegistered; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public int getThalisCleared() { return thalisCleared; }
    public void setThalisCleared(int thalisCleared) { this.thalisCleared = thalisCleared; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
