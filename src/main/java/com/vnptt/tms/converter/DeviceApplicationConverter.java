package com.vnptt.tms.converter;

import com.vnptt.tms.dto.DeviceApplicationDTO;
import com.vnptt.tms.entity.DeviceApplicationEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceApplicationConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     * Convert for method Post
     * for future upgrades.
     *
     * @param dto
     * @return
     */
    public DeviceApplicationEntity toEntity(DeviceApplicationDTO dto) {
        DeviceApplicationEntity entity = new DeviceApplicationEntity();
        entity = mapper.map(dto, DeviceApplicationEntity.class);

        return entity;
    }

    /**
     * Convert for mehtod get
     *
     * @param entity
     * @return
     */
    public DeviceApplicationDTO toDTO(DeviceApplicationEntity entity) {
        DeviceApplicationDTO dto = new DeviceApplicationDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, DeviceApplicationDTO.class);
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
    public DeviceApplicationEntity toEntity(DeviceApplicationDTO dto, DeviceApplicationEntity entity) {

        return entity;

    }
}
