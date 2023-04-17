package com.vnptt.tms.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "historyperformance")
public class HistoryPerformanceEntity extends  BaseEntity{

    @Column(name = "cpu")
    private double cpu;
    @Column(name = "memory")
    private double memory;
    @Column(name = "network")
    private String network;

    @ManyToOne
    @JoinColumn(name ="deviceId", nullable = false)
    private DeviceEntity deviceEntityHistory;

    @OneToMany(mappedBy = "historyPerformanceEntityHistory")
    private List<HistoryApplicationEntity> historyApplicationEntities;

    public List<HistoryApplicationEntity> getHistoryApplicationEntities() {
        return historyApplicationEntities;
    }

    public void setHistoryApplicationEntities(List<HistoryApplicationEntity> historyApplicationEntities) {
        this.historyApplicationEntities = historyApplicationEntities;
    }

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
