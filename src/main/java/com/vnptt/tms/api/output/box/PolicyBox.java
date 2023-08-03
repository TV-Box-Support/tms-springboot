package com.vnptt.tms.api.output.box;

import com.vnptt.tms.dto.PolicyDTO;

public class PolicyBox extends PolicyDTO {
    private Long idPolicyDetail;
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

    public Long getIdPolicyDetail() {
        return idPolicyDetail;
    }

    public void setIdPolicyDetail(Long idPolicyDetail) {
        this.idPolicyDetail = idPolicyDetail;
    }
}
