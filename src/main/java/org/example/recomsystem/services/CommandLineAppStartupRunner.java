package org.example.recomsystem.services;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    StudentGenerationService studentGenerationService;

    public static int counter;

    @Override
    public void run(String...args) throws Exception {
        studentGenerationService.generateCharacteristics(10);
        studentGenerationService.generateDisciplines(40);
        studentGenerationService.generateStudents(100);
    }
}