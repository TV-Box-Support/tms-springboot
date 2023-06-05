package com.vnptt.tms.converter;

import com.vnptt.tms.dto.RoleManagementDTO;
import com.vnptt.tms.entity.RoleManagementEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleManagementConverter {
    @Autowired
    private ModelMapper mapper;

    public RoleManagementDTO toDTO(RoleManagementEntity entity) {
        RoleManagementDTO dto = new RoleManagementDTO();
        dto = mapper.map(entity, RoleManagementDTO.class);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        return dto;
    }

    public RoleManagementEntity toEntity(RoleManagementDTO dto) {
        RoleManagementEntity entity = new RoleManagementEntity();
        entity = mapper.map(dto, RoleManagementEntity.class);
        return entity;
    }

    /**
     * Convert for method put
     *
     * @param dto
     * @param entity
     * @return
     */
    public RoleManagementEntity toEntity(RoleManagementDTO dto, RoleManagementEntity entity) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        return entity;
    }
}
