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

    /**
     * Get List application for web
     *
     * @param page        desired page to display
     * @param limit       number of elements 1 page
     * @param packagename the name of the app you want to find
     * @return
     */
    @GetMapping(value = "/application")
    public ApplicationOutput showApplicationes(@RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "limit", required = false) Integer limit,
                                               @RequestParam(value = "packagename", required = false) String packagename) {
        ApplicationOutput result = new ApplicationOutput();
        if (page != null && limit != null) {
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            if (packagename != null) {
                result.setListResult(applicationService.findByPackagename(packagename, pageable));
            } else {
                result.setListResult((applicationService.findAll(pageable)));
            }
            result.setTotalPage((int) Math.ceil((double) applicationService.totalItem() / limit));
        } else if (packagename != null) {
            result.setListResult(applicationService.findByPackagename(packagename));
        } else {
            result.setListResult(applicationService.findAll());
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
     * see the apps available on the device for box and web
     *
     * @param deviceId device want to search
     * @return
     */
    @GetMapping("/device/{deviceId}/application")
    public ApplicationOutput getAllApplicationByDeviceEntityId(@PathVariable(value = "deviceId") Long deviceId) {
        ApplicationOutput result = new ApplicationOutput();
        result.setListResult(applicationService.findAllOnDevice(deviceId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * unnecessary (only use to test)
     * add new app to database
     *
     * @param model dto application
     * @return
     */
    @PostMapping(value = "/application")
    public ResponseEntity<ApplicationDTO>  createApplication(@RequestBody ApplicationDTO model) {
//        return applicationService.save(model);
        return new ResponseEntity<>(applicationService.save(model), HttpStatus.OK);
    }

    /**
     * Mapp app to device for box
     *
     * @param deviceId device has app
     * @param model    dto application (need Id from responce when post new app)
     * @return
     */
    @PostMapping(value = "/device/{deviceId}/application")
    public ApplicationDTO addApplicationToDevice(@PathVariable(value = "deviceId") Long deviceId,
                                                 @RequestBody ApplicationDTO model) {
        return applicationService.addAppToDevice(deviceId, model);
    }


    /**
     * unnecessary (only use to test)
     * @param ids
     */
    @DeleteMapping(value = "/application")
    public void deleteApplication(@RequestBody Long[] ids) {
        applicationService.delete(ids);
    }

    /**
     * remove app no longer on the device
     *
     * @param deviceId
     * @param applicationId
     * @return
     */
    @DeleteMapping(value = "/device/{deviceId}/application/{applicationId}")
    public ResponseEntity<HttpStatus> removeApplicationOnDevice(@PathVariable(value = "deviceId") Long deviceId,
                                                                @PathVariable(value = "applicationId") Long applicationId) {
        applicationService.removeAppOnDevice(deviceId, applicationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
