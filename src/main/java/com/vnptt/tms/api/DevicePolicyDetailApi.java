package com.vnptt.tms.api;

import com.vnptt.tms.api.output.DevicePolicyDetailOutput;
import com.vnptt.tms.dto.DevicePolicyDetailDTO;
import com.vnptt.tms.service.IDevicePolicyDetailnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class DevicePolicyDetailApi {

    @Autowired
    private IDevicePolicyDetailnService devicePolicyDetailService;

    /**
     * unnecessary (Only use to test)
     *
     * @return
     */
    @GetMapping(value = "/devicePolicyDetail")
    public DevicePolicyDetailOutput showDevicePolicyDetail() {
        DevicePolicyDetailOutput result = new DevicePolicyDetailOutput();
        result.setListResult(devicePolicyDetailService.findAll());

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }

        return result;
    }

    /**
     * find devicePolicyDetail with id
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/devicePolicyDetail/{id}")
    public DevicePolicyDetailDTO showDevicePolicyDetail(@PathVariable("id") Long id) {
        return devicePolicyDetailService.findOne(id);
    }

    /**
     * get list PolicyDetail math with device to check policies unfinished for box
     * <p>
     * status of PolicyDetail
     * status 0 = not run
     * status 1 = run
     * status 2 = success
     * status 3 = error
     *
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/device/{deviceId}/devicePolicyDetail")
    public DevicePolicyDetailOutput showAllPolicyDetailOfDevice(@PathVariable(value = "deviceId") Long deviceId) {
        DevicePolicyDetailOutput result = new DevicePolicyDetailOutput();
        result.setListResult(devicePolicyDetailService.findAllWithDevice(deviceId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * get list PolicyDetail math with device to check policies detail unfinished for policy
     * <p>
     * status of PolicyDetail
     * status 0 = not run
     * status 1 = run
     * status 2 = success
     * status 3 = error
     *
     * @param
     * @return
     */
    @GetMapping(value = "/policy/{policyId}/devicePolicyDetail")
    public DevicePolicyDetailOutput showAllPolicyDetailOfPolicy(@PathVariable(value = "policyId") Long policyId) {
        DevicePolicyDetailOutput result = new DevicePolicyDetailOutput();
        result.setListResult(devicePolicyDetailService.findAllWithPolicy(policyId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * create policy detail for web
     * <p>
     * status of policy detail
     * status = 0 not run
     *
     * @param policyId id of policy
     * @param ids      list id device
     * @return
     */
    @PostMapping(value = "/policy/{policyId}/devicePolicyDetail")
    public DevicePolicyDetailOutput createDevicePolicyDetail(@PathVariable(value = "policyId") Long policyId,
                                                             @RequestBody Long[] ids) {
        DevicePolicyDetailOutput output = new DevicePolicyDetailOutput();
        output.setListResult(devicePolicyDetailService.save(ids, policyId));

        if (output.getListResult().size() >= 1) {
            output.setMessage("Request Success");
            output.setTotalElement(output.getListResult().size());
        } else {
            output.setMessage("no device had choose");
        }
        return output;
    }

    /**
     * update status of policy detail for box
     * <p>
     * status of PolicyDetail
     * status 0 = not run
     * status 1 = run
     * status 2 = success
     * status 3 = error
     *
     * @param status
     * @param id
     * @return
     */
    @PutMapping(value = "/devicePolicyDetail/{id}")
    public DevicePolicyDetailDTO updateDevicePolicyDetail(@RequestParam(value = "status") int status,
                                                          @PathVariable("id") Long id) {
        return devicePolicyDetailService.update(id, status);
    }

    /**
     * dangerous (only use to test)
     * When
     *
     * @param ids
     */
    @DeleteMapping(value = "/devicePolicyDetail")
    public void removeDevicePolicyDetail(@RequestBody Long[] ids) {
        devicePolicyDetailService.delete(ids);
    }
}
