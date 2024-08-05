package com.eduquiz.service;


import com.eduquiz.model.TopPerformer;
import com.eduquiz.repository.TopPerformerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TopPerformerService {

    @Autowired
    private TopPerformerRepository topPerformerRepository;

    public List<TopPerformer> getAllTopPerformers() {
        return topPerformerRepository.findAll();
    }
}
