package com.eduquiz.service;

import com.eduquiz.model.Quiz;
import com.eduquiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public void createQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    public String updateQuiz(Long id, Quiz quiz) {
        if (quizRepository.existsById(id)) {
            quiz.setId(id);
            quizRepository.save(quiz);
            return "Quiz updated successfully";
        } else {
            return "Quiz not found";
        }
    }

    public String deleteQuiz(Long id) {
        if (quizRepository.existsById(id)) {
            quizRepository.deleteById(id);
            return "Quiz deleted successfully";
        } else {
            return "Quiz not found";
        }
    }
}
