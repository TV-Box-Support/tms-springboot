package com.vnptt.tms.api;

import com.vnptt.tms.api.output.table.AlertDialogOutput;
import com.vnptt.tms.dto.AlertDialogDTO;
import com.vnptt.tms.service.AlertDialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class AlertDialogApi {

    @Autowired
    private AlertDialogService CommandNotificationService;

    /**
     * api show list CommandNotification for web
     *
     * @param page  number of page want to show
     * @param limit total number element in a page
     * @return list CommandNotification
     */
    @GetMapping(value = "/commandNotification")
    public AlertDialogOutput showCommandNotification(@RequestParam(value = "page", required = false) Integer page,
                                                     @RequestParam(value = "limit", required = false) Integer limit,
                                                     @RequestParam(value = "search", required = false) String message) {
        AlertDialogOutput result = new AlertDialogOutput();
        if (page != null && limit != null) {
            if(message == null){
                result.setPage(page);
                Pageable pageable = PageRequest.of(page - 1, limit);
                result.setListResult((CommandNotificationService.findAll(pageable)));
                result.setTotalPage((int) Math.ceil((double) CommandNotificationService.totalItem() / limit));
            } else{
                result.setPage(page);
                Pageable pageable = PageRequest.of(page - 1, limit);
                result.setListResult((CommandNotificationService.findAllWithMessage(message, pageable)));
                result.setTotalPage((int) Math.ceil((double) CommandNotificationService.countAllWithMessage(message) / limit));
            }

        } else {
            if(message != null){
                throw new RuntimeException("page and limit fields are required when searching");
            }
            result.setListResult(CommandNotificationService.findAll());
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
     * api show CommandNotification with id
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/commandNotification/{id}")
    public AlertDialogDTO showCommandNotification(@PathVariable("id") Long id) {
        return CommandNotificationService.findOne(id);
    }

    /**
     * api create one model CommandNotification
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/commandNotification")
    public AlertDialogDTO createCommandNotification(@RequestBody AlertDialogDTO model) {
        return CommandNotificationService.save(model);
    }

    /**
     * api modify CommandNotification
     *
     * @param model
     * @param id
     * @return
     */
    @PutMapping(value = "/commandNotification/{id}")
    public AlertDialogDTO updateCommandNotification(@RequestBody AlertDialogDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return CommandNotificationService.save(model);
    }

    /**
     * api delete CommandNotification (only use to test)
     *
     * @param ids
     */
    @DeleteMapping(value = "/commandNotification")
    @PreAuthorize("hasRole('MODERATOR')")
    public void removeCommandNotification(@RequestBody Long[] ids) {
        CommandNotificationService.delete(ids);
    }
}
