package com.vnptt.tms.security.services;

import com.vnptt.tms.entity.DeviceEntity;
import com.vnptt.tms.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceDetailsServiceImpl implements UserDetailsService {

    @Autowired
    DeviceRepository deviceRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String serialnumber) throws UsernameNotFoundException {
        // Check if the device exists in the database?
        DeviceEntity deviceEntity = deviceRepository.findOneBySn(serialnumber);
        if (deviceEntity == null) {
            throw new RuntimeException("Device Not Found with sn: " + serialnumber);
        }

        return DeviceDetailsImpl.build(deviceEntity);
    }

}
