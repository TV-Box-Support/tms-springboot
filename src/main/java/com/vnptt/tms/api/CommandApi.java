package com.vnptt.tms.api;

import com.vnptt.tms.api.output.CommandOutput;
import com.vnptt.tms.dto.CommandDTO;
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
        return result;
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
    public void updateCommand(@RequestBody Long[] ids) {
        commandService.delete(ids);
    }
}
