package com.vnptt.tms.api;

import com.vnptt.tms.api.output.ApkOutput;
import com.vnptt.tms.dto.ApkDTO;
import com.vnptt.tms.service.IApkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class ApkApi {

    @Autowired
    private IApkService apkService;

    @GetMapping(value = "/apk")
    public ApkOutput showApk(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "limit", required = false) Integer limit) {
        ApkOutput result = new ApkOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult(apkService.findAll(pageable));
            result.setTotalPage((int) Math.ceil((double) apkService.totalItem()/ limit));
        } else {
            result.setListResult(apkService.findAll());
        }
        return result;
    }

    @PostMapping(value = "/apk")
    public ApkDTO createApk(@RequestBody ApkDTO model) {
        return apkService.save(model);
    }

    @PutMapping(value = "/apk/{id}")
    public ApkDTO updateApk(@RequestBody ApkDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return apkService.save(model);
    }

    @DeleteMapping(value = "/apk")
    public void updateApk(@RequestBody Long[] ids) {
        apkService.delete(ids);
    }
}
