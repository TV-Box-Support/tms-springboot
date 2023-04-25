package com.vnptt.tms.api;

import com.vnptt.tms.api.output.DeviceOutput;
import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class DeviceApi {

    @Autowired
    private IDeviceService deviceService;

    /**
     * show device with model and firmware for web
     *
     * @param page     the page you want to display
     * @param limit    element on a page
     * @param model    type STB
     * @param firmware firmware version
     * @return
     */
    @GetMapping(value = "/device")
    public DeviceOutput showDevice(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "model", required = false) String model, @RequestParam(value = "firmware", required = false) String firmware) {
        DeviceOutput result = new DeviceOutput();
        if (page != null && limit != null) {
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            if (model != null || firmware != null) {
                result.setListResult(deviceService.findByModelAndFirmwareVer(model, firmware, pageable));
            } else {
                result.setListResult((deviceService.findAll(pageable)));
            }
            result.setTotalPage((int) Math.ceil((double) deviceService.totalItem() / limit));
        } else {
            if (model != null || firmware != null) {
                result.setListResult(deviceService.findByModelAndFirmwareVer(model, firmware));
            } else {
                result.setListResult((deviceService.findAll()));
            }
        }

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * find device with id
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/device/{id}")
    public DeviceDTO showDevice(@PathVariable("id") Long id) {
        return deviceService.findOne(id);
    }

    /**
     * show device of production
     *
     * @param dateOfManufacture
     * @return
     */
    @GetMapping(value = "/device/date")
    public DeviceOutput showDeviceWithDate(@RequestParam(value = "date") Date dateOfManufacture) {
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findByDate(dateOfManufacture));
        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * Show device infor in database for box and web
     *
     * @param serialnumber
     * @return
     */
    @GetMapping(value = "/device/serialnumber")
    public DeviceDTO showDeviceWithSn(@RequestParam(value = "serialnumber") String serialnumber) {
        DeviceDTO result = new DeviceDTO();
        result = deviceService.findOneBySn(serialnumber);
        return result;
    }

    /**
     * show device with location
     *
     * @param location
     * @return
     */
    @GetMapping(value = "/device/location")
    public DeviceOutput showDeviceWithLocation(@RequestParam(value = "location") String location) {
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findByLocation(location));
        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * show device own the application
     *
     * @param applicationId
     * @return
     */
    @GetMapping("/application/{applicationId}/device")
    public DeviceOutput getAllDeviceByApplicationId(@PathVariable(value = "applicationId") Long applicationId) {
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findAllWithApplication(applicationId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * Show list device play application now
     *
     * @param ApplicationId
     * @return
     */
    @GetMapping(value = "/application/{ApplicationId}/device/now")
    public DeviceOutput showAppRunNow(@PathVariable(value = "ApplicationId") Long ApplicationId) {
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findAllDeviceRunApp(ApplicationId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            throw new ResourceNotFoundException("no matching element found");
        }
        return result;
    }

    /**
     * Show list device active now
     *
     * @return
     */
    @GetMapping(value = "/device/now")
    public DeviceOutput showdeviceRunNow() {
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findAllDeviceRunNow());

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            throw new ResourceNotFoundException("no matching element found");
        }
        return result;
    }

    /**
     * Create new device for production batch
     *
     * @param model serialNumber, dateOfManufacture, mac have required
     * @return
     */
    @PostMapping(value = "/device")
    public DeviceDTO createDevice(@RequestBody DeviceDTO model) {
        //TODO modify to create a list of device
        return deviceService.save(model);
    }

    /**
     * update device infor for Box
     *
     * @param model
     * @param id    must get and have required
     * @return
     */
    @PutMapping(value = "/device/{id}")
    public DeviceDTO updateDevice(@RequestBody DeviceDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return deviceService.save(model);
    }

    /**
     * should not be used because it affects all historical information, very dangerous
     * (only use to test)
     *
     * @param ids
     */
    @DeleteMapping(value = "/device")
    @PreAuthorize("hasRole('MODERATOR')")
    public void deleteDevice(@RequestBody Long[] ids) {
        deviceService.delete(ids);
    }
}
