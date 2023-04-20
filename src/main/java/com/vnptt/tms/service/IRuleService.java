package com.vnptt.tms.service;

import com.vnptt.tms.dto.RuleDTO;

import java.util.List;

public interface IRuleService {
    List<RuleDTO> findAll();

    RuleDTO save(RuleDTO model);

    void delete(Long[] ids);
}
