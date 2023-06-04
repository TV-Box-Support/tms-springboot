package com.vnptt.tms.service;

import com.vnptt.tms.dto.RoleManagementDTO;

import java.util.List;

public interface IRoleManagementService {
    List<RoleManagementDTO> findAll();

    RoleManagementDTO save(RoleManagementDTO model);

    void delete(Long[] ids);
}
