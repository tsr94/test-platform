package com.test.testplatform.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.test.testplatform.model.Question;
import com.test.testplatform.repository.QuestionRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private QuestionRepository questionRepo;

    @Override
    public void run(String... args) {
        if (questionRepo.count() == 0) {
            questionRepo.save(new Question(null, "Capital of France?", "London", "Berlin", "Paris", "Madrid", "C"));
            questionRepo.save(new Question(null, "2+2=?", "3", "4", "5", "6", "B"));
            // Add more questions here
        }
    }
}
