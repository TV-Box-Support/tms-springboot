package com.vnptt.tms.dto;

public class DevicePolicyDetailDTO extends AbstractDTO<DevicePolicyDetailDTO>{

    private int status;

    private String policyName;

    private String deviceSN;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getDeviceSN() {
        return deviceSN;
    }

    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }
}
