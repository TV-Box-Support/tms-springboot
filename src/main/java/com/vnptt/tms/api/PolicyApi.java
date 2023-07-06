package com.vnptt.tms.api;

import com.vnptt.tms.api.output.table.PolicyOutput;
import com.vnptt.tms.dto.PolicyDTO;
import com.vnptt.tms.service.IPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class PolicyApi {

    @Autowired
    private IPolicyService policyService;

    @GetMapping(value = "/policy")
    public PolicyOutput showPolicy(@RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "limit", required = false) Integer limit,
                                   @RequestParam(value = "policyname", required = false) String policyname) {
        PolicyOutput result = new PolicyOutput();
        if (page != null && limit != null) {
            if (policyname != null) {
                result.setPage(page);
                Pageable pageable = PageRequest.of(page - 1, limit);
                result.setListResult((policyService.findwithPolicyname(policyname, pageable)));
                result.setTotalPage((int) Math.ceil((double) policyService.totalCountByPolicynameContain(policyname) / limit));
            } else {
                result.setPage(page);
                Pageable pageable = PageRequest.of(page - 1, limit);
                result.setListResult((policyService.findAll(pageable)));
                result.setTotalPage((int) Math.ceil((double) policyService.totalItem() / limit));
            }
        } else {
            result.setListResult(policyService.findAll());
        }

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }

        return result;
    }

    /**
     * api show policy by id
     *
     * @param id id of policy
     * @return
     */
    @GetMapping(value = "/policy/{id}")
    public PolicyDTO showPolicy(@PathVariable("id") Long id) {
        return policyService.findOne(id);
    }

    /**
     * api show list policy has use command
     *
     * @param commandId command id
     * @return
     */
    @GetMapping(value = "/command/{commandId}/policy")
    public PolicyOutput showAllPolicyByCommandId(@PathVariable(value = "commandId") Long commandId) {
        PolicyOutput result = new PolicyOutput();
        result.setListResult(policyService.findAllWithCommand(commandId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * api show list policy have apk
     *
     * @param apkId
     * @return
     */
    @GetMapping(value = "/apk/{apkId}/policy")
    public PolicyOutput showAllPolicyByApkId(@PathVariable(value = "apkId") Long apkId) {
        PolicyOutput result = new PolicyOutput();
        result.setListResult(policyService.findAllWithApk(apkId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * api show list policy of device for web
     *
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/device/{deviceId}/policy")
    public PolicyOutput showAllPolicyByDeviceId(@PathVariable(value = "deviceId") Long deviceId) {
        PolicyOutput result = new PolicyOutput();
        result.setListResult(policyService.findAllWithDeviceId(deviceId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }


    /**
     * api create new policy for web
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/policy")
    public PolicyDTO createPolicy(@RequestBody PolicyDTO model) {
        return policyService.save(model);
    }

    /**
     * api update policy for web
     *
     * @param model
     * @param id
     * @return
     */
    @PutMapping(value = "/policy/{id}")
    public PolicyDTO updatePolicy(@RequestBody PolicyDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return policyService.save(model);
    }

    /**
     * api update status for policy
     * <p>
     * status of policy
     * status 0 = not run
     * status 1 = run
     * status 2 = pause
     * status 3 = stop
     *
     * @param id
     * @param status
     * @return
     */
    @PutMapping(value = "/policy/{id}/status/{status}")
    public PolicyDTO updateStatus(@PathVariable("id") Long id,
                                  @PathVariable(value = "status") int status) {
        return policyService.updateStatus(id, status);
    }

    /**
     * should not be used because it affects all historical information, very dangerous
     * when delete policy all policyDetail will remove (be Carefull)
     *
     * @param ids
     */
    @DeleteMapping(value = "/policy")
    @PreAuthorize("hasRole('MODERATOR')")
    public void deletePolicy(@RequestBody Long[] ids) {
        policyService.delete(ids);
    }
}
