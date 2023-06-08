package com.vnptt.tms.api.output.studio;

public class TerminalStudioOutput {
    private Long total;
    private Long online;
    private Long last7day;
    private Long last30day;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }

    public Long getLast7day() {
        return last7day;
    }

    public void setLast7day(Long last7day) {
        this.last7day = last7day;
    }

    public Long getLast30day() {
        return last30day;
    }

    public void setLast30day(Long last30day) {
        this.last30day = last30day;
    }
}
