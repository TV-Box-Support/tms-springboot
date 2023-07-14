package com.vnptt.tms.entity;

import javax.persistence.*;

@Entity
@Table(name = "historyperformance")
public class HistoryPerformanceEntity extends BaseCreateDateEntity {

    @Column(name = "cpu")
    private Double cpu;
    @Column(name = "memory")
    private Double memory;

    @ManyToOne
    @JoinColumn(name = "deviceId", nullable = false)
    private DeviceEntity deviceEntityHistory;

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

    public DeviceEntity getDeviceEntityHistory() {
        return deviceEntityHistory;
    }

    public void setDeviceEntityHistory(DeviceEntity deviceEntityHistory) {
        this.deviceEntityHistory = deviceEntityHistory;
    }

}
