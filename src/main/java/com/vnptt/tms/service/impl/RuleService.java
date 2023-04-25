package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.RuleConverter;
import com.vnptt.tms.dto.RuleDTO;
import com.vnptt.tms.entity.RuleEntity;
import com.vnptt.tms.repository.RuleRepository;
import com.vnptt.tms.service.IRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService implements IRuleService {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private RuleConverter ruleConverter;

    @Override
    public List<RuleDTO> findAll() {
        List<RuleEntity> entities = ruleRepository.findAll();
        List<RuleDTO> result = new ArrayList<>();
        for (RuleEntity item : entities) {
            result.add(ruleConverter.toDTO(item));
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
    public RuleDTO save(RuleDTO model) {
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
            ruleRepository.deleteById(item);
        }
    }

}
