package com.vnptt.tms.converter;

import com.vnptt.tms.api.output.box.PolicyBox;
import com.vnptt.tms.dto.PolicyDTO;
import com.vnptt.tms.entity.PolicyEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PolicyConverter {

    @Autowired
    private ModelMapper mapper;

    /**
     * Convert for method Post
     *
     * @param dto
     * @return
     */
    public PolicyEntity toEntity(PolicyDTO dto) {
        PolicyEntity entity = new PolicyEntity();
        entity = mapper.map(dto, PolicyEntity.class);
        return entity;
    }

    /**
     * Convert for mehtod get
     *
     * @param entity
     * @return
     */
    public PolicyDTO toDTO(PolicyEntity entity) {
        PolicyDTO dto = new PolicyDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto = mapper.map(entity, PolicyDTO.class);
        if (entity.getCommandEntity() != null) {
            dto.setCommandName(entity.getCommandEntity().getName());
        }
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        return dto;
    }

    public PolicyBox toPolicyBox(PolicyEntity entity, Long policyDetalId) {
        PolicyBox dto = new PolicyBox();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        if (entity.getCommandEntity() != null) {
            dto.setCommandName(entity.getCommandEntity().getCommand());
            if(Objects.equals(entity.getCommandEntity().getCommand(), "Notification")){
                dto.setTitle(entity.getCommandEntity().getAlertDialogEntity().getTitle());
                dto.setMessage(entity.getCommandEntity().getAlertDialogEntity().getMessage());
            }
        }
        dto.setIdPolicyDetail(policyDetalId);
        dto.setPolicyname(entity.getPolicyname());
        dto.setAction(entity.getAction());
        dto.setStatus(entity.getStatus());

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
    public PolicyEntity toEntity(PolicyDTO dto, PolicyEntity entity) {
        if (dto.getPolicyname() != null) {
            entity.setPolicyname(dto.getPolicyname());
        }
        if (dto.getPolicyname() != null) {
            entity.setAction(dto.getAction());
        }
        return entity;
    }
}
