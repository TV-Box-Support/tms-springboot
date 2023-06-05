package com.vnptt.tms.converter;

import com.vnptt.tms.dto.RolesDTO;
import com.vnptt.tms.entity.RolesEntity;
import org.springframework.stereotype.Component;

@Component
public class RolesConverter {
    public RolesDTO toDTO(RolesEntity entity) {
        RolesDTO dto = new RolesDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName().toString());
        return dto;
    }
}
