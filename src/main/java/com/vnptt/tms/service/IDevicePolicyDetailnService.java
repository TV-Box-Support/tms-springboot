package com.vnptt.tms.service;

import com.vnptt.tms.api.output.chart.PieChart;
import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import com.vnptt.tms.dto.PolicyDTO;
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

    List<DevicePolicyDetailDTO> findAllWithDevice(Long deviceId, Pageable pageable);

    DevicePolicyDetailDTO update(Long id, int status);

    List<DevicePolicyDetailDTO> findAllWithPolicy(Long policyId, Pageable pageable);
    
    Long countAllWithPolicy(Long policyId);

    List<DevicePolicyDetailDTO> save(HttpServletRequest request, Long listDeviceId, Long policyId);

    List<DevicePolicyDetailDTO> findAllWithPolicy(Long policyId, Integer status, Pageable pageable);

    Long countAllWithPolicyStatus(Long policyId, Integer status);

    List<PolicyDTO> findAllWithDeviceAndStatusRun(Long deviceId);

    void removeDevicePolicyDetailWithDeviceAndPolicy(Long policyId, Long deviceId);

    Long countAllWithDevice(Long deviceId);

    List<DevicePolicyDetailDTO> findAllWithDeviceAndStatus(Long deviceId, Integer status, Pageable pageable);

    Long countAllWithDeviceAndStatus(Long deviceId, Integer status);

    List<PieChart> getTotalPieChart(Long policyId);
}
