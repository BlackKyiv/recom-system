package org.example.recomsystem.controllers;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.recomsystem.entities.Characteristics;
import org.example.recomsystem.entities.Discipline;
import org.example.recomsystem.entities.Student;
import org.example.recomsystem.repositories.CharacteristicsRepository;
import org.example.recomsystem.repositories.DisciplineRepository;
import org.example.recomsystem.services.DisciplineService;
import org.example.recomsystem.services.RecommendationService;
import org.example.recomsystem.services.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    private final CharacteristicsRepository characteristicsRepository;
    private final RecommendationService recommendationService;
    private final DisciplineService disciplineService;
    private final DisciplineRepository disciplineRepository;


    @GetMapping("/students")
    public ModelAndView getStudents() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("students");
        List<Student> students = studentService.getAllStudents();
        mav.addObject("students", students);
        System.out.println(characteristicsRepository.findAll().stream().map(Characteristics::getName).collect(Collectors.toList()));
        mav.addObject("allCharacteristics", characteristicsRepository.findAll());
        return mav;
    }

    @GetMapping("/recom/{studId}")
    public ModelAndView recomend(@PathVariable long studId) throws NotFoundException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("recom");


        List<Long> studentsWithCommon = recommendationService.findStudentIdsWithCommonDisciplines(studId);
        mav.addObject("studentsWithCommon", studentsWithCommon.stream().map(studentService::getStudentById).collect(Collectors.toList()));
        mav.addObject("disciplines", studentService.getStudentById(studId).getDisciplines());

        Map<Long, Double> pearsonCoefficients = recommendationService.calculatePearsonCoefficients(studId, studentsWithCommon);
        Map<Student, Double> stPearson = new HashMap<>();
        for (Long id : pearsonCoefficients.keySet()) {
            stPearson.put(studentService.getStudentById(id), pearsonCoefficients.get(id));
        }
        mav.addObject("stPearson", stPearson);
        mav.addObject("student", studentService.getStudentById(studId));
        mav.addObject("characteristics", characteristicsRepository.findAll());


        Map<Long, Double> res = recommendationService.calculateDisciplinesRateForStudent(studId);
        Map<Discipline, Double> result = new HashMap<>();
        for (Long id : res.keySet()) {
            result.put(disciplineRepository.getById(id), res.get(id));
        }

        mav.addObject("disciplineRatings", result);


        return mav;
    }

    @GetMapping("/disciplines")
    public ModelAndView disciplines() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("st-ds");
        mav.addObject("students", studentService.getAllStudents());
        mav.addObject("disciplines", disciplineRepository.findAll());

        return mav;
    }


}



