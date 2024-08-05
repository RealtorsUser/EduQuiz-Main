package com.eduquiz.controller;

import com.eduquiz.model.TopPerformer;
import com.eduquiz.service.TopPerformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TopPerformerController {

    @Autowired
    private TopPerformerService topPerformerService;

    @GetMapping("/api/top-performers")
    public List<TopPerformer> getTopPerformers() {
        return topPerformerService.getAllTopPerformers();
    }
}

