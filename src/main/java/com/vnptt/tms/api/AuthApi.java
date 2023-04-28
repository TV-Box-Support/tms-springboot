package com.vnptt.tms.api;

import com.vnptt.tms.api.input.LoginRequest;
import com.vnptt.tms.dto.UserDTO;
import com.vnptt.tms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Application Programming Interface for auth manager
 * include:
 * <p>
 * - signin for everybody with Unauthorization
 * - signup for only admin and mod
 * <p>
 * ...
 */
@CrossOrigin
@RestController
@RequestMapping("/TMS/api/auth")
public class AuthApi {
    @Autowired
    IUserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public UserDTO registerUser(@Valid @RequestBody UserDTO model) {
        return userService.signup(model);
    }
}
