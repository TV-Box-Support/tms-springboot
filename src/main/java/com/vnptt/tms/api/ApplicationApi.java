package com.vnptt.tms.api;

import com.vnptt.tms.api.output.ApplicationOutput;
import com.vnptt.tms.dto.ApplicationDTO;
import com.vnptt.tms.service.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class ApplicationApi {

    @Autowired
    private IApplicationService applicationService;

    @GetMapping(value = "/application")
    public ApplicationOutput showApplication(@RequestParam(value = "page", required = false) Integer page,
                                             @RequestParam(value = "limit", required = false) Integer limit) {
        ApplicationOutput result = new ApplicationOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((applicationService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) applicationService.totalItem()/ limit));
        } else {
            result.setListResult(applicationService.findAll());
        }
        return result;
    }

    @PostMapping(value = "/application")
    public ApplicationDTO createApplication(@RequestBody ApplicationDTO model) {
        return applicationService.save(model);
    }

    @PutMapping(value = "/application/{id}")
    public ApplicationDTO updateApplication(@RequestBody ApplicationDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return applicationService.save(model);
    }

    @DeleteMapping(value = "/application")
    public void updateApplication(@RequestBody Long[] ids) {
        applicationService.delete(ids);
    }
}
