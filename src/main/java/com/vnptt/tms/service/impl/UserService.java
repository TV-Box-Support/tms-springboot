package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.UserConverter;
import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.dto.UserDTO;
import com.vnptt.tms.entity.UserEntity;
import com.vnptt.tms.entity.RuleEntity;
import com.vnptt.tms.entity.UserEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.RuleRepository;
import com.vnptt.tms.repository.UserRepository;
import com.vnptt.tms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private UserConverter userConverter;

    @Override
    public UserDTO save(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        if (userDTO.getId() != null) {
            Optional<UserEntity> oldUserEntity = userRepository.findById(userDTO.getId());
            userEntity = userConverter.toEntity(userDTO, oldUserEntity.get());
        } else {
            userEntity = userConverter.toEntity(userDTO);
        }
        try {
            RuleEntity ruleEntity = ruleRepository.findOneByName(userDTO.getRuleName());
            userEntity.setRuleEntity(ruleEntity);
        } catch (Exception e) {
            throw new ResourceNotFoundException("can't not found rule with rule_name = " + userDTO.getRuleName());
        }
        try{
            userEntity = userRepository.save(userEntity);
        } catch (Exception e){
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
        if (!ruleRepository.existsById(ruleId)) {
            throw new ResourceNotFoundException("Not found rule with id = " + ruleId);
        }
        List<UserEntity> userEntities = userRepository.findUserEntitiesByRuleEntityId(ruleId);
        List<UserDTO> result = new ArrayList<>();
        for (UserEntity entity : userEntities) {
            UserDTO userDTO = userConverter.toDTO(entity);
            result.add(userDTO);
        }
        return result;
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
