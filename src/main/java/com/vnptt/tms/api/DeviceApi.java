package com.vnptt.tms.api;

import com.vnptt.tms.api.output.chart.AreaChart;
import com.vnptt.tms.api.output.chart.PieChart;
import com.vnptt.tms.api.output.studio.TerminalStudioOutput;
import com.vnptt.tms.api.output.table.DeviceOutput;
import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.security.jwt.JwtUtils;
import com.vnptt.tms.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.List;

/**
 * Application Programming Interface for device manager
 * include:
 * <p>
 * - get list device from database pageable or none
 * - get single device form database by id
 * - show (get mothod) device with production
 * <p>
 * ...
 */
@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class DeviceApi {

    @Autowired
    private IDeviceService deviceService;

    // add for get jwt of box
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * show device with model and firmware for web
     *
     * @param page     the page you want to display
     * @param limit    element on a page
     * @param model    type STB
     * @param firmware firmware version
     * @return List of device DTO
     */
    @GetMapping(value = "/device")
    public DeviceOutput showDevice(@RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "limit", required = false) Integer limit,
                                   @RequestParam(value = "model", required = false) String model,
                                   @RequestParam(value = "firmware", required = false) String firmware) {
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
     * check information for web
     *
     * @param id if of device
     * @return device DTO
     */
    @GetMapping(value = "/device/{id}")
    public DeviceDTO showDevice(@PathVariable("id") Long id) {
        return deviceService.findOne(id);
    }

    /**
     * show device with production
     *
     * @param dateOfManufacture ex 2022-12-30
     * @return list device DTO
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
     * Show device info with serialnumber
     *
     * @param serialnumber sb of device, unique field in database
     * @return device DTO
     */
    @GetMapping(value = "/device/serialnumber")
    public DeviceDTO showDeviceWithSn(HttpServletRequest request,
                                      @RequestParam(value = "serialnumber") String serialnumber) {
        DeviceDTO result = new DeviceDTO();
        result = deviceService.findOneBySn(request.getRemoteAddr(), serialnumber);
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
     * @param applicationId
     * @return
     */
    @GetMapping(value = "/application/{applicationId}/device/now")
    public DeviceOutput showAppRunNow(@PathVariable(value = "applicationId") Long applicationId) {
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findAllDeviceRunApp(applicationId));

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
    public DeviceOutput showDeviceRunNow(@RequestParam(value = "page") Integer page,
                                         @RequestParam(value = "limit") Integer limit) {
        DeviceOutput result = new DeviceOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(deviceService.findAllDeviceRunNow(pageable));
        result.setTotalPage((int) Math.ceil((double) deviceService.countDeviceRunNow() / limit));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            throw new ResourceNotFoundException("no matching element found");
        }
        return result;
    }

    /**
     * get jwt of box (only use to test)
     *
     * @param serialnumber
     * @param mac
     * @return
     */
    @GetMapping(value = "/device/jwt")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> showDeviceJwt(@RequestParam String serialnumber,
                                           @RequestParam String mac) {
        return deviceService.authenticateDevice(serialnumber, mac);
    }

    /**
     * find all device active with time
     *
     * @param day
     * @param hour
     * @param minutes
     * @return
     */
    @GetMapping(value = "/device/active")
    public DeviceOutput showDeviceActive(@RequestParam(value = "page") Integer page,
                                         @RequestParam(value = "limit") Integer limit,
                                         @RequestParam(value = "day") int day,
                                         @RequestParam(value = "hour") long hour,
                                         @RequestParam(value = "minutes") int minutes) {
        DeviceOutput result = new DeviceOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        result.setListResult(deviceService.findDeviceActive(day, hour, minutes, pageable));
        result.setTotalPage((int) Math.ceil((double) deviceService.countDeviceActive(day, hour, minutes) / limit));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * get all device in list Device
     *
     * @param listDeviceId
     * @return
     */
    @GetMapping(value = "/listDevice/{listDeviceId}/device")
    public DeviceOutput showDeviceInListDevice(@PathVariable(name = "listDeviceId") Long listDeviceId) {
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findDeviceInListDevice(listDeviceId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * get terminal studio info
     *
     * @return
     */
    @GetMapping(value = "/terminalStudio/device")
    public TerminalStudioOutput showTerminalStudioInfo() {
        TerminalStudioOutput result = deviceService.updateTerminalStudioInfo();
        return result;
    }

    @GetMapping(value = "/chart/area/device")
    public List<AreaChart> showAreaChartDeviceStatus() {
        List<AreaChart> result = deviceService.getTotalAreaChart();
        return result;
    }

    @GetMapping(value = "/chart/pie/device")
    public List<PieChart> showTotalDeviceStatus(@RequestParam(name = "type") String type) {
        List<PieChart> result = deviceService.getTotalPieChart(type);
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
        return deviceService.save(model);
    }

    /**
     * add device to list to management
     *
     * @param listDeviceId id of List Device want to add
     * @param deviceIds    list id of device add
     * @return listDevice had add
     */
    @PostMapping(value = "/listDevice/{listDeviceId}/device")
    public DeviceOutput addDeviceToListDevice(@PathVariable(value = "listDeviceId") Long listDeviceId,
                                              @RequestBody Long[] deviceIds) {
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.mapDeviceToListDevice(listDeviceId, deviceIds));
        if (result.getListResult().size() >= 1) {
            result.setMessage("Get List ListDevice success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }

        return result;
    }

    /**
     * update device info for Box
     *
     * @param model
     * @param id    must get and have required
     * @return
     */
    @PutMapping(value = "/device/{id}")
    public DeviceDTO updateDevice(@RequestBody DeviceDTO model,
                                  @PathVariable("id") Long id) {
        model.setId(id);
        return deviceService.save(model);
    }

    /**
     * update device info for Box
     *
     * @param model
     * @param sn
     * @return
     */
    @PutMapping(value = "/device/box")
    public DeviceDTO updateDeviceBox(@RequestBody DeviceDTO model,
                                     @RequestParam(value = "serialnumber") String sn) {
        return deviceService.boxUpdate(sn, model);
    }

    /**
     * Remove device in List Device
     *
     * @param listDeviceId list Device Id
     * @param deviceId     device Id
     * @return
     */
    @DeleteMapping(value = "/listDevice/{listDeviceId}/device/{deviceId}")
    public ResponseEntity<HttpStatus> removeDeviceInListDevice(@PathVariable(value = "listDeviceId") Long listDeviceId,
                                                               @PathVariable(value = "deviceId") Long deviceId) {
        deviceService.removeDeviceinListDevice(listDeviceId, deviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
