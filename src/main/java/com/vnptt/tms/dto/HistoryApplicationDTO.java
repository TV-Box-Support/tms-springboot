package com.vnptt.tms.dto;

public class HistoryApplicationDTO extends AbstractDTO{

    private Long deviceApplicationId;
    private Double cpu;
    private Double memory;

    public Long getDeviceApplicationId() {
        return deviceApplicationId;
    }

    public void setDeviceApplicationId(Long deviceApplicationId) {
        this.deviceApplicationId = deviceApplicationId;
    }

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

}
