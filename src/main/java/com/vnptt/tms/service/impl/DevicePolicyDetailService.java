package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.DevicePolicyDetailConverter;
import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.entity.DevicePolicyDetailEntity;
import com.vnptt.tms.entity.PolicyEntity;
import com.vnptt.tms.repository.DevicePolicyDetailRepository;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.repository.PolicyRepository;
import com.vnptt.tms.service.IDevicePolicyDetailnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

    @Override
    public DevicePolicyDetailDTO save(DevicePolicyDetailDTO devicePolicyDetailDTO) {
        DevicePolicyDetailEntity devicePolicyDetailEntity = new DevicePolicyDetailEntity();
        if (devicePolicyDetailDTO.getId() != null){
            Optional <DevicePolicyDetailEntity> oldDevicePolicyDetailEntity = devicePolicyDetailRepository.findById(devicePolicyDetailDTO.getId());
            devicePolicyDetailEntity = devicePolicyDetailConverter.toEntity(devicePolicyDetailDTO, oldDevicePolicyDetailEntity.get());
        } else {
            devicePolicyDetailEntity = devicePolicyDetailConverter.toEntity(devicePolicyDetailDTO);
        }
        // Set Device and Policy to PolyceDetail table @{
        try{
            PolicyEntity policyEntity = policyRepository.findOneById(devicePolicyDetailDTO.getPolicyId());
            devicePolicyDetailEntity.setPolicyEntityDetail(policyEntity);
        } catch (Exception e){
            devicePolicyDetailDTO.setDeviceSN("value wrong");
            return devicePolicyDetailDTO;
        }
        try {
            DeviceEntity deviceEntity = deviceRepository.findOneBySn(devicePolicyDetailDTO.getDeviceSN());
            devicePolicyDetailEntity.setDeviceEntityDetail(deviceEntity);
        } catch (Exception e){
            devicePolicyDetailDTO.setDeviceSN("Can't find device " + devicePolicyDetailDTO.getDeviceSN());
            return devicePolicyDetailDTO;
        }
        //}@

        devicePolicyDetailEntity = devicePolicyDetailRepository.save(devicePolicyDetailEntity);
        return devicePolicyDetailConverter.toDTO(devicePolicyDetailEntity);
    }

    @Override
    public DevicePolicyDetailDTO findOne(Long id) {
        DevicePolicyDetailEntity entity = devicePolicyDetailRepository.findOneById(id);
        return devicePolicyDetailConverter.toDTO(entity);
    }

    /**
     *  find item with page number and totalPage number
     * @param pageable
     * @return
     */
    @Override
    public List<DevicePolicyDetailDTO> findAll(Pageable pageable) {
        List<DevicePolicyDetailEntity> entities = devicePolicyDetailRepository.findAll(pageable).getContent();
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for(DevicePolicyDetailEntity item : entities){
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(item);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    @Override
    public List<DevicePolicyDetailDTO> findAll() {
        List<DevicePolicyDetailEntity> entities = devicePolicyDetailRepository.findAll();
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for(DevicePolicyDetailEntity item : entities){
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(item);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    @Override
    public int totalItem(){
        return (int) devicePolicyDetailRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item: ids) {
            devicePolicyDetailRepository.deleteById(item);
        }
    }
}
