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

    // Khi một phương thức được đánh dấu bằng @Transactional, nó tạo ra một giao dịch (transaction)
    // xung quanh các thao tác trên cơ sở dữ liệu
    // Giao dịch có thể được xem như một gói chứa các thao tác dữ liệu, và nó đảm bảo rằng các thao tác đó được
    // thực hiện hoàn toàn hoặc không được thực hiện chút nào. Nếu một thao tác bị lỗi, giao dịch sẽ tự động được
    // rollback (quay trở lại trạng thái trước khi giao dịch bắt đầu), để đảm bảo tính nhất quán của dữ liệu.
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
