package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.ApkConverter;
import com.vnptt.tms.dto.ApkDTO;
import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.entity.ApkEntity;
import com.vnptt.tms.entity.ApplicationEntity;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.entity.PolicyEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.ApkRepository;
import com.vnptt.tms.repository.PolicyRepository;
import com.vnptt.tms.service.IApkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class ApkService implements IApkService {

    @Autowired
    private ApkRepository apkRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ApkConverter apkConverter;

    @Override
    public ApkDTO save(ApkDTO apkDTO) {
        ApkEntity apkEntity = new ApkEntity();
        if (apkDTO.getId() != null) {
            Optional<ApkEntity> oldApkEntity = apkRepository.findById(apkDTO.getId());
            apkEntity = apkConverter.toEntity(apkDTO, oldApkEntity.get());
        } else {
            apkEntity = apkConverter.toEntity(apkDTO);
        }
        apkEntity = apkRepository.save(apkEntity);
        return apkConverter.toDTO(apkEntity);
    }

    @Override
    public ApkDTO findOne(Long id) {
        ApkEntity entity = apkRepository.findOneById(id);
        return apkConverter.toDTO(entity);
    }

    /**
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<ApkDTO> findAll(Pageable pageable) {
        List<ApkEntity> entities = apkRepository.findAll(pageable).getContent();
        List<ApkDTO> result = new ArrayList<>();
        for (ApkEntity item : entities) {
            ApkDTO apkDTO = apkConverter.toDTO(item);
            result.add(apkDTO);
        }
        return result;
    }

    @Override
    public List<ApkDTO> findAll() {
        List<ApkEntity> entities = apkRepository.findAll();
        List<ApkDTO> result = new ArrayList<>();
        for (ApkEntity item : entities) {
            ApkDTO apkDTO = apkConverter.toDTO(item);
            result.add(apkDTO);
        }
        return result;
    }

    /**
     * find all apk in a policy
     *
     * @param policyId has required
     * @return
     */
    @Override
    public List<ApkDTO> findAllOnPolicy(Long policyId) {
        if (!apkRepository.existsById(policyId)) {
            throw new ResourceNotFoundException("Not found policy with id = " + policyId);
        }
        List<ApkEntity> apkEntities = apkRepository.findApkEntitiesByPolicyEntitiesId(policyId);
        List<ApkDTO> result = new ArrayList<>();
        for (ApkEntity entity : apkEntities) {
            ApkDTO apkDTO = apkConverter.toDTO(entity);
            result.add(apkDTO);
        }

        return result;
    }

    /**
     * add apk to policy
     * @param policyId
     * @param apkId
     * @return
     */
    @Override
    public ApkDTO addApkToPolicy(Long policyId, Long apkId) {
        ApkEntity apkEntity = policyRepository.findById(policyId).map(policy -> {
            ApkEntity apk = apkRepository.findById(apkId)
                    .orElseThrow(() -> new ResourceNotFoundException("Not found apk with id = " + apkId));

            // check if apk has still
            List<ApkEntity> apkEntities = policy.getApkEntitiesPolicy();
            for (ApkEntity item : apkEntities) {
                if (item.equals(apk)) {
                    return apk;
                }
            }
            //map and add apk to policy
            policy.addApk(apk);
            policyRepository.save(policy);
            return apk;
        }).orElseThrow(() -> new ResourceNotFoundException("Not found policy with id = " + policyId));
        return apkConverter.toDTO(apkEntity);
    }

    @Override
    public void removeApkinPolicy(Long policyId, Long apkId) {
        PolicyEntity policyEntity = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found policy with id = " + policyId));

        List <ApkEntity> apkEntities = policyEntity.getApkEntitiesPolicy();
        boolean remove = false;
        for (ApkEntity entity : apkEntities){
            if (Objects.equals(entity.getId(), apkId)){
                remove = true;
            }
        }
        if (remove){
            policyEntity.removeApk(apkId);
            policyRepository.save(policyEntity);
        } else {
            throw new ResourceNotFoundException("policy don't have apk with id = " + apkId);
        }

    }

    @Override
    public int totalItem() {
        return (int) apkRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            apkRepository.deleteById(item);
        }
    }
}
