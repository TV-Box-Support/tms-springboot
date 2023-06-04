package com.vnptt.tms.converter;

import com.vnptt.tms.dto.ListDeviceDTO;
import com.vnptt.tms.entity.ListDeviceEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListDeviceConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     * Convert for method Post
     * for future upgrades.
     *
     * @param dto
     * @return
     */
    public ListDeviceEntity toEntity(ListDeviceDTO dto) {
        ListDeviceEntity entity = new ListDeviceEntity();
        entity = mapper.map(dto, ListDeviceEntity.class);
        return entity;
    }

    /**
     * Convert for mehtod get
     *
     * @param entity
     * @return
     */
    public ListDeviceDTO toDTO(ListDeviceEntity entity) {
        ListDeviceDTO dto = new ListDeviceDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, ListDeviceDTO.class);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        return dto;
    }

    /**
     * Convert for method put
     * for future upgrades.
     *
     * @param dto
     * @param entity
     * @return
     */
    public ListDeviceEntity toEntity(ListDeviceDTO dto, ListDeviceEntity entity) {
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        return entity;
    }
}
