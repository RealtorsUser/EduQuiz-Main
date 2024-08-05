package com.eduquiz.service;

import com.eduquiz.model.Performer;
import com.eduquiz.model.Result;
import com.eduquiz.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public List<Result> findByUsername(String username) {
        return resultRepository.findByUsername(username);
    }

    @Override
    public List<Performer> findTopPerformers() {
        return resultRepository.findTopPerformers();
    }
}
