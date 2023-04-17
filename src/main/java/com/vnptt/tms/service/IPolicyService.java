package com.vnptt.tms.service;

import com.vnptt.tms.dto.PolicyDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPolicyService {
    PolicyDTO save(PolicyDTO policyDTO);
    PolicyDTO findOne(Long id);
    int totalItem();
    void delete(Long[] ids);
    List<PolicyDTO> findAll(Pageable pageable);
    List<PolicyDTO> findAll();
}
