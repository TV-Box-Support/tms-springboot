package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.DevicePolicyDetailConverter;
import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.entity.DevicePolicyDetailEntity;
import com.vnptt.tms.entity.PolicyEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.DevicePolicyDetailRepository;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.repository.PolicyRepository;
import com.vnptt.tms.service.IDevicePolicyDetailnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DevicePolicyDetailService implements IDevicePolicyDetailnService {

    @Autowired
    private DevicePolicyDetailRepository devicePolicyDetailRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DevicePolicyDetailConverter devicePolicyDetailConverter;

    /**
     * create list policy detail with list deviceId math
     *
     * @param ids list deviceId
     * @return
     */
    @Override
    public List<DevicePolicyDetailDTO> save(Long[] ids, Long policyId) {
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        PolicyEntity entity = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(" cant not find policy with id = " + policyId));

        for (Long id : ids) {
            DeviceEntity deviceEntity = deviceRepository.findOneById(id);
            if (deviceEntity != null) {
                DevicePolicyDetailEntity devicePolicyDetailEntity = new DevicePolicyDetailEntity();
                devicePolicyDetailEntity.setDeviceEntityDetail(deviceEntity);
                devicePolicyDetailEntity.setPolicyEntityDetail(entity);
                devicePolicyDetailEntity.setStatus(0);
                devicePolicyDetailEntity.setAction(entity.getAction());

                devicePolicyDetailEntity = devicePolicyDetailRepository.save(devicePolicyDetailEntity);
                result.add(devicePolicyDetailConverter.toDTO(devicePolicyDetailEntity));
            }
        }
        return result;
    }

    @Override
    public DevicePolicyDetailDTO findOne(Long id) {
        DevicePolicyDetailEntity entity = devicePolicyDetailRepository.findOneById(id);
        return devicePolicyDetailConverter.toDTO(entity);
    }

    /**
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<DevicePolicyDetailDTO> findAll(Pageable pageable) {
        List<DevicePolicyDetailEntity> entities = devicePolicyDetailRepository.findAll(pageable).getContent();
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for (DevicePolicyDetailEntity item : entities) {
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(item);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    @Override
    public List<DevicePolicyDetailDTO> findAll() {
        List<DevicePolicyDetailEntity> entities = devicePolicyDetailRepository.findAll();
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for (DevicePolicyDetailEntity item : entities) {
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(item);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    /**
     * find all polycy of device
     *
     * @param deviceId
     * @return
     */
    @Override
    public List<DevicePolicyDetailDTO> findAllWithDevice(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        List<DevicePolicyDetailEntity> devicePolicyDetailEntities = devicePolicyDetailRepository.findAllByDeviceEntityDetailId(deviceId);
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for (DevicePolicyDetailEntity entity : devicePolicyDetailEntities) {
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(entity);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    /**
     * box update status when active policy done
     *
     * @param id     id of policyDeviceDetail
     * @param status
     * @return
     */
    @Override
    public DevicePolicyDetailDTO update(Long id, int status) {
        DevicePolicyDetailEntity devicePolicyDetailEntity = devicePolicyDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found devicePolicyDEtail with id = " + id));
        devicePolicyDetailEntity.setStatus(status);
        devicePolicyDetailEntity = devicePolicyDetailRepository.save(devicePolicyDetailEntity);
        return devicePolicyDetailConverter.toDTO(devicePolicyDetailEntity);
    }

    /**
     * find all policy of device
     *
     * @param policyId
     * @return
     */
    @Override
    public List<DevicePolicyDetailDTO> findAllWithPolicy(Long policyId) {
        if (!policyRepository.existsById(policyId)) {
            throw new ResourceNotFoundException("Not found policy with id = " + policyId);
        }
        List<DevicePolicyDetailEntity> devicePolicyDetailEntities = devicePolicyDetailRepository.findAllByPolicyEntityDetailId(policyId);
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for (DevicePolicyDetailEntity entity : devicePolicyDetailEntities) {
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(entity);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    @Override
    public int totalItem() {
        return (int) devicePolicyDetailRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            devicePolicyDetailRepository.deleteById(item);
        }
    }
}
