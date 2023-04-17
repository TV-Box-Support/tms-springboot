package com.vnptt.tms.api;

import com.vnptt.tms.api.output.PolicyOutput;
import com.vnptt.tms.dto.PolicyDTO;
import com.vnptt.tms.service.IPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class PolicyApi {

    @Autowired
    private IPolicyService policyService;

    @GetMapping(value = "/policy")
    public PolicyOutput showPolicy(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "limit", required = false) Integer limit) {
        PolicyOutput result = new PolicyOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((policyService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) policyService.totalItem()/ limit));
        } else {
            result.setListResult(policyService.findAll());
        }
        return result;
    }

    @PostMapping(value = "/policy")
    public PolicyDTO createPolicy(@RequestBody PolicyDTO model) {
        return policyService.save(model);
    }

    @PutMapping(value = "/policy/{id}")
    public PolicyDTO updatePolicy(@RequestBody PolicyDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return policyService.save(model);
    }

    @DeleteMapping(value = "/policy")
    public void updatePolicy(@RequestBody Long[] ids) {
        policyService.delete(ids);
    }
}
