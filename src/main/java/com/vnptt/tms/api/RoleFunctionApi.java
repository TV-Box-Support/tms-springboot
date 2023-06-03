package com.vnptt.tms.api;

import com.vnptt.tms.dto.RoleFunctionDTO;
import com.vnptt.tms.service.IRoleFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("TMS/api")
public class RoleFunctionApi {

    @Autowired
    private IRoleFunctionService roleFunctionService;

    @GetMapping(value = "/roleFunction")
    public List<RoleFunctionDTO> showAllRoleFunction() {
        return roleFunctionService.findAll();
    }

    @PostMapping(value = "/roleFunction")
    @PreAuthorize("hasRole('MODERATOR')")
    public RoleFunctionDTO createRoleFunction(@RequestBody RoleFunctionDTO model) {
        return roleFunctionService.save(model);
    }

    @DeleteMapping(value = "/roleFunction")
    @PreAuthorize("hasRole('MODERATOR')")
    public void removeroleFunction(@RequestBody Long[] ids) {
        roleFunctionService.delete(ids);
    }
}
