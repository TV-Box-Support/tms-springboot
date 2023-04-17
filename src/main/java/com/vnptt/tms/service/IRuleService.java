package com.vnptt.tms.service;

import com.vnptt.tms.dto.RuleDTO;

import java.util.List;

public interface IRuleService {
    RuleDTO findOne(Long id);
    List<RuleDTO> findAll();

}
