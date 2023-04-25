package com.vnptt.tms.service.impl;

import com.vnptt.tms.api.input.LoginRequest;
import com.vnptt.tms.config.ERole;
import com.vnptt.tms.converter.UserConverter;
import com.vnptt.tms.dto.UserDTO;
import com.vnptt.tms.entity.RuleEntity;
import com.vnptt.tms.entity.UserEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.RuleRepository;
import com.vnptt.tms.repository.UserRepository;
import com.vnptt.tms.security.jwt.JwtUtils;
import com.vnptt.tms.security.responce.JwtResponse;
import com.vnptt.tms.security.services.UserDetailsImpl;
import com.vnptt.tms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }

        return build(userEntity);
    }

    /**
     * update user for put request
     *
     * @param userDTO
     * @return
     */
    @Override
    public UserDTO update(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();

        Optional<UserEntity> oldUserEntity = userRepository.findById(userDTO.getId());
        userEntity = userConverter.toEntity(userDTO, oldUserEntity.get());

        List<RuleEntity> ruleEntities = new ArrayList<>();
        List<String> rules = userDTO.getRuleName();
        if (rules != null) {
            for (String iteam : rules) {
                RuleEntity ruleEntity = ruleRepository.findOneByName(iteam);
                if (ruleEntity == null) {
                    throw new ResourceNotFoundException("can't not found rule with rule_name = " + userDTO.getRuleName());
                }
                ruleEntities.add(ruleEntity);
            }

            userEntity.setRuleEntities(ruleEntities);
        }

        try {
            userEntity = userRepository.save(userEntity);
        } catch (Exception e) {
            throw new ResourceNotFoundException("have the same Username of account alive: " + userDTO.getUsername());
        }

        return userConverter.toDTO(userEntity);
    }

    /**
     * @Override public UserDTO update(UserDTO userDTO) {
     * UserEntity oldUserEntity = userRepository.findOne(userDTO.getId());
     * UserEntity userEntity = userConverter.toEntity(userDTO, oldUserEntity)
     * RuleEntity ruleEntity = ruleRepository.findOneByName(userDTO.getRuleName());
     * userEntity.setRuleEntityUser(ruleEntity);
     * userEntity = userRepository.save(userEntity);
     * return userConverter.toDTO(userEntity);
     * }
     **/

    @Override
    public UserDTO findOne(Long id) {
        UserEntity entity = userRepository.findOneById(id);
        return userConverter.toDTO(entity);
    }

    /**
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        List<UserEntity> entities = userRepository.findAll(pageable).getContent();
        List<UserDTO> result = new ArrayList<>();
        for (UserEntity item : entities) {
            UserDTO userDTO = userConverter.toDTO(item);
            result.add(userDTO);
        }
        return result;
    }

    @Override
    public List<UserDTO> findAll() {
        List<UserEntity> entities = userRepository.findAll();
        List<UserDTO> result = new ArrayList<>();
        for (UserEntity item : entities) {
            UserDTO userDTO = userConverter.toDTO(item);
            result.add(userDTO);
        }
        return result;
    }

    @Override
    public List<UserDTO> findAllWithRule(Long ruleId) {
//        if (!ruleRepository.existsById(ruleId)) {
//            throw new ResourceNotFoundException("Not found rule with id = " + ruleId);
//        }
//        List<UserEntity> userEntities = userRepository.findUserEntitiesByRuleEntityId(ruleId);
//        List<UserDTO> result = new ArrayList<>();
//        for (UserEntity entity : userEntities) {
//            UserDTO userDTO = userConverter.toDTO(entity);
//            result.add(userDTO);
//        }
//        return result;
        return null;
    }


    @Override
    public UserDTO signup(UserDTO model) {
        if (userRepository.existsByUsername(model.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        UserEntity userEntity = userConverter.toEntity(model);

        // Create new user's account
        userEntity.setPassword(encoder.encode(model.getPassword()));

        List<String> strRoles = model.getRuleName();
        List<RuleEntity> roles = new ArrayList<>();

        if (strRoles == null) {
            RuleEntity userRole = ruleRepository.findByName(ERole.ROLE_USER);
            if (userRole == null) {
                throw new RuntimeException("Error: Role is not found.");
            }
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RuleEntity adminRole = ruleRepository.findByName(ERole.ROLE_ADMIN);
                        if (adminRole == null) {
                            throw new RuntimeException("Error: Role is not found.");
                        }
                        roles.add(adminRole);
                        break;
                    case "mod":
                        RuleEntity modRole = ruleRepository.findByName(ERole.ROLE_MODERATOR);
                        if (modRole == null) {
                            throw new RuntimeException("Error: Role is not found.");
                        }
                        roles.add(modRole);

                        break;
                    default:
                        RuleEntity userRole = ruleRepository.findByName(ERole.ROLE_USER);
                        if (userRole == null) {
                            throw new RuntimeException("Error: Role is not found.");
                        }
                        roles.add(userRole);
                }
            });
        }

        userEntity.setRuleEntities(roles);
        userRepository.save(userEntity);

        return userConverter.toDTO(userEntity);
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> rules = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                rules));
    }

    @Override
    public int totalItem() {
        return (int) userRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            userRepository.deleteById(item);
        }
    }

    public UserDetails build(UserEntity user) {
        List<GrantedAuthority> authorities = user.getRuleEntities().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
