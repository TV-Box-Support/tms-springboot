package com.vnptt.tms.service.impl;

import com.vnptt.tms.api.input.LoginRequest;
import com.vnptt.tms.config.ERoleFunction;
import com.vnptt.tms.converter.UserConverter;
import com.vnptt.tms.dto.UserDTO;
import com.vnptt.tms.entity.RoleFunctionEntity;
import com.vnptt.tms.entity.RoleManagementEntity;
import com.vnptt.tms.entity.UserEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.RoleFunctionRepository;
import com.vnptt.tms.repository.RoleManagementRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService implements IUserService {//, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleFunctionRepository roleFunctionRepository;

    @Autowired
    private RoleManagementRepository roleManagementRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

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

        List<RoleFunctionEntity> ruleEntities = new ArrayList<>();
        List<String> rules = userDTO.getRuleName();
        if (rules != null) {
            for (String iteam : rules) {
                RoleFunctionEntity roleFunctionEntity = roleFunctionRepository.findOneByName(iteam);
                if (roleFunctionEntity == null) {
                    throw new ResourceNotFoundException("can't not found rule with rule_name = " + userDTO.getRuleName());
                }
                ruleEntities.add(roleFunctionEntity);
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
     * update password for user
     *
     * @param id
     * @param passwordold
     * @param passwordnew
     * @return
     */
    @Override
    public UserDTO updatePassword(Long id, String passwordold, String passwordnew) {
        UserEntity userEntity = userRepository.findOneById(id);
        if (userEntity == null) {
            throw new ResourceNotFoundException("not found user with id = " + id);
        }
        if (!encoder.matches(passwordold, userEntity.getPassword())) {
            throw new RuntimeException("Wrong password: " + passwordold);
        }
        userEntity.setPassword(encoder.encode(passwordnew));
        userRepository.save(userEntity);
        return userConverter.toDTO(userEntity);
    }

    /**
     * update password with role admin
     *
     * @param id
     * @param passwordnew
     * @return
     */
    @Override
    public UserDTO forcedUpdatePassword(Long id, String passwordnew) {
        UserEntity userEntity = userRepository.findOneById(id);
        if (userEntity == null) {
            throw new ResourceNotFoundException("not found user with id = " + id);
        }
        userEntity.setPassword(encoder.encode(passwordnew));
        userRepository.save(userEntity);
        return userConverter.toDTO(userEntity);
    }

    @Override
    public List<UserDTO> findAllWithNameOrEmailOrUsernameOrCompany(Pageable pageable, Integer active, String name, String email, String username, String company) {
        boolean activeConvert = true;
        if (active == 0) {
            activeConvert = false;
        }
        List<UserEntity> entities = userRepository.findAllByActiveOrNameContainingOrEmailContainingOrUsernameContainingOrCompanyContainingOrderByModifiedDateDesc(pageable, activeConvert, name, email, username, company);
        List<UserDTO> result = new ArrayList<>();
        for (UserEntity item : entities) {
            UserDTO userDTO = userConverter.toDTO(item);
            result.add(userDTO);
        }
        return result;
    }

    @Override
    public Long totalItemWithNameOrEmailOrUsernameOrCompany(Integer active, String name, String email, String username, String company) {
        boolean activeConvert = true;
        if (active == 0) {
            activeConvert = false;
        }
        return userRepository.countAllByActiveOrNameContainingOrEmailContainingOrUsernameContainingOrCompanyContaining(activeConvert, name, email, username, company);
    }

    @Override
    public Long totalItemWithActive(Integer active) {
        boolean activeConvert = true;
        if (active == 0) {
            activeConvert = false;
        }
        return userRepository.countAllByActive(activeConvert);
    }

    @Override
    public void remove(Long id) {
        UserEntity userEntity = userRepository.findOneById(id);
        if (userEntity == null) {
            throw new ResourceNotFoundException("not found user with id = " + id);
        }
        userEntity.setActive(false);
        userRepository.save(userEntity);
    }

    @Override
    public List<UserDTO> findAllWithActive(Pageable pageable, Integer active) {
        boolean activeConvert = true;
        if (active == 0) {
            activeConvert = false;
        }
        List<UserEntity> entities = userRepository.findAllByActiveOrderByModifiedDateDesc(pageable, activeConvert);
        List<UserDTO> result = new ArrayList<>();
        for (UserEntity item : entities) {
            UserDTO userDTO = userConverter.toDTO(item);
            result.add(userDTO);
        }
        return result;
    }

    @Override
    public List<UserDTO> findAllWithActive(Integer active) {
        boolean activeConvert = true;
        if (active == 0) {
            activeConvert = false;
        }
        List<UserEntity> entities = userRepository.findAllByActiveOrderByModifiedDateDesc(activeConvert);
        List<UserDTO> result = new ArrayList<>();
        for (UserEntity item : entities) {
            UserDTO userDTO = userConverter.toDTO(item);
            result.add(userDTO);
        }
        return result;
    }


    @Override
    public UserDTO findOne(Long id) {
        UserEntity entity = userRepository.findOneById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("not found user with id = " + id);
        }
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

    /**
     * todo modify
     * @param ruleIds
     * @return
     */
    @Override
    public List<UserDTO> findAllWithRule(Long[] ruleIds) {
//        List<RuleEntity> ruleEntities = new ArrayList<>();
//        for (Long id : ruleIds) {
//            RuleEntity ruleEntity = ruleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found rule with id = " + id));
//            ruleEntities.add(ruleEntity);
//        }
//        List<UserEntity> userEntities = userRepository.findAllByRuleEntities(ruleEntities);
//        List<UserDTO> result = new ArrayList<>();
//        for (UserEntity entity : userEntities) {
//            UserDTO userDTO = userConverter.toDTO(entity);
//            result.add(userDTO);
//        }
//        return result;
        return null;
    }

    /**
     * create new account
     *
     * @param model model user DTO
     * @return user DTO after save on database
     */
    @Override
    public UserDTO signup(UserDTO model) {
        if (userRepository.existsByUsername(model.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        UserEntity userEntity = userConverter.toEntity(model);
        userEntity.setActive(true);
        if (model.getRuleManagement() != null){
            RoleManagementEntity roleManagementEntity = roleManagementRepository.findByName(model.getRuleManagement());
            if (roleManagementEntity == null){
                throw new ResourceNotFoundException("Not found role management!");
            }
            userEntity.setRoleManagementEntityUser(roleManagementEntity);
        }

        // Create new user's account
        userEntity.setPassword(encoder.encode(model.getPassword()));

        List<String> strRoles = model.getRuleName();
        List<RoleFunctionEntity> roles = new ArrayList<>();

        if (strRoles == null) {
            RoleFunctionEntity userRole = roleFunctionRepository.findByName(ERoleFunction.ROLE_USER);
            if (userRole == null) {
                throw new RuntimeException("Error: Role is not found.");
            }
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleFunctionEntity adminRole = roleFunctionRepository.findByName(ERoleFunction.ROLE_ADMIN);
                        if (adminRole == null) {
                            throw new RuntimeException("Error: Role is not found.");
                        }
                        roles.add(adminRole);
                        break;
                    case "mod":
                        RoleFunctionEntity modRole = roleFunctionRepository.findByName(ERoleFunction.ROLE_MODERATOR);
                        if (modRole == null) {
                            throw new RuntimeException("Error: Role is not found.");
                        }
                        roles.add(modRole);

                        break;
                    default:
                        RoleFunctionEntity userRole = roleFunctionRepository.findByName(ERoleFunction.ROLE_USER);
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

        return ResponseEntity.ok(
                new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        rules,
                        "TMS"));
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
}
