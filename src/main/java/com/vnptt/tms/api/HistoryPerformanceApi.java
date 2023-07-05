package com.vnptt.tms.api;

import com.vnptt.tms.api.output.box.PolicyBoxOutput;
import com.vnptt.tms.api.output.table.HistoryPerformanceOutput;
import com.vnptt.tms.dto.HistoryPerformanceDTO;
import com.vnptt.tms.service.IHistoryPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class HistoryPerformanceApi {

    @Autowired
    private IHistoryPerformanceService historyPerformanceService;

    /**
     * unrealistic (only use to test)
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "/historyPerformance")
    public HistoryPerformanceOutput showHistoryPerformance(@RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "limit", required = false) Integer limit) {
        HistoryPerformanceOutput result = new HistoryPerformanceOutput();
        if (page != null && limit != null) {
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            result.setListResult((historyPerformanceService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) historyPerformanceService.totalItem() / limit));
        } else {
            result.setListResult(historyPerformanceService.findAll());
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
     * meaningless (only use to test)
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/historyPerformance/{id}")
    public HistoryPerformanceDTO showHistoryPerformance(@PathVariable("id") Long id) {
        return historyPerformanceService.findOne(id);
    }

    /**
     * find all history performance of device
     *
     * @param deviceId
     * @param day
     * @param hour
     * @param minutes
     * @return
     */
    @GetMapping(value = "/device/{id}/historyPerformance")
    public HistoryPerformanceOutput showHistoryPerformanceDevice(@PathVariable("id") Long deviceId,
                                                                 @RequestParam(value = "day") int day,
                                                                 @RequestParam(value = "hour") long hour,
                                                                 @RequestParam(value = "minutes") int minutes) {
        HistoryPerformanceOutput result = new HistoryPerformanceOutput();
        result.setListResult(historyPerformanceService.findHistoryLater(deviceId, day, hour, minutes));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }


    /**
     * add new history performance for Box
     * and return task for box
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/historyPerformance")
    public PolicyBoxOutput createHistoryPerformance(@RequestBody HistoryPerformanceDTO model) {
        PolicyBoxOutput result = new PolicyBoxOutput();
        result.setListResult(historyPerformanceService.save(model));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    @DeleteMapping(value = "/historyPerformance")
    @PreAuthorize("hasRole('MODERATOR')")
    public void removeHistoryPerformance(@RequestBody Long[] ids) {
        historyPerformanceService.delete(ids);
    }
}
