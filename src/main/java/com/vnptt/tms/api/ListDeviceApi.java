package com.vnptt.tms.api;


import com.vnptt.tms.api.output.table.ListDeviceOutput;
import com.vnptt.tms.dto.ListDeviceDTO;
import com.vnptt.tms.service.IListDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class ListDeviceApi {

    @Autowired
    private IListDeviceService listDeviceService;

    /**
     * Get List device for web
     *
     * @param page  desired page to display
     * @param limit number of elements 1 page
     * @return List app DTO
     */
    @GetMapping(value = "/listDevice")
    public ListDeviceOutput showListDevice(@RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "limit", required = false) Integer limit,
                                           @RequestParam(value = "name", required = false) String name) {
        ListDeviceOutput result = new ListDeviceOutput();
        if (page != null && limit != null) {
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            if (name == null) {
                result.setListResult((listDeviceService.findAll(pageable)));
                result.setTotalPage((int) Math.ceil((double) listDeviceService.totalItem() / limit));
            } else {
                result.setListResult((listDeviceService.findAllWithName(name, pageable)));
                result.setTotalPage((int) Math.ceil((double) listDeviceService.countWithName(name) / limit));
            }
        } else {
            result.setListResult(listDeviceService.findAll());
        }

        if (result.getListResult().size() >= 1) {
            result.setMessage("Get List ListDevice success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }

        return result;
    }

    /**
     * show application with id
     *
     * @param id id of app want to show
     * @return app DTO
     */
    @GetMapping(value = "/listDevice/{id}")
    public ListDeviceDTO showListDevice(@PathVariable("id") Long id) {
        return listDeviceService.findOne(id);
    }

    /**
     * get list device in of User management
     *
     * @return
     */
    @GetMapping(value = "/user/{userId}/listDevice")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ListDeviceOutput showListDeviceInRoles(@PathVariable(name = "userId") Long userId) {
        ListDeviceOutput result = new ListDeviceOutput();
        result.setListResult(listDeviceService.findListDeviceManagementByUser(userId));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * add new app to database
     *
     * @return http status ok 200
     */
    @PostMapping(value = "listDevice")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ListDeviceDTO> createListDevice(@RequestBody ListDeviceDTO listDeviceDTO) {

        return new ResponseEntity<>(listDeviceService.save(listDeviceDTO), HttpStatus.OK);
    }


    /**
     * Map List Device to roles Management
     *
     * @param userId       id of role
     * @param listDeviceId id of list Device
     * @return
     */
    @PostMapping(value = "/user/{userId}/listDevice/{listDeviceId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ListDeviceDTO addListDeviceToUser(@PathVariable(value = "userId") Long userId,
                                             @PathVariable(value = "listDeviceId") Long listDeviceId) {
        return listDeviceService.addListDeviceToUser(userId, listDeviceId);
    }


    /**
     * Edit list device info
     *
     * @param listDeviceId
     * @param listDeviceDTO
     * @return
     */
    @PutMapping(value = "/listDevice/{listDeviceId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ListDeviceDTO> updateListDevice(@PathVariable(name = "listDeviceId") Long listDeviceId,
                                                          @RequestBody ListDeviceDTO listDeviceDTO) {
        listDeviceDTO.setId(listDeviceId);
        return new ResponseEntity<>(listDeviceService.save(listDeviceDTO), HttpStatus.OK);
    }

    /**
     * remove map list device of roles management
     *
     * @param userId
     * @param listDeviceId
     * @return
     */
    @DeleteMapping(value = "/user/{userId}/listDevice/{listDeviceId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> removeListDeviceInManagement(@PathVariable(value = "userId") Long userId,
                                                                   @PathVariable(value = "listDeviceId") Long listDeviceId) {
        listDeviceService.removeListDeviceInUser(userId, listDeviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * unnecessary (only use to test)
     *
     * @param ids list id of app want to delete ex: [1,2,3]
     */
    @DeleteMapping(value = "/ListDevice")
    @PreAuthorize("hasRole('MODERATOR')")
    public void deleteListDevice(@RequestBody Long[] ids) {
        listDeviceService.delete(ids);
    }
}
