package com.vnptt.tms.entity;

import javax.persistence.*;

@Entity
@Table(name = "historyapplication")
public class HistoryApplicationEntity extends BaseEntity {
    @Column(name = "cpu")
    private double cpu;
    @Column(name = "memory")
    private double memory;
    @Column(name = "status", nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = false)
    private ApplicationEntity applicationEntityHistory;

    @ManyToOne
    @JoinColumn(name = "deviceId", nullable = false)
    private DeviceEntity deviceEntityAppHistory;

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ApplicationEntity getApplicationEntityHistory() {
        return applicationEntityHistory;
    }

    public void setApplicationEntityHistory(ApplicationEntity applicationEntityHistory) {
        this.applicationEntityHistory = applicationEntityHistory;
    }

    public DeviceEntity getDeviceEntityAppHistory() {
        return deviceEntityAppHistory;
    }

    public void setDeviceEntityAppHistory(DeviceEntity deviceEntityAppHistory) {
        this.deviceEntityAppHistory = deviceEntityAppHistory;
    }
}
