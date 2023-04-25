package com.vnptt.tms.api;

import com.vnptt.tms.dto.RuleDTO;
import com.vnptt.tms.service.IRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("TMS/api")
public class RuleApi {
    @Autowired
    private IRuleService ruleService;

    @GetMapping(value = "/rule")
    public List<RuleDTO> showAllRule() {
        return ruleService.findAll();
    }

    @PostMapping(value = "/rule")
    @PreAuthorize("hasRole('MODERATOR')")
    public RuleDTO createRule(@RequestBody RuleDTO model) {
        return ruleService.save(model);
    }

    @DeleteMapping(value = "/rule")
    @PreAuthorize("hasRole('MODERATOR')")
    public void removeRule(@RequestBody Long[] ids) {
        ruleService.delete(ids);
    }
}
