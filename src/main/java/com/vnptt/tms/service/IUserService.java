package com.vnptt.tms.service;

import com.vnptt.tms.dto.UserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserDTO save(UserDTO userDTO);
    UserDTO findOne(Long id);
    int totalItem();
    void delete(Long[] ids);
    List<UserDTO> findAll(Pageable pageable);
    List<UserDTO> findAll();
    List<UserDTO> findAllWithRule(Long ruleId);
}
