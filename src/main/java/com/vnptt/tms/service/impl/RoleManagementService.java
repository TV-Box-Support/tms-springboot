package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.RoleManagementConverter;
import com.vnptt.tms.dto.RoleManagementDTO;
import com.vnptt.tms.entity.RoleManagementEntity;
import com.vnptt.tms.repository.RoleManagementRepository;
import com.vnptt.tms.service.IRoleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleManagementService implements IRoleManagementService {

    @Autowired
    private RoleManagementRepository roleManagementRepository;

    @Autowired
    private RoleManagementConverter roleManagementConverter;

    @Override
    public List<RoleManagementDTO> findAll() {
        List<RoleManagementEntity> entities = roleManagementRepository.findAll();
        List<RoleManagementDTO> result = new ArrayList<>();
        for (RoleManagementEntity item : entities) {
            result.add(roleManagementConverter.toDTO(item));
        }
        return result;
    }

    /**
     * @param model
     * @return
     */
    @Override
    public RoleManagementDTO save(RoleManagementDTO model) {
        RoleManagementEntity roleManagementEntity = roleManagementConverter.toEntity(model);
        roleManagementEntity = roleManagementRepository.save(roleManagementEntity);
        return roleManagementConverter.toDTO(roleManagementEntity);
    }

    /**
     * unnecessary (only use to test)
     *
     * @param ids
     * @return
     */
    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            roleManagementRepository.deleteById(item);
        }
    }

}
