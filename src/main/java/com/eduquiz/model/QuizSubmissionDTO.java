package com.eduquiz.model;

import java.util.List;

public class QuizSubmissionDTO {
    private List<AnswerDTO> answers;

    // Getters and setters

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}

