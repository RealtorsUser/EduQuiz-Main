package com.eduquiz.service;

import com.eduquiz.model.Performer;
import com.eduquiz.model.Result;

import java.util.List;

public interface ResultService {
    List<Result> findByUsername(String username);
    List<Performer> findTopPerformers();
}
