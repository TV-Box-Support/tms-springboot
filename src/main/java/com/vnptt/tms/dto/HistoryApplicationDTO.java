package com.vnptt.tms.dto;

import java.sql.Timestamp;

public class HistoryApplicationDTO extends AbstractDTO<HistoryApplicationDTO>{

    private Long applicationId;
    private Long historyPerId;
    private double cpu;
    private double memory;
    private boolean status;

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getHistoryPerId() {
        return historyPerId;
    }

    public void setHistoryPerId(Long historyPerId) {
        this.historyPerId = historyPerId;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
