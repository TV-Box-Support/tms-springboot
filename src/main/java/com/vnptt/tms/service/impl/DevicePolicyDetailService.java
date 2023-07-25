package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.DevicePolicyDetailConverter;
import com.vnptt.tms.converter.PolicyConverter;
import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import com.vnptt.tms.dto.PolicyDTO;
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
    private PolicyConverter policyConverter;

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

        if (entity.getStatus() != 0) {
            throw new RuntimeException("cannot change the already active policy");
        }

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

                DevicePolicyDetailEntity check = devicePolicyDetailRepository.findOneByDeviceEntityDetailIdAndPolicyEntityDetailId(id, policyId);
                if (check != null) {
                    continue;
                }

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
     * @param pageable
     * @return
     */
    @Override
    public List<DevicePolicyDetailDTO> findAllWithDevice(Long deviceId, Pageable pageable) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        List<DevicePolicyDetailEntity> devicePolicyDetailEntities = devicePolicyDetailRepository.findAllByDeviceEntityDetailIdOrderByModifiedDateDesc(deviceId, pageable);
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
     * @param pageable
     * @return
     */
    @Override
    public List<DevicePolicyDetailDTO> findAllWithPolicy(Long policyId, Pageable pageable) {
        if (!policyRepository.existsById(policyId)) {
            throw new ResourceNotFoundException("Not found policy with id = " + policyId);
        }
        List<DevicePolicyDetailEntity> devicePolicyDetailEntities = devicePolicyDetailRepository.findAllByPolicyEntityDetailIdOrderByModifiedDateDesc(policyId, pageable);
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for (DevicePolicyDetailEntity entity : devicePolicyDetailEntities) {
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(entity);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    @Override
    public Long countAllWithPolicy(Long policyId) {
        return devicePolicyDetailRepository.countAllByPolicyEntityDetailId(policyId);
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

        if (policyEntity.getStatus() != 0) {
            throw new RuntimeException("cannot change the already active policy");
        }

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
            DevicePolicyDetailEntity check = devicePolicyDetailRepository.findOneByDeviceEntityDetailIdAndPolicyEntityDetailId(device.getId(), policyId);
            if (check != null) {
                continue;
            }
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
    public List<DevicePolicyDetailDTO> findAllWithPolicy(Long policyId, Integer status, Pageable pageable) {
        if (!policyRepository.existsById(policyId)) {
            throw new ResourceNotFoundException("Not found policy with id = " + policyId);
        }
        List<DevicePolicyDetailEntity> devicePolicyDetailEntities = devicePolicyDetailRepository.findAllByPolicyEntityDetailIdAndStatusOrderByModifiedDateDesc(policyId, status, pageable);
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for (DevicePolicyDetailEntity entity : devicePolicyDetailEntities) {
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(entity);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    @Override
    public Long countAllWithPolicyStatus(Long policyId, Integer status) {
        return devicePolicyDetailRepository.countAllByPolicyEntityDetailIdAndStatus(policyId, status);
    }

    @Override
    public List<PolicyDTO> findAllWithDeviceAndStatusRun(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        List<DevicePolicyDetailEntity> devicePolicyDetailEntitiesRun = devicePolicyDetailRepository.findAllByDeviceEntityDetailIdAndStatusOrderByModifiedDateAsc(deviceId, 1);
        List<PolicyDTO> result = new ArrayList<>();
        for (DevicePolicyDetailEntity entity : devicePolicyDetailEntitiesRun) {
            PolicyEntity policyEntity = entity.getPolicyEntityDetail();

            result.add(policyConverter.toDTO(policyEntity));
        }
        return result;
    }

    @Override
    public void removeDevicePolicyDetailWithDeviceAndPolicy(Long policyId, Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        PolicyEntity policyEntity = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(" cant not find policy with id = " + policyId));
        if (policyEntity.getStatus() != 0) {
            throw new RuntimeException("cannot change the already active policy");
        }
        DevicePolicyDetailEntity devicePolicyDetail = devicePolicyDetailRepository.findOneByDeviceEntityDetailIdAndPolicyEntityDetailId(deviceId, policyId);
        if (devicePolicyDetail == null) {
            throw new ResourceNotFoundException("device is not assigned policy ");
        } else if (devicePolicyDetail.getStatus() == 0) {
            devicePolicyDetailRepository.deleteById(devicePolicyDetail.getId());
        } else {
            throw new RuntimeException("device cannot be erased in already enforced policy");
        }

    }

    @Override
    public Long countAllWithDevice(Long deviceId) {
        return devicePolicyDetailRepository.countAllByDeviceEntityDetailId(deviceId);
    }

    @Override
    public List<DevicePolicyDetailDTO> findAllWithDeviceAndStatus(Long deviceId, Integer status, Pageable pageable) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Not found device with id = " + deviceId);
        }
        List<DevicePolicyDetailEntity> devicePolicyDetailEntities = devicePolicyDetailRepository.findAllByDeviceEntityDetailIdAndStatusOrderByModifiedDateDesc(deviceId, status, pageable);
        List<DevicePolicyDetailDTO> result = new ArrayList<>();
        for (DevicePolicyDetailEntity entity : devicePolicyDetailEntities) {
            DevicePolicyDetailDTO devicePolicyDetailDTO = devicePolicyDetailConverter.toDTO(entity);
            result.add(devicePolicyDetailDTO);
        }
        return result;
    }

    @Override
    public Long countAllWithDeviceAndStatus(Long deviceId, Integer status) {
        return devicePolicyDetailRepository.countAllByDeviceEntityDetailIdAndStatus(deviceId, status);
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
