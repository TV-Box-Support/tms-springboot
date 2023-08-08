package com.vnptt.tms.api;

import com.vnptt.tms.api.output.chart.AreaChartDeviceOnl;
import com.vnptt.tms.api.output.chart.AreaChartHisPerf;
import com.vnptt.tms.api.output.chart.BarChart;
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
     * api show device with model and firmware for web
     *
     * @param page   the page you want to display
     * @param limit  element on a page
     * @param search search of STB
     * @return List of device DTO
     */
    @GetMapping(value = "/device")
    public DeviceOutput showDevice(@RequestParam(value = "page") Integer page,
                                   @RequestParam(value = "limit") Integer limit,
                                   @RequestParam(value = "search", required = false) String search) {
        DeviceOutput result = new DeviceOutput();

        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (search != null) {
            result.setListResult(deviceService.findByDescriptionAndSn(search, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countByDescriptionAndSn(search) / limit));
        } else {
            result.setListResult((deviceService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) deviceService.totalItem() / limit));
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
     * api show device with SN on bar search
     *
     * @param search search of STB
     * @return List of device DTO
     */
    @GetMapping(value = "/barSearch/device")
    public DeviceOutput showDeviceOnBarSearch(@RequestParam(value = "search", required = false) String search) {
        DeviceOutput result = new DeviceOutput();

        result.setListResult((deviceService.findOnBarSearch(search)));


        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * api show device with id
     * check information for web
     *
     * @param id id of device
     * @return device DTO
     */
    @GetMapping(value = "/device/{id}")
    public DeviceDTO showDevice(@PathVariable("id") Long id) {
        return deviceService.findOne(id);
    }

    /**
     * api Show device info with serialnumber
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
     * api show device with location
     *
     * @param location
     * @return
     */
    @GetMapping(value = "/device/location")
    public DeviceOutput showDeviceWithLocation(@RequestParam(value = "page") Integer page,
                                               @RequestParam(value = "limit") Integer limit,
                                               @RequestParam(value = "description", required = false) String description,
                                               @RequestParam(value = "location") String location) {
        DeviceOutput result = new DeviceOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (description != null) {
            result.setListResult(deviceService.findByDescriptionAndLocation(location, description, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countByDescriptionAndLocation(location, description) / limit));
        } else {
            result.setListResult(deviceService.findByLocation(location, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countByLocation(location) / limit));
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
     * show device own the application
     *
     * @param applicationId
     * @return
     */
    @GetMapping("/application/{applicationId}/device")
    public DeviceOutput getAllDeviceByApplicationId(@RequestParam(value = "page") Integer page,
                                                    @RequestParam(value = "limit") Integer limit,
                                                    @PathVariable(value = "applicationId") Long applicationId,
                                                    @RequestParam(value = "search", required = false) String sn) {
        DeviceOutput result = new DeviceOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (sn != null) {
            result.setListResult(deviceService.findAllWithApplicationIdAndSn(applicationId, sn, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countByApplicationIdAndSn(applicationId, sn) / limit));
        } else {
            result.setListResult(deviceService.findAllWithApplication(applicationId, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countByApplicationId(applicationId) / limit));
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
     * api Show list device play application now
     * todo: add ui
     *
     * @param applicationId
     * @return
     */
    @GetMapping(value = "/application/{applicationId}/device/now")
    public DeviceOutput showAppRunNow(@PathVariable(value = "applicationId") Long applicationId,
                                      @RequestParam(value = "page") Integer page,
                                      @RequestParam(value = "limit") Integer limit,
                                      @RequestParam(value = "search", required = false) String sn) {
        DeviceOutput result = new DeviceOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (sn != null) {
            result.setListResult(deviceService.findAllDeviceRunAppWithSn(applicationId, sn, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countAppActiveByApplicationIdAndSn(applicationId, sn) / limit));
        } else {
            result.setListResult(deviceService.findAllDeviceRunApp(applicationId, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countAppactiveByApplicationId(applicationId) / limit));
        }

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            throw new ResourceNotFoundException("no matching element found");
        }
        return result;
    }

    /**
     * api Show list device active now
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "/device/now")
    public DeviceOutput showDeviceRunNow(@RequestParam(value = "page") Integer page,
                                         @RequestParam(value = "limit") Integer limit,
                                         @RequestParam(value = "search", required = false) String serialmunber) {
        DeviceOutput result = new DeviceOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (serialmunber != null) {
            result.setListResult(deviceService.findAllDeviceRunNowWithSN(serialmunber, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countDeviceRunNowWithSN(serialmunber) / limit));
        } else {
            result.setListResult(deviceService.findAllDeviceRunNow(pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countDeviceRunNow() / limit));
        }


        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            throw new ResourceNotFoundException("no matching element found");
        }
        return result;
    }

    /**
     * api get jwt of box (only use to test)
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
     * api show all device active in time
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
     * api show all device in list Device
     *
     * @param listDeviceId id of listdevice
     * @return list device in listDevice
     */
    @GetMapping(value = "/listDevice/{listDeviceId}/device")
    public DeviceOutput showDeviceInListDevice(@RequestParam(value = "page") Integer page,
                                               @RequestParam(value = "limit") Integer limit,
                                               @PathVariable(name = "listDeviceId") Long listDeviceId,
                                               @RequestParam(value = "search", required = false) String serialmunber) {
        DeviceOutput result = new DeviceOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (serialmunber != null) {
            result.setListResult(deviceService.findDeviceInListDeviceWithSn(listDeviceId, serialmunber, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countDeviceinListDeviceWithSn(listDeviceId, serialmunber) / limit));
        } else {
            result.setListResult(deviceService.findDeviceInListDevice(listDeviceId, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countDeviceinListDevice(listDeviceId) / limit));
        }
        ;

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * api get terminal studio info
     *
     * @return
     */
    @GetMapping(value = "/terminalStudio/device")
    public TerminalStudioOutput showTerminalStudioInfo() {
        TerminalStudioOutput result = deviceService.updateTerminalStudioInfo();
        return result;
    }

    /**
     * api get device has policy
     *
     * @return
     */
    @GetMapping(value = "/policy/{policyId}/device")
    public DeviceOutput showDeviceHasPolicy(@PathVariable(value = "policyId") Long policyId,
                                            @RequestParam(value = "page") Integer page,
                                            @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "search", required = false) String sn) {
        DeviceOutput result = new DeviceOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (sn != null) {
            result.setListResult(deviceService.findDeviceWithPolicyIdAndSN(policyId, sn, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countDeviceWithPolicyIdAndSn(policyId, sn) / limit));
        } else {
            result.setListResult(deviceService.findDeviceWithPolicyId(policyId, pageable));
            result.setTotalPage((int) Math.ceil((double) deviceService.countDeviceWithPolicyId(policyId) / limit));
        }

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            throw new ResourceNotFoundException("no matching element found");
        }
        return result;

    }


    /**
     * api Show total device online 7 day ago
     *
     * @return
     */
    @GetMapping(value = "/chart/bar/device")
    public List<BarChart> showBarChartDeviceStatus() {
        List<BarChart> result = deviceService.getTotalBarChart();
        return result;
    }

    /**
     * api Show device online offline and not active
     *
     * @param type
     * @return
     */
    @GetMapping(value = "/chart/pie/device")
    public List<PieChart> showTotalDeviceStatus(@RequestParam(name = "type") String type) {
        List<PieChart> result = deviceService.getTotalPieChart(type);
        return result;
    }

    /**
     * api Show history online day now
     *
     * @param dayAgo
     * @return
     */
    @GetMapping(value = "/chart/area/device/{id}")
    public List<AreaChartHisPerf> showAreaChartStatus(@RequestParam(name = "dayago") Integer dayAgo,
                                                      @PathVariable(name = "id") Long id) {
        List<AreaChartHisPerf> result = deviceService.getAreaChartStatus(dayAgo, id);
        return result;
    }

    /**
     * api Show history online for 30 day
     *
     * @return
     */
    @GetMapping(value = "/chart/area/device")
    public List<AreaChartDeviceOnl> showAreaChartDeviceOnline() {
        List<AreaChartDeviceOnl> result = deviceService.getAreaChartDeviceOnline();
        return result;
    }

    /**
     * api Show time spend 1 week
     *
     * @return
     */
    @GetMapping(value = "/chart/area/device/device/{deviceId}")
    public List<AreaChartDeviceOnl> showAreaChartDeviceTime(@PathVariable(value = "deviceId") Long deviceId) {
        List<AreaChartDeviceOnl> result = deviceService.getAreaChartDeviceTime(deviceId);
        return result;
    }


    /**
     * api Create new device for production batch
     *
     * @param model serialNumber have required
     * @return
     */
    @PostMapping(value = "/device")
    public DeviceDTO createDevice(@RequestBody DeviceDTO model) {
        return deviceService.save(model);
    }

    /**
     * api add device to listDevice
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
     * api update device info for web (description)
     *
     * @param model device info
     * @param id    must get and have required
     * @return device info after update
     */
    @PutMapping(value = "/device/{id}")
    public DeviceDTO updateDevice(@RequestBody DeviceDTO model,
                                  @PathVariable("id") Long id) {
        model.setId(id);
        return deviceService.save(model);
    }

    /**
     * api update device info for Box
     *
     * @param model device info
     * @param sn    sn of box
     * @return
     */
    @PutMapping(value = "/device/box")
    public DeviceDTO updateDeviceBox(@RequestBody DeviceDTO model,
                                     @RequestParam(value = "serialnumber") String sn) {
        return deviceService.boxUpdate(sn, model);
    }

    /**
     * api Remove device in List Device
     *
     * @param listDeviceId list Device Id
     * @param deviceId     device Id
     * @return https 200
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
