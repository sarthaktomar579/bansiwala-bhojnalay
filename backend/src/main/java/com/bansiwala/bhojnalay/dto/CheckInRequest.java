package com.bansiwala.bhojnalay.dto;

import com.bansiwala.bhojnalay.enums.CheckInMethod;
import com.bansiwala.bhojnalay.enums.MealType;

public class CheckInRequest {
    private String qrCodeUuid;
    private String fingerprintTemplate;
    private MealType mealType;
    private CheckInMethod checkInMethod;

    public String getQrCodeUuid() { return qrCodeUuid; }
    public void setQrCodeUuid(String qrCodeUuid) { this.qrCodeUuid = qrCodeUuid; }

    public String getFingerprintTemplate() { return fingerprintTemplate; }
    public void setFingerprintTemplate(String fingerprintTemplate) { this.fingerprintTemplate = fingerprintTemplate; }

    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }

    public CheckInMethod getCheckInMethod() { return checkInMethod; }
    public void setCheckInMethod(CheckInMethod checkInMethod) { this.checkInMethod = checkInMethod; }
}
