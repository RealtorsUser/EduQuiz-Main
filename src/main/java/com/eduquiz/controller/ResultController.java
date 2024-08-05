package com.eduquiz.controller;

import com.eduquiz.model.Performer;
import com.eduquiz.model.Result;
import com.eduquiz.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping("/results")
    public ResponseEntity<List<Result>> getResults(@RequestParam String username) {
        List<Result> results = resultService.findByUsername(username);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/top-performers")
    public ResponseEntity<List<Performer>> getTopPerformers() {
        List<Performer> performers = resultService.findTopPerformers();
        return ResponseEntity.ok(performers);
    }
}

