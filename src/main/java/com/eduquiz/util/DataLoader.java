package com.eduquiz.util;

import com.eduquiz.model.Question;
import com.eduquiz.model.School;
import com.eduquiz.repository.QuestionRepository;
import com.eduquiz.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Preload some schools
        schoolRepository.save(new School("ABC Kindergarten", "Area 1", "Kindergarten"));
        schoolRepository.save(new School("XYZ Primary School", "Area 2", "Primary"));
        schoolRepository.save(new School("LMN Secondary School", "Area 3", "Secondary"));


        if (questionRepository.count() == 0) {
            Question question1 = new Question();
            question1.setQuestionText("What is the process by which plants make their own food using sunlight called?");
            question1.setOptions(Arrays.asList("Respiration", "Photosynthesis", "Fermentation", "Digestion"));
            question1.setCorrectAnswer("Photosynthesis");

            Question question2 = new Question();
            question2.setQuestionText("What is the chemical formula for water?");
            question2.setOptions(Arrays.asList("CO2", "H2O", "NaCl", "O2"));
            question2.setCorrectAnswer("H2O");

            questionRepository.saveAll(Arrays.asList(question1, question2));
        }
    }

}



