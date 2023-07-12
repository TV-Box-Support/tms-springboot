package com.vnptt.tms.converter;

import com.vnptt.tms.dto.AlertDialogDTO;
import com.vnptt.tms.entity.AlertDialogEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertDialogConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     * Convert for method Post
     *
     * @param dto
     * @return
     */
    public AlertDialogEntity toEntity(AlertDialogDTO dto) {
        AlertDialogEntity entity = new AlertDialogEntity();
        entity = mapper.map(dto, AlertDialogEntity.class);
        return entity;
    }

    /**
     * Convert for mehtod get
     *
     * @param entity
     * @return
     */
    public AlertDialogDTO toDTO(AlertDialogEntity entity) {
        AlertDialogDTO dto = new AlertDialogDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, AlertDialogDTO.class);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getCreatedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        return dto;
    }

    /**
     * Convert for method put
     *
     * @param dto
     * @param entity
     * @return
     */
    public AlertDialogEntity toEntity(AlertDialogDTO dto, AlertDialogEntity entity) {
        if (dto.getMessage() != null)
            entity.setMessage(dto.getMessage());
        if (dto.getTitle() != null)
            entity.setTitle(dto.getTitle());
        return entity;
    }
}
