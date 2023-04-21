package com.vnptt.tms.converter;

import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.entity.DeviceEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     * Convert for method Post
     *
     * @param dto
     * @return
     */
    public DeviceEntity toEntity(DeviceDTO dto) {
        DeviceEntity entity = new DeviceEntity();
        entity = mapper.map(dto, DeviceEntity.class);
        return entity;
    }

    /**
     * Convert for mehtod get
     *
     * @param entity
     * @return
     */
    public DeviceDTO toDTO(DeviceEntity entity) {
        DeviceDTO dto = new DeviceDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, DeviceDTO.class);
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
    public DeviceEntity toEntity(DeviceDTO dto, DeviceEntity entity) {
        entity.setDesciption(dto.getDesciption());
        entity.setHdmi(dto.getHdmi());
        entity.setModel(dto.getModel());
        entity.setFirmwareVer(dto.getFirmwareVer());
        entity.setIp(dto.getIp());
        entity.setLocation(dto.getLocation());
        entity.setProduct(dto.getProduct());
//        unnecessary remove to reduce error
//        entity.setDate(dto.getDate());
//        entity.setSn(dto.getSn());
//        entity.setMac(dto.getMac());
        return entity;
    }
}
