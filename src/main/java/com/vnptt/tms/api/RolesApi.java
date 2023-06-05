package com.vnptt.tms.api;

import com.vnptt.tms.dto.RolesDTO;
import com.vnptt.tms.service.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("TMS/api")
public class RolesApi {

    @Autowired
    private IRolesService roleFunctionService;

    @GetMapping(value = "/roleFunction")
    public List<RolesDTO> showAllRoleFunction() {
        return roleFunctionService.findAll();
    }

    @PostMapping(value = "/roleFunction")
    @PreAuthorize("hasRole('MODERATOR')")
    public RolesDTO createRoleFunction(@RequestBody RolesDTO model) {
        return roleFunctionService.save(model);
    }

    @DeleteMapping(value = "/roleFunction")
    @PreAuthorize("hasRole('MODERATOR')")
    public void removeroleFunction(@RequestBody Long[] ids) {
        roleFunctionService.delete(ids);
    }
}
