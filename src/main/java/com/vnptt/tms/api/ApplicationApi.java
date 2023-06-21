package com.vnptt.tms.api;

import com.vnptt.tms.api.output.table.ApplicationOutput;
import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.service.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Application Programming Interface for application manager
 * include:
 * <p>
 * - get list application from database pageable or none
 * - get single application form database by id
 * - show (get method) all apps available on the box
 * - post application to database
 * - Map (post method) app to device if database don't have app, create and add app to database
 * <p>
 * ...
 */
@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class ApplicationApi {

    @Autowired
    private IApplicationService applicationService;

    /**
     * Get List application with any version for web
     *
     * @param page        desired page to display
     * @param limit       number of elements 1 page
     * @param packagename the name of the app you want to find
     * @return List app DTO
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
                result.setTotalPage((int) Math.ceil((double) applicationService.countByPackagename(packagename) / limit));
            } else {
                result.setListResult((applicationService.findAll(pageable)));
                result.setTotalPage((int) Math.ceil((double) applicationService.totalItem() / limit));
            }
        } else if (packagename != null) {
            result.setListResult(applicationService.findByPackagename(packagename));
        } else {
            result.setListResult(applicationService.findAll());
        }

        if (result.getListResult().size() >= 1) {
            result.setMessage("Get List application success");
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
    @GetMapping(value = "/application/{id}")
    public ApplicationDTO showApplication(@PathVariable("id") Long id) {
        return applicationService.findOne(id);
    }


    /**
     * see the apps available on the device for web
     *
     * @param deviceId device want to search
     * @param isSystem true/false/null
     * @param name     name of application want to search
     * @return List application DTO
     */
    @GetMapping("/device/{deviceId}/application")
    public ApplicationOutput getAllApplicationByDeviceEntityId(@RequestParam(value = "page") Integer page,
                                                               @RequestParam(value = "limit") Integer limit,
                                                               @PathVariable(value = "deviceId") Long deviceId,
                                                               @RequestParam(value = "isAlive") Boolean isAlive,
                                                               @RequestParam(value = "isSystem", required = false) Boolean isSystem,
                                                               @RequestParam(value = "name", required = false) String name) {
        ApplicationOutput result = new ApplicationOutput();

        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (isSystem == null && name == null) {
            result.setListResult(applicationService.findAllOnDevice(deviceId, pageable));
            result.setTotalPage((int) Math.ceil((double) applicationService.countByDeviceId(deviceId) / limit));
        } else {
            if (isSystem == null) {
                isSystem = false;
            }
            if (isAlive == null) {
                isAlive = true;
            }
            result.setListResult(applicationService.findAllWithDeviceNameIsSystem(deviceId, name, isAlive, isSystem, pageable));
            result.setTotalPage((int) Math.ceil((double) applicationService.countWithDeviceNameIsSystem(deviceId, name, isAlive, isSystem) / limit));
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
     * see the apps available on the device for box
     *
     * @return List application DTO
     */
    @GetMapping("/device/application")
    public ApplicationOutput getAllApplicationAliveForBox(@RequestParam(value = "serialnumber") String sn) {
        ApplicationOutput result = new ApplicationOutput();

        result.setListResult(applicationService.findAllApplicationAliveOnBox(sn));

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
     * because device only use addApplicationToDevice
     * add new app to database
     *
     * @param model dto application
     * @return http status ok 200
     */
    @PostMapping(value = "/application")
    public ResponseEntity<ApplicationDTO> createApplication(@RequestBody ApplicationDTO model) {
        return new ResponseEntity<>(applicationService.save(model), HttpStatus.OK);
    }

    /**
     * Map app to device for box, if database don't have app, create and add
     *
     * @param model dto application (need id from response when post new app)
     * @return
     */
    @PostMapping(value = "/device/application")
    public ApplicationDTO addApplicationToDevice(@RequestParam(value = "serialnumber") String sn,
                                                 @RequestBody ApplicationDTO model) {
        return applicationService.addAppToDevice(sn, model);
    }

    /**
     * unnecessary (only use to test)
     *
     * @param ids list id of app want to delete ex: [1,2,3]
     */
    @DeleteMapping(value = "/application")
    @PreAuthorize("hasRole('MODERATOR')")
    public void deleteApplication(@RequestBody Long[] ids) {
        applicationService.delete(ids);
    }

}
