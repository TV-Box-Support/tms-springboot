package com.vnptt.tms.api;

import com.vnptt.tms.api.output.CommandOutput;
import com.vnptt.tms.dto.CommandDTO;
import com.vnptt.tms.dto.PolicyDTO;
import com.vnptt.tms.service.ICommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class CommandApi {

    @Autowired
    private ICommandService commandService;

    /**
     * show command for web
     * @param page number of page want to show
     * @param limit total number element in a page
     * @return
     */
    @GetMapping(value = "/command")
    public CommandOutput showCommand(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "limit", required = false) Integer limit) {
        CommandOutput result = new CommandOutput();
        if (page != null && limit != null){
            result.setPage(page);
            Pageable pageable = PageRequest.of(page -1, limit );
            result.setListResult((commandService.findAll(pageable)));
            result.setTotalPage((int) Math.ceil((double) commandService.totalItem()/ limit));
        } else {
            result.setListResult(commandService.findAll());
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
     * find command with id
     * @param id
     * @return
     */
    @GetMapping(value = "/command/{id}")
    public CommandDTO showCommand(@PathVariable("id") Long id) {
        return commandService.findOne(id);
    }

    @PostMapping(value = "/command")
    public CommandDTO createCommand(@RequestBody CommandDTO model) {
        return commandService.save(model);
    }

    @PutMapping(value = "/command/{id}")
    public CommandDTO updateCommand(@RequestBody CommandDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return commandService.save(model);
    }

    @DeleteMapping(value = "/Command")
    public void removeCommand(@RequestBody Long[] ids) {
        commandService.delete(ids);
    }
}
