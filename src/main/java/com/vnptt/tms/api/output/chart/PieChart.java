package com.vnptt.tms.api.output.chart;

/**
 *  PieChart
 *              60*            120*
 *            *********     ***********
 *        ******   \           *********
 *      ***         \                ****
 *     **            \                 **
 *    *       20%     \        30%       *
 *   *                 \                  *
 *  *-------------------*------------------*
 *   *                                    *
 *    *            A - 50%               *
 *     **                              **
 *      ***                          ****
 *        ******               *********
 *            *********     ***********
 *
 *      name: A
 *      value: 50.00
 */
public class PieChart {
    private Long value;
    String name;

    public PieChart(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
