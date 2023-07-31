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
        if (policyEntity.getAction() > 3 || policyEntity.getAction() < 1) {
            throw new RuntimeException("Action of policy must be 1,2,3 " );
        }
        // oke if policy don't have command
        CommandEntity commandEntity = commandRepository.findOneByName(policyDTO.getCommandName());
        if(commandEntity != null){
            policyEntity.setCommandEntity(commandEntity);
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
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<PolicyDTO> findAll(Pageable pageable) {
        List<PolicyEntity> entities = policyRepository.findAllByOrderByModifiedDateDesc(pageable);
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
        List<PolicyEntity> policyEntities = policyRepository.findAllByCommandEntityIdOrderByModifiedDateDesc(commandId);
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
        List<PolicyEntity> policyEntities = policyRepository.findPolicyEntitiesByApkEntitiesPolicyIdOrderByModifiedDateDesc(apkId);
        for (PolicyEntity entity : policyEntities) {
            PolicyDTO policyDTO = policyConverter.toDTO(entity);
            result.add(policyDTO);
        }
        return result;
    }

    /**
     * find all policy in device
     *todo: check
     * @param deviceId
     * @param pageable
     * @return
     */
    @Override
    public List<PolicyDTO> findAllWithDeviceId(Long deviceId, Pageable pageable) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device id = " + deviceId);
        }
        List<PolicyDTO> result = new ArrayList<>();
        List<PolicyEntity> policyEntities = policyRepository.findAllByDevicePolicyDetailEntitiesDeviceEntityDetailIdOrderByModifiedDateDesc(deviceId, pageable);
        for (PolicyEntity entity : policyEntities) {
            PolicyDTO policyDTO = policyConverter.toDTO(entity);
            result.add(policyDTO);
        }
        return result;
    }

    /**
     * set status for policy
     * policy detail status = 0 not run
     * policy detail status = 1 run
     * policy detail status = 2 running
     * if policy detail status = 3 ( policy success) -> don't set
     * policy detail status = 4 stop
     * <p>
     * status 0 not run
     * status 1 run
     * status 2 pause
     * status 3 stop
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
        for (DevicePolicyDetailEntity iteam : devicePolicyDetailEntities) {
            switch (status) {
                case 1:
                    if (iteam.getStatus() == 0) {
                        iteam.setStatus(1);
                    }
                    break;
                case 2:
                    if (iteam.getStatus() == 1) {
                        iteam.setStatus(0);
                    }
                    break;
                case 3:
                    iteam.setStatus(4);

            }

        }
        policyEntity = policyRepository.save(policyEntity);
        return policyConverter.toDTO(policyEntity);
    }

    @Override
    public List<PolicyDTO> findwithPolicyname(String policyname, Pageable pageable) {
        List<PolicyEntity> entities = policyRepository.findAllByPolicynameContaining(policyname, pageable);
        List<PolicyDTO> result = new ArrayList<>();
        for (PolicyEntity item : entities) {
            PolicyDTO policyDTO = policyConverter.toDTO(item);
            result.add(policyDTO);
        }
        return result;
    }

    @Override
    public Long totalCountByPolicynameContain(String policyname) {
        return policyRepository.countAllByPolicynameContaining(policyname);
    }

    @Override
    public Long totalCountByDeviceId(Long deviceId) {
        return policyRepository.countAllByDevicePolicyDetailEntitiesDeviceEntityDetailId(deviceId);
    }

    @Override
    public List<PolicyDTO> findAllWithDeviceIdAndPolicyName(Long deviceId, String policyname, Pageable pageable) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device id = " + deviceId);
        }
        List<PolicyDTO> result = new ArrayList<>();
        List<PolicyEntity> policyEntities = policyRepository.findPolicyEntitiesByDevicePolicyDetailEntitiesDeviceEntityDetailIdAndPolicynameContainingOrderByModifiedDateDesc(deviceId, policyname, pageable);
        for (PolicyEntity entity : policyEntities) {
            PolicyDTO policyDTO = policyConverter.toDTO(entity);
            result.add(policyDTO);
        }
        return result;
    }

    @Override
    public Long countAllByDeviceIdAndPolicyName(Long deviceId, String policyname) {
        return policyRepository.countAllByDevicePolicyDetailEntitiesDeviceEntityDetailIdAndPolicynameContaining(deviceId, policyname);
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
