package com.vnptt.tms.api;

import com.vnptt.tms.api.output.ApkOutput;
import com.vnptt.tms.dto.ApkDTO;
import com.vnptt.tms.service.IApkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class ApkApi {

    @Autowired
    private IApkService apkService;

    /**
     * get all apk infor from server
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "/apk")
    public ApkOutput showApk(@RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "limit", required = false) Integer limit) {
        ApkOutput result = new ApkOutput();
        if (page != null && limit != null) {
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            result.setListResult(apkService.findAll(pageable));
            result.setTotalPage((int) Math.ceil((double) apkService.totalItem() / limit));
        } else {
            result.setListResult(apkService.findAll());
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
     * dowload apk for box
     *
     * @param fileName
     * @param request
     * @return
     */
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

        // Load file as Resource
        Resource resource = apkService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ignored) {
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * find apk with id
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/apk/{id}")
    public ApkDTO showApk(@PathVariable("id") Long id) {
        return apkService.findOne(id);
    }

    /**
     * see the apk available on the policy for box and web
     *
     * @param policyId device want to search
     * @return
     */
    @GetMapping("/policy/{policyId}/apk")
    public ApkOutput showAllApkInPolicy(@PathVariable(value = "policyId") Long policyId) {
        ApkOutput result = new ApkOutput();
        result.setListResult(apkService.findAllOnPolicy(policyId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * Mapp apk to policy
     *
     * @param policyId policy has apk
     * @param apkId
     * @return
     */
    @PostMapping(value = "/policy/{policyId}/apk/{apkId}")
    public ApkDTO addApplicationToDevice(@PathVariable(value = "policyId") Long policyId,
                                         @PathVariable(value = "apkId") Long apkId) {
        return apkService.addApkToPolicy(policyId, apkId);
    }

    /**
     * create apk normal
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/apk")
    public ApkDTO createApk(@RequestBody ApkDTO model) {
        return apkService.save(model);
    }

    /**
     * upload file to server and save apk info
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadFile")
    public ApkDTO uploadFile(@RequestParam("file") MultipartFile file) {
        return apkService.saveFile(file);
    }

    /**
     * update apk info
     *
     * @param model
     * @param id
     * @return
     */
    @PutMapping(value = "/apk/{id}")
    public ApkDTO updateApk(@RequestBody ApkDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return apkService.save(model);
    }

    /**
     * remove apk in policy
     *
     * @param policyId
     * @param apkId
     * @return
     */
    @DeleteMapping(value = "/policy/{policyId}/apk/{apkId}")
    public ResponseEntity<HttpStatus> removeApkinPolicy(@PathVariable(value = "policyId") Long policyId,
                                                        @PathVariable(value = "apkId") Long apkId) {
        apkService.removeApkinPolicy(policyId, apkId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * delete apk in database (very dangerous) (only use to test)
     *
     * @param ids
     */
    @DeleteMapping(value = "/apk")
    public void removeApk(@RequestBody Long[] ids) {
        apkService.delete(ids);
    }
}
