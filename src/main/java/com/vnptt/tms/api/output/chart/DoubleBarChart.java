package com.vnptt.tms.api.output.chart;

/**
 * Double BarChart ((horizontal column))
 *
 * time ^
 *  100 | ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄   value 1
 *  100 - █████████████████      value 2
 *   80 | ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄
 *   80 - █████████████████████████
 *   60 | ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄
 *   60 - ██████████████████████████████████████████
 *   40 | ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄
 *   40 - █████████
 *   20 | ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄
 *   20 - █████████████████████████████████████████████████
 *    0 | ▄▄▄▄▄▄▄▄▄▄▄
 *    0 - ████████████████████████████████████████████████████████
 *       0         10        20        30        40        50       value
 */
public class DoubleBarChart {
    private String name;
    private Long value1;
    private Long value2;

    public DoubleBarChart(String name, Long value1, Long value2) {
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue1() {
        return value1;
    }

    public void setValue1(Long value1) {
        this.value1 = value1;
    }

    public Long getValue2() {
        return value2;
    }

    public void setValue2(Long value2) {
        this.value2 = value2;
    }
}
