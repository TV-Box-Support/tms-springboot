package com.vnptt.tms.service;

import com.vnptt.tms.dto.RolesDTO;

import java.util.List;

public interface IRolesService {
    List<RolesDTO> findAll();

    RolesDTO save(RolesDTO model);

    void delete(Long[] ids);
}
