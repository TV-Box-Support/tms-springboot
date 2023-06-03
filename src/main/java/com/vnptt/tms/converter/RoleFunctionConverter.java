package com.vnptt.tms.converter;

import com.vnptt.tms.dto.RoleFunctionDTO;
import com.vnptt.tms.entity.RoleFunctionEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleFunctionConverter {
    public RoleFunctionDTO toDTO(RoleFunctionEntity entity) {
        RoleFunctionDTO dto = new RoleFunctionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName().toString());
        return dto;
    }
}
