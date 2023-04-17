package com.vnptt.tms.dto;

public class DevicePolicyDetailDTO extends AbstractDTO<DevicePolicyDetailDTO>{

    private int status;

    private Long policyId;

    private String deviceSN;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getDeviceSN() {
        return deviceSN;
    }

    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }
}
