package org.example.recomsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.recomsystem.services.DisciplineService;
import org.example.recomsystem.services.RecommendationService;
import org.example.recomsystem.services.StudentGenerationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("generate")
public class GenerationController {

    private final StudentGenerationService studentGenerationService;

    private final RecommendationService recommendationService;

    private final DisciplineService disciplineService;


    @GetMapping("/st")
    public void genSt() {
        studentGenerationService.generateStudents(25);
    }

    @GetMapping("/ds")
    public void genDs() {
        studentGenerationService.generateDisciplines(10);
    }

    @GetMapping("/ch")
    public void genCh() {
        studentGenerationService.generateCharacteristics(5);
    }


}

