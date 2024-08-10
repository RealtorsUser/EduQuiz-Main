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
@RequestMapping("/api")
public class QuizController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/quizzes")
    public ResponseEntity<List<Question>> getQuizQuestions() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no questions found
        }
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitQuiz(@RequestBody QuizSubmissionDTO submission) {
        if (submission == null || submission.getAnswers() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid submission data"));
        }

        List<AnswerDTO> answers = submission.getAnswers();
        int score = 0;

        for (AnswerDTO answer : answers) {
            Optional<Question> questionOpt = questionRepository.findById(answer.getQuestionId());
            if (questionOpt.isPresent()) {
                Question question = questionOpt.get();
                if (question.getCorrectAnswer().equals(answer.getSelectedAnswer())) {
                    score++;
                }
            } else {
                // Handle case where question ID does not exist
                return ResponseEntity.badRequest().body(Map.of("error", "Question not found for ID: " + answer.getQuestionId()));
            }
        }

        return ResponseEntity.ok(Map.of("score", score, "totalQuestions", answers.size()));
    }
}