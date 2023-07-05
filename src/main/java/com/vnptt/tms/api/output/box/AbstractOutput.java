package com.vnptt.tms.api.output.box;

import java.util.ArrayList;
import java.util.List;

/**
 * The main return pattern for information returned is a list
 *
 * @param <T> T is object has extended this class
 */
public class AbstractOutput<T> {
    private Integer totalElement;
    private String message;
    private List<T> listResult = new ArrayList<>();

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
