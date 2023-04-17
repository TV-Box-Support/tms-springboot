package com.vnptt.tms.api.output;

import com.vnptt.tms.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class AbstractOutput<T> {
    private int page;
    private int totalPage;
    private List<T> T = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getListResult() {
        return T;
    }

    public void setListResult(List<T> t) {
        T = t;
    }
}
