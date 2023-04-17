package com.vnptt.tms.converter;

import com.vnptt.tms.dto.HistoryPerformanceDTO;
import com.vnptt.tms.entity.HistoryPerformanceEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HistoryPerformanceConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     *  Convert for method Post
     * @param dto
     * @return
     */
    public HistoryPerformanceEntity toEntity(HistoryPerformanceDTO dto) {
        HistoryPerformanceEntity entity = new HistoryPerformanceEntity();
        entity = mapper.map(dto, HistoryPerformanceEntity.class);
        return entity;
    }

    /**
     *  Convert for mehtod get
     * @param entity
     * @return
     */
    public HistoryPerformanceDTO toDTO(HistoryPerformanceEntity entity) {
        HistoryPerformanceDTO dto = new HistoryPerformanceDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, HistoryPerformanceDTO.class);
        if (entity.getDeviceEntityHistory() != null) {
            dto.setDevicesn(entity.getDeviceEntityHistory().getSn());
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
    public HistoryPerformanceEntity toEntity(HistoryPerformanceDTO dto, HistoryPerformanceEntity entity) {
        entity.setCpu(dto.getCpu());
        entity.setMemory((dto.getMemory()));
        entity.setNetwork(dto.getNetwork());
        return entity;
    }
}
