package com.vnptt.tms.api;

import com.vnptt.tms.api.output.DeviceOutput;
import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class DeviceApi {

    @Autowired
    private IDeviceService deviceService;


    @GetMapping(value = "/device")
    public DeviceOutput showDevice(@RequestParam(value = "page", required = false) Integer page,
                                 @RequestParam(value = "limit", required = false) Integer limit) {
        DeviceOutput result = new DeviceOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((deviceService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) deviceService.totalItem()/ limit));
        } else {
            result.setListResult(deviceService.findAll());
        }
        return result;
    }

    @PostMapping(value = "/device")
    public DeviceDTO createDevice(@RequestBody DeviceDTO model) {
        return deviceService.save(model);
    }

    @PutMapping(value = "/device/{id}")
    public DeviceDTO updateDevice(@RequestBody DeviceDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return deviceService.save(model);
    }

    @DeleteMapping(value = "/device")
    public void deleteDevice(@RequestBody Long[] ids) {
        deviceService.delete(ids);
    }
}
