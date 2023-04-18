package com.vnptt.tms.api.output;

import com.vnptt.tms.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class AbstractOutput<T> {
    private int page;
    private int totalPage;
    private Integer totalElement;
    private String message;
    private List<T> listResult = new ArrayList<>();

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(Integer totalElement) {
        this.totalElement = totalElement;
    }

    public List<T> getListResult() {
        return listResult;
    }

    public void setListResult(List<T> listResult) {
        this.listResult = listResult;
    }
}
