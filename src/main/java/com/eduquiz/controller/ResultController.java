package com.eduquiz.controller;

import com.eduquiz.model.Result;
import com.eduquiz.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping("/results")
    public String getResultsPage(Model model) {
        List<Result> results = resultService.getAllResults();
        model.addAttribute("results", results);
        return "results"; // This will resolve to "results.html"
    }

    @GetMapping("/results/{id}")
    @ResponseBody
    public Result getResultById(@PathVariable("id") Long id) {
        return resultService.getResultById(id);
    }

    @GetMapping("/results/top-performers")
    @ResponseBody
    public List<Result> getTopPerformers() {
        return resultService.getTopPerformers();
    }

    @GetMapping("/results/user")
    @ResponseBody
    public List<Result> getResultsByUserName(@RequestParam("userName") String userName) {
        return resultService.getResultsByUserName(userName);
    }
}
