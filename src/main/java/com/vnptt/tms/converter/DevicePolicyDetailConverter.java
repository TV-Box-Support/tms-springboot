package com.vnptt.tms.converter;

import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import com.vnptt.tms.entity.DevicePolicyDetailEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevicePolicyDetailConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     *  Convert for method Post
     * @param dto
     * @return
     */
    public DevicePolicyDetailEntity toEntity(DevicePolicyDetailDTO dto) {
        DevicePolicyDetailEntity entity = new DevicePolicyDetailEntity();
        entity = mapper.map(dto, DevicePolicyDetailEntity.class);
        return entity;
    }

    /**
     *  Convert for method get
     * @param entity
     * @return
     */
    public DevicePolicyDetailDTO toDTO(DevicePolicyDetailEntity entity) {
        DevicePolicyDetailDTO dto = new DevicePolicyDetailDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, DevicePolicyDetailDTO.class);
        if (entity.getDeviceEntityDetail() != null){
            dto.setDeviceSN(entity.getDeviceEntityDetail().getSn());
        }
        if (entity.getPolicyEntityDetail() != null){
            dto.setPolicyId(entity.getPolicyEntityDetail().getId());
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
    public DevicePolicyDetailEntity toEntity(DevicePolicyDetailDTO dto, DevicePolicyDetailEntity entity) {
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
