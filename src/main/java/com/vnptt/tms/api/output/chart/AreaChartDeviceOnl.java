package com.vnptt.tms.api.output.chart;

import java.time.LocalDate;

/**
 * AreaChart
 *
 * Value^
 *      |
 * 100 -|                                      *
 *      |                                      |
 *  80 -|                                  *   |
 *      |                                 |   *|
 *  60 -|           *                  *  |    |
 *      |           |                 *   |    |
 *  40 -|          *|*            *    |     |
 *      |          |*|            |    *     |
 *  20 -|*         | |   *        |     |     |
 *      | *        | |    *       |     |    *
 *   0 -|__*_______|_|_____*______|_____*_____|___________________>
 *      0         10        20       30    40   50                  Time
 *
 *      Time: date
 *      Value: value
 *
 */
public class AreaChartDeviceOnl {
    private LocalDate date;
    private Long value;

    public AreaChartDeviceOnl(LocalDate date, Long value) {
        this.date = date;
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
