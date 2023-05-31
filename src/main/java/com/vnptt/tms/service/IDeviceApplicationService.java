package com.vnptt.tms.service;

import com.vnptt.tms.dto.DeviceApplicationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDeviceApplicationService {
    DeviceApplicationDTO save(Long deviceId, Long applicationId);

    DeviceApplicationDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<DeviceApplicationDTO> findAll(Pageable pageable);

    List<DeviceApplicationDTO> findAll();

    void removeAppOnDevice(Long deviceId, Long applicationId);

    DeviceApplicationDTO update(Long deviceId, Long applicationId);
}
