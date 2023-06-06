package com.vnptt.tms.api;


import com.vnptt.tms.api.output.DeviceApplicationOutput;
import com.vnptt.tms.dto.DeviceApplicationDTO;
import com.vnptt.tms.service.IDeviceApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class DeviceApplicationApi {

    @Autowired
    private IDeviceApplicationService deviceApplicationService;

    /**
     * Get List device application for web
     *
     * @param page  desired page to display
     * @param limit number of elements 1 page
     * @return List app DTO
     */
    @GetMapping(value = "/deviceApplication")
    public DeviceApplicationOutput showDeviceApplication(@RequestParam(value = "page", required = false) Integer page,
                                                         @RequestParam(value = "limit", required = false) Integer limit,
                                                         @RequestParam(value = "name", required = false) String name) {
        DeviceApplicationOutput result = new DeviceApplicationOutput();
        if (page != null && limit != null) {
            if (name == null) {
                result.setPage(page);
                Pageable pageable = PageRequest.of(page - 1, limit);
                result.setListResult((deviceApplicationService.findAll(pageable)));
                result.setTotalPage((int) Math.ceil((double) deviceApplicationService.totalItem() / limit));
            } else {
                result.setPage(page);
                Pageable pageable = PageRequest.of(page - 1, limit);
                result.setListResult((deviceApplicationService.findAllWithName(name, pageable)));
                result.setTotalPage((int) Math.ceil((double) deviceApplicationService.countAllWithName(name) / limit));
            }
        } else {
            if(name != null){
                throw new RuntimeException("search must be had page value and limit value!");
            }
            result.setListResult(deviceApplicationService.findAll());
        }

        if (result.getListResult().size() >= 1) {
            result.setMessage("Get List deviceApplication success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }

        return result;
    }

    /**
     * show application with id
     *
     * @param id id of app want to show
     * @return app DTO
     */
    @GetMapping(value = "/deviceApplication/{id}")
    public DeviceApplicationDTO showDeviceApplication(@PathVariable("id") Long id) {
        return deviceApplicationService.findOne(id);
    }

    /**
     * unnecessary (only use to test)
     * because used better api in ApplicationAPI folder
     * and device only use thí addApplicationToDevice api to
     * add new app to database
     *
     * @return http status ok 200
     */
    @PostMapping(value = "/device/{deviceId}/application/{applicationId}/deviceApplication")
    public ResponseEntity<DeviceApplicationDTO> createDeviceApplication(@PathVariable("deviceId") Long deviceId,
                                                                        @PathVariable("applicationId") Long applicationId) {
        return new ResponseEntity<>(deviceApplicationService.save(deviceId, applicationId), HttpStatus.OK);
    }

    @PutMapping(value = "/device/{deviceId}/application/{applicationId}/deviceApplication")
    public ResponseEntity<DeviceApplicationDTO> updateDeviceApplication(@PathVariable("deviceId") Long deviceId,
                                                                        @PathVariable("applicationId") Long applicationId) {
        return new ResponseEntity<>(deviceApplicationService.update(deviceId, applicationId), HttpStatus.OK);
    }

    /**
     * remove app no longer on the device
     *
     * @param deviceId      id of device need modify app list
     * @param applicationId id of application need remove
     * @return http status 204
     */
    @DeleteMapping(value = "/device/{deviceId}/application/{applicationId}")
    public ResponseEntity<HttpStatus> removeApplicationOnDevice(@PathVariable(value = "deviceId") Long deviceId,
                                                                @PathVariable(value = "applicationId") Long applicationId) {
        deviceApplicationService.removeAppOnDevice(deviceId, applicationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * unnecessary (only use to test)
     *
     * @param ids list id of app want to delete ex: [1,2,3]
     */
    @DeleteMapping(value = "/deviceApplication")
    @PreAuthorize("hasRole('MODERATOR')")
    public void deleteDeviceApplication(@RequestBody Long[] ids) {
        deviceApplicationService.delete(ids);
    }
}
