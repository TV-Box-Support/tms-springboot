package com.vnptt.tms.api;

import com.vnptt.tms.dto.RoleManagementDTO;
import com.vnptt.tms.service.IRoleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("TMS/api")
public class RoleManagementApi {

    @Autowired
    private IRoleManagementService roleManagementService;

    @GetMapping(value = "/roleManagement")
    public List<RoleManagementDTO> showAllRoleManagement() {
        return roleManagementService.findAll();
    }

    @PostMapping(value = "/roleManagement")
    @PreAuthorize("hasRole('MODERATOR')")
    public RoleManagementDTO createRoleManagement(@RequestBody RoleManagementDTO model) {
        return roleManagementService.save(model);
    }

    @DeleteMapping(value = "/roleManagement")
    @PreAuthorize("hasRole('MODERATOR')")
    public void removeRoleManagement(@RequestBody Long[] ids) {
        roleManagementService.delete(ids);
    }
}
