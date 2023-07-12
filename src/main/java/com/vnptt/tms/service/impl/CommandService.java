package com.vnptt.tms.service.impl;

import com.vnptt.tms.converter.CommandConverter;
import com.vnptt.tms.dto.CommandDTO;
import com.vnptt.tms.entity.CommandEntity;
import com.vnptt.tms.entity.AlertDialogEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.AlertDialogRepository;
import com.vnptt.tms.repository.CommandRepository;
import com.vnptt.tms.service.ICommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CommandService implements ICommandService {

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandConverter commandConverter;

    @Autowired
    private AlertDialogRepository alertDialogRepository;


    @Override
    public CommandDTO save(CommandDTO commandDTO) {
        CommandEntity commandEntity = new CommandEntity();
        if (commandDTO.getId() != null) {
            Optional<CommandEntity> oldCommandEntity = commandRepository.findById(commandDTO.getId());
            commandEntity = commandConverter.toEntity(commandDTO, oldCommandEntity.get());
            if (commandDTO.getCommandNotificationId() != null) {
                AlertDialogEntity alertDialogEntity = alertDialogRepository.findOneById(commandDTO.getCommandNotificationId());
                if (alertDialogEntity == null) {
                    throw new ResourceNotFoundException("not found cnotification with id " + commandDTO.getCommandNotificationId());
                }
                commandEntity.setCommandNotificationEntity(alertDialogEntity);
            }

        } else {
            commandEntity = commandConverter.toEntity(commandDTO);
            if (commandDTO.getCommandNotificationId() != null) {
                AlertDialogEntity alertDialogEntity = alertDialogRepository.findOneById(commandDTO.getCommandNotificationId());
                if (alertDialogEntity == null) {
                    throw new ResourceNotFoundException("not found cnotification with id " + commandDTO.getCommandNotificationId());
                }
                commandEntity.setCommandNotificationEntity(alertDialogEntity);
            }
        }
        commandEntity = commandRepository.save(commandEntity);
        return commandConverter.toDTO(commandEntity);
    }

    @Override
    public CommandDTO findOne(Long id) {
        CommandEntity entity = commandRepository.findOneById(id);
        return commandConverter.toDTO(entity);
    }

    /**
     * find item with page number and totalPage number
     *
     * @param pageable
     * @return
     */
    @Override
    public List<CommandDTO> findAll(Pageable pageable) {
        List<CommandEntity> entities = commandRepository.findAll(pageable).getContent();
        List<CommandDTO> result = new ArrayList<>();
        for (CommandEntity item : entities) {
            CommandDTO commandDTO = commandConverter.toDTO(item);
            result.add(commandDTO);
        }
        return result;
    }

    @Override
    public List<CommandDTO> findAll() {
        List<CommandEntity> entities = commandRepository.findAll();
        List<CommandDTO> result = new ArrayList<>();
        for (CommandEntity item : entities) {
            CommandDTO commandDTO = commandConverter.toDTO(item);
            result.add(commandDTO);
        }
        return result;
    }

    @Override
    public int totalItem() {
        return (int) commandRepository.count();
    }

    @Override
    public void delete(Long[] ids) {
        for (Long item : ids) {
            commandRepository.deleteById(item);
        }
    }
}
