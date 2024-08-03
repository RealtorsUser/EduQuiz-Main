package com.eduquiz.controller;

import com.eduquiz.model.AnswerDTO;
import com.eduquiz.model.Question;
import com.eduquiz.model.QuizSubmissionDTO;
import com.eduquiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    public ResponseEntity<List<Question>> getQuizQuestions() {
        List<Question> questions = questionRepository.findAll();
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@RequestBody QuizSubmissionDTO submission) {
        List<AnswerDTO> answers = submission.getAnswers();
        int score = 0;

        for (AnswerDTO answer : answers) {
            Optional<Question> questionOpt = questionRepository.findById(answer.getQuestionId());
            if (questionOpt.isPresent()) {
                Question question = questionOpt.get();
                if (question.getCorrectAnswer().equals(answer.getSelectedAnswer())) {
                    score++;
                }
            }
        }

        return ResponseEntity.ok(Map.of("score", score, "totalQuestions", answers.size()));
    }

}

