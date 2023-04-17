package com.vnptt.tms.converter;

import com.vnptt.tms.dto.ApkDTO;
import com.vnptt.tms.entity.ApkEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApkConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     *  Convert for method Post
     * @param dto
     * @return
     */
    public ApkEntity toEntity(ApkDTO dto) {
        ApkEntity entity = new ApkEntity();
        entity = mapper.map(dto, ApkEntity.class);
        return entity;
    }

    /**
     *  Convert for mehtod get
     * @param entity
     * @return
     */
    public ApkDTO toDTO(ApkEntity entity) {
        ApkDTO dto = new ApkDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, ApkDTO.class);
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
    public ApkEntity toEntity(ApkDTO dto, ApkEntity entity) {
        entity.setPackagename(dto.getPackagename());
        entity.setVersion(dto.getVersion());
        entity.setApkfileUrl(dto.getApkfileUrl());
        entity.setMd5(dto.getMd5());
        entity.setPackagesize(dto.getPackagesize());
        return entity;
    }
}
