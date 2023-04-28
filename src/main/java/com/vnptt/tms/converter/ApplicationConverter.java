package com.vnptt.tms.converter;

import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.entity.ApplicationEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     * Convert for method Post
     *
     * @param dto
     * @return
     */
    public ApplicationEntity toEntity(ApplicationDTO dto) {
        ApplicationEntity entity = new ApplicationEntity();
        entity = mapper.map(dto, ApplicationEntity.class);

        return entity;
    }

    /**
     * Convert for mehtod get
     *
     * @param entity
     * @return
     */
    public ApplicationDTO toDTO(ApplicationEntity entity) {
        ApplicationDTO dto = new ApplicationDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, ApplicationDTO.class);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getModifiedDate());
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
    public ApplicationEntity toEntity(ApplicationDTO dto, ApplicationEntity entity) {
        entity.setPackagename(dto.getPackagename());
        entity.setVersion(dto.getVersion());
        return entity;

    }
}
