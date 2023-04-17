package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.PolicyConverter;
import com.vnptt.tms.dto.PolicyDTO;
import com.vnptt.tms.entity.CommandEntity;
import com.vnptt.tms.entity.PolicyEntity;
import com.vnptt.tms.repository.CommandRepository;
import com.vnptt.tms.repository.PolicyRepository;
import com.vnptt.tms.service.IPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PolicyService implements IPolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private PolicyConverter policyConverter;

    @Override
    public PolicyDTO save(PolicyDTO policyDTO) {
        PolicyEntity policyEntity = new PolicyEntity();
        if (policyDTO.getId() != null){
            Optional <PolicyEntity> oldPolicyEntity = policyRepository.findById(policyDTO.getId());
            policyEntity = policyConverter.toEntity(policyDTO, oldPolicyEntity.get());
        } else {
            policyEntity = policyConverter.toEntity(policyDTO);
        }
        try{
            CommandEntity commandEntity = commandRepository.findOneByCommand(policyDTO.getCommandName());
            policyEntity.setCommandEntity(commandEntity);
        } catch (Exception e){
            policyDTO.setCommandName("Can't not find " + policyDTO.getCommandName());
            return policyDTO;
        }
        policyEntity = policyRepository.save(policyEntity);
        return policyConverter.toDTO(policyEntity);
    }

    @Override
    public PolicyDTO findOne(Long id) {
        PolicyEntity entity = policyRepository.findOneById(id);
        return policyConverter.toDTO(entity);
    }

    /**
     *  find item with page number and totalPage number
     * @param pageable
     * @return
     */
    @Override
    public List<PolicyDTO> findAll(Pageable pageable) {
        List<PolicyEntity> entities = policyRepository.findAll(pageable).getContent();
        List<PolicyDTO> result = new ArrayList<>();
        for(PolicyEntity item : entities){
            PolicyDTO policyDTO = policyConverter.toDTO(item);
            result.add(policyDTO);
        }
        return result;
    }

    @Override
    public List<PolicyDTO> findAll() {
        List<PolicyEntity> entities = policyRepository.findAll();
        List<PolicyDTO> result = new ArrayList<>();
        for(PolicyEntity item : entities){
            PolicyDTO policyDTO = policyConverter.toDTO(item);
            result.add(policyDTO);
        }
        return result;
    }

    @Override
    public int totalItem(){
        return (int) policyRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item: ids) {
            policyRepository.deleteById(item);
        }
    }
}
