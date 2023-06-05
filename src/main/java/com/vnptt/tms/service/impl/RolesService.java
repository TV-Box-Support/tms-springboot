package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.RolesConverter;
import com.vnptt.tms.dto.RolesDTO;
import com.vnptt.tms.entity.RolesEntity;
import com.vnptt.tms.repository.RolesRepository;
import com.vnptt.tms.service.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolesService implements IRolesService {

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private RolesConverter rolesConverter;

    @Override
    public List<RolesDTO> findAll() {
        List<RolesEntity> entities = rolesRepository.findAll();
        List<RolesDTO> result = new ArrayList<>();
        for (RolesEntity item : entities) {
            result.add(rolesConverter.toDTO(item));
        }
        return result;
    }

    /**
     * unnecessary (only use to test)
     *
     * @param model
     * @return
     */
    @Override
    public RolesDTO save(RolesDTO model) {
//        RuleEntity ruleEntity = ruleConverter.toEntity(model);
//        ruleEntity = ruleRepository.save(ruleEntity);
//        return ruleConverter.toDTO(ruleEntity);
        return null;
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
            rolesRepository.deleteById(item);
        }
    }

}
