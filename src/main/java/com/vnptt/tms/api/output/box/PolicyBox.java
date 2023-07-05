package com.vnptt.tms.api.output.box;

import com.vnptt.tms.dto.PolicyDTO;

public class PolicyBox extends PolicyDTO {
    private Long idPolicyDetail;

    public Long getIdPolicyDetail() {
        return idPolicyDetail;
    }

    public void setIdPolicyDetail(Long idPolicyDetail) {
        this.idPolicyDetail = idPolicyDetail;
    }
}
