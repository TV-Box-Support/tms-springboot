package com.vnptt.tms.api;

import com.vnptt.tms.api.output.UserOutput;
import com.vnptt.tms.dto.UserDTO;
import com.vnptt.tms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class UserApi {

    @Autowired
    private IUserService userService;

    @GetMapping(value = "/user")
    public UserOutput showUser(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "limit", required = false) Integer limit) {
        UserOutput result = new UserOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((userService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) userService.totalItem()/ limit));
        } else {
            result.setListResult(userService.findAll());
        }
        return result;
    }

    @PostMapping(value = "/user")
    public UserDTO createUser(@RequestBody UserDTO model) {
        return userService.save(model);
    }

    @PutMapping(value = "/user/{id}")
    public UserDTO updateUser(@RequestBody UserDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return userService.save(model);
    }

    @DeleteMapping(value = "/user")
    public void updateUser(@RequestBody Long[] ids) {
        userService.delete(ids);
    }
}
