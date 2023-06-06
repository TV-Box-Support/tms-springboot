package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.DevicePolicyDetailConverter;
import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import com.vnptt.tms.entity.*;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.*;
import com.vnptt.tms.security.jwt.AuthTokenFilter;
import com.vnptt.tms.security.jwt.JwtUtils;
import com.vnptt.tms.service.IDevicePolicyDetailnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class DevicePolicyDetailService implements IDevicePolicyDetailnService {

    @Autowired
    private DevicePolicyDetailRepository devicePolicyDetailRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListDeviceRepository listDeviceRepository;

    @Autowired
    private DevicePolicyDetailConverter devicePolicyDetailConverter;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * create list policy detail with list deviceId math
     *
     * @return
     */
    @Override
    public List<DevicePolicyDetailDTO> save(HttpServletRequest request, Long[] deviceIds, Long policyId) {
        PolicyEntity entity = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(" cant not find policy with id = " + policyId));

        String jwt = authTokenFilter.parseJwtTMS(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserEntity userEntity = userRepository.findByUsername(username);

        List<ListDeviceEntity> listDeviceEntities = userEntity.getDeviceEntities();

        List<DevicePolicyDetailDTO> result = new ArrayList<>();

        Set<DeviceEntity> deviceEntitiesCheck = new HashSet<>();
        for (ListDeviceEntity list : listDeviceEntities) {
            List<DeviceEntity> devices = list.getListDeviceDetail();
            deviceEntitiesCheck.addAll(devices);
        }

        List<DeviceEntity> finalDeviceEntitiesCheck = new ArrayList<>(deviceEntitiesCheck);
        // Check if the elements in deviceIds are the same as geviceEntity.getId() in the finalDeviceEntitiesCheck list
        for (Long deviceId : deviceIds) {
            boolean found = false;
            for (DeviceEntity deviceEntity : finalDeviceEntitiesCheck) {
                if (deviceId.equals(deviceEntity.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("you do not have permission to operate the device with id = " + deviceId);
            }
        }

        for (Long id : deviceIds) {
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
        List<DevicePolicyDetailEntity> devicePolicyDetailEntities = devicePolicyDetailRepository.findAllByDeviceEntityDetailIdOrderByModifiedDateDesc(deviceId);
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
                .orElseThrow(() -> new ResourceNotFoundException("Not found devicePolicyDetail with id = " + id));
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
        List<DevicePolicyDetailEntity> devicePolicyDetailEntities = devicePolicyDetailRepository.findAllByPolicyEntityDetailIdOrderByModifiedDateDesc(policyId);
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for (DevicePolicyDetailEntity entity : devicePolicyDetailEntities) {
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(entity);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    /**
     * Create list policy detail for device
     * Box will be checked and know what to do
     *
     * @param request
     * @param listDeviceId
     * @param policyId
     * @return
     */
    @Override
    public List<DevicePolicyDetailDTO> save(HttpServletRequest request, Long listDeviceId, Long policyId) {
        PolicyEntity policyEntity = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(" cant not find policy with id = " + policyId));

        ListDeviceEntity listDevice = listDeviceRepository.findById(listDeviceId)
                .orElseThrow(() -> new ResourceNotFoundException(" cant not find list Device with id = " + listDeviceId));

        String jwt = authTokenFilter.parseJwtTMS(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserEntity userEntity = userRepository.findByUsername(username);
        List<ListDeviceEntity> listDeviceEntities = userEntity.getDeviceEntities();

        List<DevicePolicyDetailDTO> result = new ArrayList<>();

        boolean isWrong = true;
        for (ListDeviceEntity list : listDeviceEntities) {
            if (listDeviceId.equals(list.getId())) {
                isWrong = false;
                break;
            }
        }

        if (isWrong) {
            throw new RuntimeException("you do not have permission to operate the list device with id = " + listDeviceId);
        }

        List<DeviceEntity> deviceEntities = listDevice.getListDeviceDetail();
        for (DeviceEntity device : deviceEntities) {
            DevicePolicyDetailEntity devicePolicyDetailEntity = new DevicePolicyDetailEntity();
            devicePolicyDetailEntity.setDeviceEntityDetail(device);
            devicePolicyDetailEntity.setPolicyEntityDetail(policyEntity);
            devicePolicyDetailEntity.setStatus(0);
            devicePolicyDetailEntity.setAction(policyEntity.getAction());

            devicePolicyDetailEntity = devicePolicyDetailRepository.save(devicePolicyDetailEntity);
            result.add(devicePolicyDetailConverter.toDTO(devicePolicyDetailEntity));

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
