package com.vnptt.tms.entity;

import javax.persistence.*;

@Entity
@Table(name = "historyapplication")
public class HistoryApplicationEntity extends BaseCreateDateEntity {
    @Column(name = "cpu")
    private Double cpu;
    @Column(name = "memory")
    private Double memory;

    @Column(name = "main")
    private boolean main;

    @ManyToOne
    @JoinColumn(name = "deviceApplicationId", nullable = false)
    private DeviceApplicationEntity historyDeviceApplicationEntity;

    public Double getCpu() {
        return cpu;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
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


    public DeviceApplicationEntity getHistoryDeviceApplicationEntity() {
        return historyDeviceApplicationEntity;
    }

    public void setHistoryDeviceApplicationEntity(DeviceApplicationEntity historyDeviceApplicationEntity) {
        this.historyDeviceApplicationEntity = historyDeviceApplicationEntity;
    }
}
