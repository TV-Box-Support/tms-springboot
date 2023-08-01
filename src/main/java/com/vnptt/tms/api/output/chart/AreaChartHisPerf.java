package com.vnptt.tms.api.output.chart;

import java.time.LocalDateTime;

public class AreaChartHisPerf {
    private LocalDateTime date;
    private Double cpu;
    private Double memory;

    public AreaChartHisPerf(LocalDateTime date, Double cpu, Double memory) {
        this.date = date;
        this.cpu = cpu;
        this.memory = memory;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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
