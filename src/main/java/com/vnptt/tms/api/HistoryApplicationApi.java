package com.vnptt.tms.api;

import com.vnptt.tms.api.output.HistoryApplicationOutput;
import com.vnptt.tms.dto.HistoryApplicationDTO;
import com.vnptt.tms.service.IHistoryApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class HistoryApplicationApi {

    @Autowired
    private IHistoryApplicationService historyApplicationService;

    @GetMapping(value = "/historyApplication")
    public HistoryApplicationOutput showHistoryApplication(@RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "limit", required = false) Integer limit) {
        HistoryApplicationOutput result = new HistoryApplicationOutput();
        if (page != null && limit != null) {
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            result.setListResult((historyApplicationService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) historyApplicationService.totalItem() / limit));
        } else {
            result.setListResult(historyApplicationService.findAll());
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
     * find HistoryApplication with id
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/historyApplication/{id}")
    public HistoryApplicationDTO showHistoryApplication(@PathVariable("id") Long id) {
        return historyApplicationService.findOne(id);
    }

    /**
     * find all history application of device
     *
     * @param deviceId
     * @param day
     * @param hour
     * @param minutes
     * @return
     */
    @GetMapping(value = "/device/{deviceId}/application/{applicationId}/historyApplication")
    public HistoryApplicationOutput showHistoryApplicationDevice(@PathVariable("deviceId") Long deviceId,
                                                                 @PathVariable("applicationId") Long applicationId,
                                                                 @RequestParam(value = "day") int day,
                                                                 @RequestParam(value = "hour") long hour,
                                                                 @RequestParam(value = "minutes") int minutes) {
        HistoryApplicationOutput result = new HistoryApplicationOutput();
        result.setListResult(historyApplicationService.findHistoryAppDeviceLater(deviceId, applicationId, day, hour, minutes));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("app don't run in " + day + " day " + hour + " hour " + minutes + " minutes late.");
        }
        return result;
    }

    /**
     * Add new history application
     *
     * @param deviceId
     * @param applicationId
     * @param model         (cpu, memory, status)
     * @return
     */
    @PostMapping(value = "/device/{deviceId}/application/{applicationId}/historyApplication")
    public HistoryApplicationDTO createHistoryApplication(@PathVariable(value = "deviceId") Long deviceId,
                                                          @PathVariable(value = "applicationId") Long applicationId,
                                                          @RequestBody HistoryApplicationDTO model) {
        model.setApplicationId(applicationId);
        model.setDeviceId(deviceId);
        return historyApplicationService.save(model);
    }

    /**
     * meaningless (only use to test)
     *
     * @param model
     * @param id
     * @return
     */
    @PutMapping(value = "/historyApplication/{id}")
    public HistoryApplicationDTO updateHistoryApplication(@RequestBody HistoryApplicationDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return historyApplicationService.save(model);
    }

    @DeleteMapping(value = "/historyApplication")
    @PreAuthorize("hasRole('MODERATOR')")
    public void removeHistoryApplication(@RequestBody Long[] ids) {
        historyApplicationService.delete(ids);
    }
}
