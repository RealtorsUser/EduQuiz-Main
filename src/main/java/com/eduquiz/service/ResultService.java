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

    public void createResult(Result result) {
        resultRepository.save(result);
    }

    public Result getResultById(Long id) {
        return resultRepository.findById(id).orElse(null);
    }

    public String updateResult(Long id, Result result) {
        if (resultRepository.existsById(id)) {
            result.setId(id);
            resultRepository.save(result);
            return "Result updated successfully";
        } else {
            return "Result not found";
        }
    }

    public String deleteResult(Long id) {
        if (resultRepository.existsById(id)) {
            resultRepository.deleteById(id);
            return "Result deleted successfully";
        } else {
            return "Result not found";
        }
    }
}
