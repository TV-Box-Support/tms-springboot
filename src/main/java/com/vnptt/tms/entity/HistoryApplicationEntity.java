package com.vnptt.tms.entity;

import javax.persistence.*;

@Entity
@Table(name = "historyapplication")
public class HistoryApplicationEntity extends BaseEntity {
    @Column(name = "cpu")
    private Double cpu;
    @Column(name = "memory")
    private Double memory;
    @Column(name = "status", nullable = false)
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "deviceApplicationId", nullable = false)
    private DeviceApplicationEntity historyDeviceApplicationEntity;

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public DeviceApplicationEntity getHistoryDeviceApplicationEntity() {
        return historyDeviceApplicationEntity;
    }

    public void setHistoryDeviceApplicationEntity(DeviceApplicationEntity historyDeviceApplicationEntity) {
        this.historyDeviceApplicationEntity = historyDeviceApplicationEntity;
    }
}
