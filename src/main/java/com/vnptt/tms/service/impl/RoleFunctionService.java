package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.RoleFunctionConverter;
import com.vnptt.tms.dto.RoleFunctionDTO;
import com.vnptt.tms.entity.RoleFunctionEntity;
import com.vnptt.tms.repository.RoleFunctionRepository;
import com.vnptt.tms.service.IRoleFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleFunctionService implements IRoleFunctionService {

    @Autowired
    private RoleFunctionRepository roleFunctionRepository;

    @Autowired
    private RoleFunctionConverter roleFunctionConverter;

    @Override
    public List<RoleFunctionDTO> findAll() {
        List<RoleFunctionEntity> entities = roleFunctionRepository.findAll();
        List<RoleFunctionDTO> result = new ArrayList<>();
        for (RoleFunctionEntity item : entities) {
            result.add(roleFunctionConverter.toDTO(item));
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
    public RoleFunctionDTO save(RoleFunctionDTO model) {
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
            roleFunctionRepository.deleteById(item);
        }
    }

}
