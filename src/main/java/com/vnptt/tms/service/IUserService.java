package com.vnptt.tms.service;

import com.vnptt.tms.api.input.LoginRequest;
import com.vnptt.tms.dto.UserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {

    UserDTO findOne(Long id);

    int totalItem();

    void delete(Long[] ids);

    List<UserDTO> findAll(Pageable pageable);

    List<UserDTO> findAll();

    List<UserDTO> findAllWithRule(Long ruleId);

    UserDTO signup(UserDTO model);

    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    UserDTO update(UserDTO model);
}
