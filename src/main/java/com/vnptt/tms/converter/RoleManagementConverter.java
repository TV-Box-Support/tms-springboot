package com.vnptt.tms.converter;

import com.vnptt.tms.dto.RoleManagementDTO;
import com.vnptt.tms.entity.RoleManagementEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleManagementConverter {
    public RoleManagementDTO toDTO(RoleManagementEntity entity) {
        RoleManagementDTO dto = new RoleManagementDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName().toString());
        return dto;
    }
}
