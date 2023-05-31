package com.vnptt.tms.dto;

public class DeviceApplicationDTO extends AbstractDTO {
    private Boolean isalive;
    private Long deviceId;
    private Long applicationId;

    public Boolean getIsalive() {
        return isalive;
    }

    public void setIsalive(Boolean isalive) {
        this.isalive = isalive;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
}
