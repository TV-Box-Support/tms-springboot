package com.vnptt.tms.api;

import com.vnptt.tms.dto.RuleDTO;
import com.vnptt.tms.service.IRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class RuleApi {
    @Autowired
    private IRuleService ruleService;

    @GetMapping(value = "/rule")
    public List<RuleDTO> showAllCategory() {
        return ruleService.findAll();
    }

    @PostMapping(value = "/rule")
    public RuleDTO createRule(@RequestBody RuleDTO model) {
        return ruleService.save(model);
    }

    @DeleteMapping(value = "/rule")
    public void removeRule(@RequestBody Long[] ids) {
        ruleService.delete(ids);
    }
}
