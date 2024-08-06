package com.eduquiz.service;

import com.eduquiz.model.Result;
import com.eduquiz.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    public Result getResultById(Long id) {
        return resultRepository.findById(id).orElse(null);
    }

    public List<Result> getTopPerformers() {
        // Assuming there's a method in ResultRepository to fetch top performers
        return resultRepository.findTopPerformers();
    }

    public List<Result> getResultsByUsername(String username) {
        return resultRepository.findByUserName(username);
    }
}
