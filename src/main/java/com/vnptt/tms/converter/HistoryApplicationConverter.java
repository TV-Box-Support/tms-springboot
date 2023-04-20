package com.vnptt.tms.converter;

import com.vnptt.tms.dto.HistoryApplicationDTO;
import com.vnptt.tms.entity.HistoryApplicationEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HistoryApplicationConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     *  Convert for method Post
     * @param dto
     * @return
     */
    public HistoryApplicationEntity toEntity(HistoryApplicationDTO dto) {
        HistoryApplicationEntity entity = new HistoryApplicationEntity();
        entity = mapper.map(dto, HistoryApplicationEntity.class);
        return entity;
    }

    /**
     *  Convert for method get
     * @param entity
     * @return
     */
    public HistoryApplicationDTO toDTO(HistoryApplicationEntity entity) {
        HistoryApplicationDTO dto = new HistoryApplicationDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, HistoryApplicationDTO.class);
        dto.setApplicationId(entity.getApplicationEntityHistory().getId());
        dto.setDeviceId(entity.getDeviceEntityAppHistory().getId());
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
    public HistoryApplicationEntity toEntity(HistoryApplicationDTO dto, HistoryApplicationEntity entity) {
        entity.setCpu(dto.getCpu());
        entity.setMemory((dto.getMemory()));
        entity.setStatus(dto.isStatus());
        return entity;
    }
}
