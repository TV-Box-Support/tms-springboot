package com.vnptt.tms.dto;


public class HistoryPerformanceDTO extends AbstractDTO {

    private String devicesn;
    private Double cpu;
    private Double memory;
    private Double network;

    public String getDevicesn() {
        return devicesn;
    }

    public void setDevicesn(String devicesn) {
        this.devicesn = devicesn;
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

    public Double getNetwork() {
        return network;
    }

    public void setNetwork(Double network) {
        this.network = network;
    }
}
