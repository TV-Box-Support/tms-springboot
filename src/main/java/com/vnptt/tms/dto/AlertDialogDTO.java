package com.vnptt.tms.dto;

public class AlertDialogDTO extends AbstractDTO {

    private String title;

    private String message;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
