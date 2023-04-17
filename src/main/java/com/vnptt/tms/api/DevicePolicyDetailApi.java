package com.vnptt.tms.api;

import com.vnptt.tms.api.output.DevicePolicyDetailOutput;
import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import com.vnptt.tms.service.IDevicePolicyDetailnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class DevicePolicyDetailApi {

    @Autowired
    private IDevicePolicyDetailnService devicePolicyDetailService;

    @GetMapping(value = "/devicePolicyDetail")
    public DevicePolicyDetailOutput showDevicePolicyDetail(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "limit", required = false) Integer limit) {
        DevicePolicyDetailOutput result = new DevicePolicyDetailOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((devicePolicyDetailService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) devicePolicyDetailService.totalItem()/ limit));
        } else {
            result.setListResult(devicePolicyDetailService.findAll());
        }
        return result;
    }

    @PostMapping(value = "/devicePolicyDetail")
    public DevicePolicyDetailDTO createDevicePolicyDetail(@RequestBody DevicePolicyDetailDTO model) {
        return devicePolicyDetailService.save(model);
    }

    @PutMapping(value = "/devicePolicyDetail/{id}")
    public DevicePolicyDetailDTO updateDevicePolicyDetail(@RequestBody DevicePolicyDetailDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return devicePolicyDetailService.save(model);
    }

    @DeleteMapping(value = "/DevicePolicyDetail")
    public void updateDevicePolicyDetail(@RequestBody Long[] ids) {
        devicePolicyDetailService.delete(ids);
    }
}
