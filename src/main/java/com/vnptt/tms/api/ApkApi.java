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

/**
 * Application Programming Interface for apk manager
 * include:
 * <p>
 * - get list apk from database pageable or none
 * - get single apk form database by id
 * - dowload (get method) apk file by fileDownloadUri
 * - show (get method) the apk available on the policy for box and web
 * - Map (post method) apk to policy
 * - create (post method) apk normal with info add manually
 * - create (post method) apk by upload file
 * - update (put method) apk infor
 * - delete map apk to policy
 * - remove (delete method) apk out of database
 * <p>
 * ...
 */
@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class ApkApi {

    @Autowired
    private IApkService apkService;

    /**
     * get list apk from database
     *
     * @param page  the number of page you want to display
     * @param limit the number of element in a page
     * @return List apk DTO
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
     * dowload apk file
     *
     * @param fileName name of apk file
     * @param request  HTTP Header
     * @return file raw
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
     * show apk with id
     *
     * @param id id of apk
     * @return apk DTO
     */
    @GetMapping(value = "/apk/{id}")
    public ApkDTO showApk(@PathVariable("id") Long id) {
        return apkService.findOne(id);
    }

    /**
     * see the apk available on the policy for box and web
     *
     * @param policyId device want to search
     * @return List Apk DTO
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
     * @param policyId policy want to map
     * @param apkId    apk want to map
     * @return apk in policy want to map
     */
    @PostMapping(value = "/policy/{policyId}/apk/{apkId}")
    public ApkDTO addApplicationToDevice(@PathVariable(value = "policyId") Long policyId,
                                         @PathVariable(value = "apkId") Long apkId) {
        return apkService.addApkToPolicy(policyId, apkId);
    }

    /**
     * create apk normal
     *
     * @param model apk DTO
     * @return apk DTO after save on database
     */
    @PostMapping(value = "/apk")
    public ApkDTO createApk(@RequestBody ApkDTO model) {
        return apkService.save(model);
    }

    /**
     * upload file to server and save apk info
     *
     * @param file MultipartFile file apk
     * @return apk DTO after apk has upload done in dir (in application.properties)
     */
    @PostMapping("/uploadFile")
    public ApkDTO uploadFile(@RequestParam("file") MultipartFile file) {
        return apkService.saveFile(file);
    }

    /**
     * update apk info
     *
     * @param model new info of element apk
     * @param id    id of apk in database
     * @return apk DTO after save in database
     */
    @PutMapping(value = "/apk/{id}")
    public ApkDTO updateApk(@RequestBody ApkDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return apkService.save(model);
    }

    /**
     * remove map of apk and policy
     *
     * @param policyId id of policy want to modify
     * @param apkId    id of apk want to remove in policy
     * @return http status no content 204
     */
    @DeleteMapping(value = "/policy/{policyId}/apk/{apkId}")
    public ResponseEntity<HttpStatus> removeApkInPolicy(@PathVariable(value = "policyId") Long policyId,
                                                        @PathVariable(value = "apkId") Long apkId) {
        apkService.removeApkinPolicy(policyId, apkId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * delete apk in database (very dangerous) (only use to test)
     *
     * @param ids list id apk want to delete ex: [1,2,3]
     */
    @DeleteMapping(value = "/apk")
    public void removeApk(@RequestBody Long[] ids) {
        apkService.delete(ids);
    }
}
