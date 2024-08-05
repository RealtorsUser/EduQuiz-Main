package com.eduquiz.controller;

import com.eduquiz.model.Question;
import com.eduquiz.model.QuestionUploadDTO;
import com.eduquiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadQuestions(@RequestBody List<QuestionUploadDTO> questions) {
        List<Question> questionEntities = questions.stream().map(dto -> {
            Question question = new Question();
            question.setQuestionText(dto.getQuestionText());
            question.setOptions(dto.getOptions());
            question.setCorrectAnswer(dto.getCorrectAnswer());
            return question;
        }).collect(Collectors.toList());

        questionRepository.saveAll(questionEntities);

        return ResponseEntity.ok("Questions uploaded successfully");
    }
}

