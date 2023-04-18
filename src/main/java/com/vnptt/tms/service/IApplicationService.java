package com.vnptt.tms.service;

import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.entity.ApplicationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IApplicationService {
    ApplicationDTO save(ApplicationDTO applicationDTO);

    ApplicationDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<ApplicationDTO> findAll(Pageable pageable);

    List<ApplicationDTO> findAll();

    List<ApplicationDTO> findAllOnDevice(Long deviceId);

    ApplicationDTO addAppToDevice(Long deviceId, ApplicationDTO model);

    void removeAppOnDevice(Long deviceId, Long applicationId);

    List<ApplicationDTO> findByPackagename(String packagename);
}
