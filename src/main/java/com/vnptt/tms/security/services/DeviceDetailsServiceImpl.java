package com.vnptt.tms.security.services;

import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.entity.ListDeviceEntity;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.repository.DeviceRepository;
import com.vnptt.tms.repository.ListDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ListDeviceRepository listDeviceRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String serialnumber) throws UsernameNotFoundException {
        // Check if the device exists in the database?
        // Username = serialnumber
        DeviceEntity deviceEntity = deviceRepository.findOneBySn(serialnumber);
        if (deviceEntity == null) {
            deviceEntity = new DeviceEntity();
            deviceEntity.setSn(serialnumber);
            deviceEntity = deviceRepository.save(deviceEntity);
            ListDeviceEntity listDeviceEntity = listDeviceRepository.findOneByName("all");
            if (listDeviceEntity == null) {
                throw new ResourceNotFoundException("miss list device all device!");
            }
            listDeviceEntity.addDevice(deviceEntity);
            listDeviceRepository.save(listDeviceEntity);
        }
        return DeviceDetailsImpl.build(deviceEntity);
    }

}
