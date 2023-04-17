package com.vnptt.tms.api;

import com.vnptt.tms.api.output.HistoryPerformanceOutput;
import com.vnptt.tms.dto.HistoryPerformanceDTO;
import com.vnptt.tms.service.IHistoryPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class HistoryPerformanceApi {

    @Autowired
    private IHistoryPerformanceService historyPerformanceService;

    @GetMapping(value = "/historyPerformance")
    public HistoryPerformanceOutput showHistoryPerformance(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "limit", required = false) Integer limit) {
        HistoryPerformanceOutput result = new HistoryPerformanceOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((historyPerformanceService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) historyPerformanceService.totalItem()/ limit));
        } else {
            result.setListResult(historyPerformanceService.findAll());
        }
        return result;
    }

    @PostMapping(value = "/historyPerformance")
    public HistoryPerformanceDTO createHistoryPerformance(@RequestBody HistoryPerformanceDTO model) {
        return historyPerformanceService.save(model);
    }

    @PutMapping(value = "/historyPerformance/{id}")
    public HistoryPerformanceDTO updateHistoryPerformance(@RequestBody HistoryPerformanceDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return historyPerformanceService.save(model);
    }

    @DeleteMapping(value = "/historyPerformance")
    public void updateHistoryPerformance(@RequestBody Long[] ids) {
        historyPerformanceService.delete(ids);
    }
}
