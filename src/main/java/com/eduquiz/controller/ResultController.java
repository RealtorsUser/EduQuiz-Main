package com.eduquiz.controller;

import com.eduquiz.model.Result;
import com.eduquiz.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping("/results")
    public List<Result> getResults() {
        return resultService.getAllResults();
    }

    @GetMapping("/results/{id}")
    public Result getResultById(@PathVariable("id") Long id) {
        return resultService.getResultById(id);
    }

    @GetMapping("/results/top-performers")
    public List<Result> getTopPerformers() {
        return resultService.getTopPerformers();
    }

    @GetMapping("/results/user")
    public List<Result> getResultsByUserName(@RequestParam("userName") String userName) {
        return resultService.getResultsByUserName(userName);
    }
}
