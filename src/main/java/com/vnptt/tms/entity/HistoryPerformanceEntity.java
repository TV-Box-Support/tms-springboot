package com.vnptt.tms.entity;

import javax.persistence.*;

@Entity
@Table(name = "historyperformance")
public class HistoryPerformanceEntity extends BaseEntity {

    @Column(name = "cpu")
    private Double cpu;
    @Column(name = "memory")
    private Double memory;
    @Column(name = "network")
    private String network;

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

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public DeviceEntity getDeviceEntityHistory() {
        return deviceEntityHistory;
    }

    public void setDeviceEntityHistory(DeviceEntity deviceEntityHistory) {
        this.deviceEntityHistory = deviceEntityHistory;
    }

}
