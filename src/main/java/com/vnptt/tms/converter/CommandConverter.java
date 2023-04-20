package com.vnptt.tms.converter;

import com.vnptt.tms.dto.CommandDTO;
import com.vnptt.tms.entity.CommandEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     *  Convert for method Post
     * @param dto
     * @return
     */
    public CommandEntity toEntity(CommandDTO dto) {
        CommandEntity entity = new CommandEntity();
        entity = mapper.map(dto, CommandEntity.class);
        return entity;
    }

    /**
     *  Convert for mehtod get
     * @param entity
     * @return
     */
    public CommandDTO toDTO(CommandEntity entity) {
        CommandDTO dto = new CommandDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, CommandDTO.class);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getCreatedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        return dto;
    }

    /**
     *  Convert for method put
     * @param dto
     * @param entity
     * @return
     */
    public CommandEntity toEntity(CommandDTO dto, CommandEntity entity) {
        entity.setCommand(dto.getCommand());
        return entity;
    }
}
