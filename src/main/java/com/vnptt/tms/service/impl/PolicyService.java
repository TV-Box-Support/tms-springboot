package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.PolicyConverter;
import com.vnptt.tms.dto.PolicyDTO;
import com.vnptt.tms.entity.CommandEntity;
import com.vnptt.tms.entity.DevicePolicyDetailEntity;
import com.vnptt.tms.entity.PolicyEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApkRepository;
import com.vnptt.tms.repository.CommandRepository;
import com.vnptt.tms.repository.DeviceRepository;
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
    private ApkRepository apkRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private PolicyConverter policyConverter;

    /**
     * create policy from web
     *
     * @param policyDTO
     * @return
     */
    @Override
    public PolicyDTO save(PolicyDTO policyDTO) {
        PolicyEntity policyEntity = new PolicyEntity();
        if (policyDTO.getId() != null) {
            if (!policyRepository.existsById(policyDTO.getId())) {
                throw new ResourceNotFoundException("Not found policy with id = " + policyDTO.getId());
            }
            Optional<PolicyEntity> oldPolicyEntity = policyRepository.findById(policyDTO.getId());
            policyEntity = policyConverter.toEntity(policyDTO, oldPolicyEntity.get());
        } else {
            policyEntity = policyConverter.toEntity(policyDTO);
            // set default policy not run now
            policyEntity.setStatus(0);
        }
        // oke if policy don't have command
        CommandEntity commandEntity = commandRepository.findOneByCommand(policyDTO.getCommandName());
        policyEntity.setCommandEntity(commandEntity);

        policyEntity = policyRepository.save(policyEntity);
        return policyConverter.toDTO(policyEntity);
    }

    @Override
    public PolicyDTO findOne(Long id) {
        PolicyEntity entity = policyRepository.findOneById(id);
        return policyConverter.toDTO(entity);
    }

    /**
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<PolicyDTO> findAll(Pageable pageable) {
        List<PolicyEntity> entities = policyRepository.findAll(pageable).getContent();
        List<PolicyDTO> result = new ArrayList<>();
        for (PolicyEntity item : entities) {
            PolicyDTO policyDTO = policyConverter.toDTO(item);
            result.add(policyDTO);
        }
        return result;
    }

    @Override
    public List<PolicyDTO> findAll() {
        List<PolicyEntity> entities = policyRepository.findAll();
        List<PolicyDTO> result = new ArrayList<>();
        for (PolicyEntity item : entities) {
            PolicyDTO policyDTO = policyConverter.toDTO(item);
            result.add(policyDTO);
        }
        return result;
    }

    /**
     * find all policy with command Id
     *
     * @param commandId
     * @return
     */
    @Override
    public List<PolicyDTO> findAllWithCommand(Long commandId) {
        if (!commandRepository.existsById(commandId)) {
            throw new ResourceNotFoundException("Not found command with id = " + commandId);
        }
        List<PolicyEntity> policyEntities = policyRepository.findAllByCommandEntityId(commandId);
        List<PolicyDTO> result = new ArrayList<>();
        for (PolicyEntity entity : policyEntities) {
            PolicyDTO policyDTO = policyConverter.toDTO(entity);
            result.add(policyDTO);
        }
        return result;
    }

    /**
     * find all policy have apk
     *
     * @param apkId
     * @return
     */
    @Override
    public List<PolicyDTO> findAllWithApk(Long apkId) {
        if (!apkRepository.existsById(apkId)) {
            throw new ResourceNotFoundException("Not found apk with id = " + apkId);
        }
        List<PolicyDTO> result = new ArrayList<>();
        List<PolicyEntity> policyEntities = policyRepository.findPolicyEntitiesByApkEntitiesPolicyId(apkId);
        for (PolicyEntity entity : policyEntities) {
            PolicyDTO policyDTO = policyConverter.toDTO(entity);
            result.add(policyDTO);
        }
        return result;
    }

    /**
     *find all policy in device
     *
     * @param deviceId
     * @return
     */
    @Override
    public List<PolicyDTO> findAllWithDeviceId(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found apk device id = " + deviceId);
        }
        List<PolicyDTO> result = new ArrayList<>();
        List<PolicyEntity> policyEntities = policyRepository.findPolicyEntitiesByDevicePolicyDetailEntitiesDeviceEntityDetailId(deviceId);
        for (PolicyEntity entity : policyEntities) {
            PolicyDTO policyDTO = policyConverter.toDTO(entity);
            result.add(policyDTO);
        }
        return result;
    }

    /**
     * set status for policy
     * if policy detail status == 3 ( policy success) -> don't set
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public PolicyDTO updateStatus(Long id, int status) {
        if (!policyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Not found policy with id = " + id);
        }

        PolicyEntity policyEntity = policyRepository.findOneById(id);
        policyEntity.setStatus(status);
        List<DevicePolicyDetailEntity> devicePolicyDetailEntities = policyEntity.getDevicePolicyDetailEntities();
        for (DevicePolicyDetailEntity iteam : devicePolicyDetailEntities){
            if (iteam.getStatus() != 3 ){
                iteam.setStatus(status);
            }
        }
        policyEntity = policyRepository.save(policyEntity);
        return policyConverter.toDTO(policyEntity);
    }

    @Override
    public int totalItem() {
        return (int) policyRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            policyRepository.deleteById(item);
        }
    }
}
