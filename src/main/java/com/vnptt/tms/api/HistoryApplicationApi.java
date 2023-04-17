package com.vnptt.tms.api;

import com.vnptt.tms.api.output.HistoryApplicationOutput;
import com.vnptt.tms.dto.HistoryApplicationDTO;
import com.vnptt.tms.service.IHistoryApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((historyApplicationService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) historyApplicationService.totalItem()/ limit));
        } else {
            result.setListResult(historyApplicationService.findAll());
        }
        return result;
    }

    @PostMapping(value = "/historyApplication")
    public HistoryApplicationDTO createHistoryApplication(@RequestBody HistoryApplicationDTO model) {
        return historyApplicationService.save(model);
    }

    @PutMapping(value = "/historyApplication/{id}")
    public HistoryApplicationDTO updateHistoryApplication(@RequestBody HistoryApplicationDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return historyApplicationService.save(model);
    }

    @DeleteMapping(value = "/historyApplication")
    public void updateHistoryApplication(@RequestBody Long[] ids) {
        historyApplicationService.delete(ids);
    }
}
