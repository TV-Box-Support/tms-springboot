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
    private IRuleService iRuleService;

    @GetMapping(value = "/rule/{id}")
    public RuleDTO showCategory(@PathVariable("id") Long id) {
        return iRuleService.findOne(id);
    }

    @GetMapping(value = "/rule")
    public List<RuleDTO> showAllCategory(){
        return iRuleService.findAll();
    }
}
