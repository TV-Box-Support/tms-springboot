package com.vnptt.tms.converter;

import com.vnptt.tms.dto.PolicyDTO;
import com.vnptt.tms.entity.PolicyEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PolicyConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     *  Convert for method Post
     * @param dto
     * @return
     */
    public PolicyEntity toEntity(PolicyDTO dto) {
        PolicyEntity entity = new PolicyEntity();
        entity = mapper.map(dto, PolicyEntity.class);
        return entity;
    }

    /**
     *  Convert for mehtod get
     * @param entity
     * @return
     */
    public PolicyDTO toDTO(PolicyEntity entity) {
        PolicyDTO dto = new PolicyDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, PolicyDTO.class);
        if (entity.getCommandEntity() != null) {
            dto.setCommandName(entity.getCommandEntity().getCommand());
        }
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        return dto;
    }

    /**
     *  Convert for method put
     * @param dto
     * @param entity
     * @return
     */
    public PolicyEntity toEntity(PolicyDTO dto, PolicyEntity entity) {
        entity.setPolicyname(dto.getPolicyname());
        //not use
        //entity.setStatus(dto.getStatus());
        return entity;
    }
}
