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

    void remove(Long id);

    List<UserDTO> findAllWithActive(Pageable pageable, Integer active);

    List<UserDTO> findAllWithActive(Integer active);

    UserDTO update(UserDTO model);

    UserDTO updatePassword(Long id, String passwordold, String passwordnew);

    UserDTO forcedUpdatePassword(Long id, String passwordnew);
}
