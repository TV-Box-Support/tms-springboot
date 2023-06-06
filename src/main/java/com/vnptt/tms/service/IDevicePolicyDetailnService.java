package com.vnptt.tms.service;

import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IDevicePolicyDetailnService {
    List<DevicePolicyDetailDTO> save(HttpServletRequest request, Long[] ids, Long policyId);

    DevicePolicyDetailDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<DevicePolicyDetailDTO> findAll(Pageable pageable);

    List<DevicePolicyDetailDTO> findAll();

    List<DevicePolicyDetailDTO> findAllWithDevice(Long deviceId);

    DevicePolicyDetailDTO update(Long id, int status);

    List<DevicePolicyDetailDTO> findAllWithPolicy(Long policyId);

    List<DevicePolicyDetailDTO> save(HttpServletRequest request, Long listDeviceId, Long policyId);
}
