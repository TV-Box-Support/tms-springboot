package com.vnptt.tms.service;

import com.vnptt.tms.dto.RoleFunctionDTO;

import java.util.List;

public interface IRoleFunctionService {
    List<RoleFunctionDTO> findAll();

    RoleFunctionDTO save(RoleFunctionDTO model);

    void delete(Long[] ids);
}
