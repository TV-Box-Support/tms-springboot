package com.vnptt.tms.converter;

import com.vnptt.tms.dto.RuleDTO;
import com.vnptt.tms.entity.RuleEntity;
import org.springframework.stereotype.Component;

@Component
public class RuleConverter {
    public RuleDTO toDTO(RuleEntity entity) {
        RuleDTO dto = new RuleDTO();
        if(entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto.setName(entity.getName());
        return dto;
    }
}
