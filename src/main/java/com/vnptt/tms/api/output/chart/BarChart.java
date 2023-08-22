package com.vnptt.tms.api.output.chart;

import java.time.LocalDate;

/**
 * BarChart (horizontal column)
 *
 *  time ^
 *       |
 *  100 - ██████████████████████████████████
 *       |
 *   80 - ████████████
 *       |
 *   60 - ██████████████████████
 *       |
 *   40 - ████████████████████████████
 *       |
 *   20 - █████████████████
 *       |
 *    0 - ████████████████████████████████████████████████████████
 *       0         10        20        30        40        50       devicenumber
 */
public class BarChart {
    private LocalDate date;
    private Long devicenumber;

    public BarChart(LocalDate date, Long devicenumber) {
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
