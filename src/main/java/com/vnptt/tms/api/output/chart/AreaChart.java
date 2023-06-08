package com.vnptt.tms.api.output.chart;

import java.time.LocalDate;

public class AreaChart {
    private LocalDate date;
    private Long devicenumber;

    public AreaChart(LocalDate date, Long devicenumber) {
        this.date = date;
        this.devicenumber = devicenumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getDevicenumber() {
        return devicenumber;
    }

    public void setDevicenumber(Long devicenumber) {
        this.devicenumber = devicenumber;
    }
}
