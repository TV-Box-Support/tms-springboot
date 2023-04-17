package com.vnptt.tms.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "devicePolicyDetail")
public class DevicePolicyDetailEntity extends BaseEntity{

    @Column(name = "status", nullable = false)
    private int status;

    @ManyToOne
    @JoinColumn(name = "policyId", nullable = false)
    private PolicyEntity policyEntityDetail;

    @ManyToOne
    @JoinColumn(name = "deviceId", nullable = false)
    private DeviceEntity deviceEntityDetail;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PolicyEntity getPolicyEntityDetail() {
        return policyEntityDetail;
    }

    public void setPolicyEntityDetail(PolicyEntity policyEntityDetail) {
        this.policyEntityDetail = policyEntityDetail;
    }

    public DeviceEntity getDeviceEntityDetail() {
        return deviceEntityDetail;
    }

    public void setDeviceEntityDetail(DeviceEntity deviceEntityDetail) {
        this.deviceEntityDetail = deviceEntityDetail;
    }
}
