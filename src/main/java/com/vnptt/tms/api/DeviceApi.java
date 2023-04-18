package com.vnptt.tms.api;

import com.vnptt.tms.api.output.ApplicationOutput;
import com.vnptt.tms.api.output.DeviceOutput;
import com.vnptt.tms.dto.DeviceDTO;
import com.vnptt.tms.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class DeviceApi {

    @Autowired
    private IDeviceService deviceService;


    @GetMapping(value = "/device")
    public DeviceOutput showDevice(@RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "limit", required = false) Integer limit,
                                   @RequestParam(value = "model", required = false) String model,
                                   @RequestParam(value = "firmware", required = false) String firmware) {
        DeviceOutput result = new DeviceOutput();
        if (model != null || firmware != null ){
            result.setListResult(deviceService.findByModelAndFirmwareVer(model, firmware));
        } else if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((deviceService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) deviceService.totalItem()/ limit));
        } else {
            result.setListResult(deviceService.findAll());
        }

        if (result.getListResult().size() >= 1){
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    @GetMapping(value ="/device/date")
    public DeviceOutput showDeviceWithDate(@RequestParam(value = "date") Date dateOfManufacture){
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findByDate(dateOfManufacture));
        if (result.getListResult().size() >= 1){
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    @GetMapping(value ="/device/location")
    public DeviceOutput showDeviceWithLocation(@RequestParam(value = "location") String location){
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findByLocation(location));
        if (result.getListResult().size() >= 1){
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    @GetMapping("/application/{applicationId}/device")
    public DeviceOutput getAllDeviceByApplicationId(@PathVariable(value = "applicationId") Long applicationId) {
        DeviceOutput result = new DeviceOutput();
        result.setListResult(deviceService.findAllWithApplication(applicationId));

        if (result.getListResult().size() >= 1){
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
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
