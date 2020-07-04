package test.uicode.mongoperf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import test.uicode.mongoperf.ResultDto;
import test.uicode.mongoperf.entity.BaseEntity;
import test.uicode.mongoperf.service.OneService;

@RestController
@RequestMapping("mongoperf")
public class OneController {

    @Autowired
    private OneService oneService;

    @GetMapping
    public String hello() {
        return "Hello";
    }

    @GetMapping("init")
    public void initEntities(@RequestParam Integer entityNumber, @RequestParam Integer entitySize) {
        oneService.initEntities(entityNumber, entitySize);
    }

    @GetMapping("search")
    public ResultDto search(@RequestParam String nameFilter) {
        return oneService.searchEntities(nameFilter);
    }
    
    @GetMapping("all")
    public List<BaseEntity> getAllEntities() {
        return oneService.getAllEntities();
    }

}
