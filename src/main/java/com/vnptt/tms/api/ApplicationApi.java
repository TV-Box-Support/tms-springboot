package com.vnptt.tms.api;

import com.vnptt.tms.api.output.ApplicationOutput;
import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.service.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class ApplicationApi {

    @Autowired
    private IApplicationService applicationService;

    @GetMapping(value = "/application")
    public ApplicationOutput showApplicationes(@RequestParam(value = "page", required = false) Integer page,
                                             @RequestParam(value = "limit", required = false) Integer limit,
                                               @RequestParam(value = "packagename", required = false) String packagename) {
        ApplicationOutput result = new ApplicationOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((applicationService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) applicationService.totalItem()/ limit));
        } else if(packagename != null){
            result.setListResult(applicationService.findByPackagename(packagename));
        } else {
            result.setListResult(applicationService.findAll());
        }
        if (result.getListResult().size() >= 1){
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }


    @GetMapping("/device/{deviceId}/application")
    public ApplicationOutput getAllApplicationByDeviceEntityId(@PathVariable(value = "deviceId") Long deviceId,
                                                               @RequestParam(value = "page", required = false) Integer page,
                                                               @RequestParam(value = "limit", required = false) Integer limit) {
        ApplicationOutput result = new ApplicationOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((applicationService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) applicationService.totalItem()/ limit));
        } else {
            result.setListResult(applicationService.findAllOnDevice(deviceId));
        }

        if (result.getListResult().size() >= 1){
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    @PostMapping(value = "/application")
    public ApplicationDTO createApplication(@RequestBody ApplicationDTO model) {
        return applicationService.save(model);
    }

    @PostMapping(value = "/device/{deviceId}/application")
    public ApplicationDTO addApplicationToDevice(@PathVariable(value = "deviceId") Long deviceId, @RequestBody ApplicationDTO model) {
        return applicationService.addAppToDevice(deviceId, model);
    }

    @PutMapping(value = "/application/{id}")
    public ApplicationDTO updateApplication(@RequestBody ApplicationDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return applicationService.save(model);
    }

    @DeleteMapping(value = "/application")
    public void deleteApplication(@RequestBody Long[] ids) {
        applicationService.delete(ids);
    }

    @DeleteMapping(value = "/device/{deviceId}/application/{applicationId}")
    public ResponseEntity<HttpStatus> removeApplicationOnDevice(@PathVariable(value = "deviceId") Long deviceId,
                                                                @PathVariable(value = "applicationId") Long applicationId){
        applicationService.removeAppOnDevice(deviceId, applicationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
