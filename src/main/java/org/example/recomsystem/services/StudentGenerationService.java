package org.example.recomsystem.services;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.example.recomsystem.entities.Characteristics;
import org.example.recomsystem.entities.Discipline;
import org.example.recomsystem.entities.Student;
import org.example.recomsystem.entities.StudentCharacteristics;
import org.example.recomsystem.repositories.CharacteristicsRepository;
import org.example.recomsystem.repositories.DisciplineRepository;
import org.example.recomsystem.repositories.StudentCharacteristicsRepository;
import org.example.recomsystem.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentGenerationService {

    private final StudentRepository studentRepository;
    private final StudentCharacteristicsRepository studentCharacteristicsRepository;
    private final DisciplineRepository disciplineRepository;
    private final CharacteristicsRepository characteristicsRepository;
    private final Faker faker = new Faker();

    public void generateTestData(int numStudents, int numDisciplines, int numCharacteristics) {
        List<Characteristics> characteristics = new ArrayList<>();
        for (int i = 1; i <= numCharacteristics; i++) {
            characteristics.add(new Characteristics(null, faker.lorem().word()));
        }
        characteristicsRepository.saveAllAndFlush(characteristics);

        characteristics = characteristicsRepository.findAll();

        List<Discipline> disciplines = new ArrayList<>();
        for (int i = 1; i <= numDisciplines; i++) {
            disciplines.add(new Discipline(null, faker.educator().course(), new ArrayList<>()));
        }
        disciplineRepository.saveAll(disciplines);
        disciplines = disciplineRepository.findAll();

        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= numStudents; i++) {
            Student student = new Student(null, faker.name().fullName(), faker.number().numberBetween(18, 30),
                    faker.internet().emailAddress(), faker.address().fullAddress(),
                    new ArrayList<>(disciplines.subList(0, 5)), null);

            studentRepository.save(student);
            Set<StudentCharacteristics> studentCharacteristics = new HashSet<>();
            for (Characteristics c : characteristics) {
                studentCharacteristics.add(new StudentCharacteristics(null, student, c, faker.number().numberBetween(0, 10)));
            }
            studentCharacteristicsRepository.saveAllAndFlush(studentCharacteristics);
        }
    }

    public void generateDisciplines(int numDisciplines) {
        List<Discipline> disciplines = new ArrayList<>();
        for (int i = 1; i <= numDisciplines; i++) {
            disciplines.add(new Discipline(null, faker.educator().course(), new ArrayList<>()));
        }
        disciplineRepository.saveAll(disciplines);
    }

    public void generateCharacteristics(int numCharacteristics) {
        List<Characteristics> characteristics = new ArrayList<>();
        for (int i = 0; i < numCharacteristics; i++) {
            characteristicsRepository.save(new Characteristics(null, faker.lorem().word()));
            System.out.println(i);
        }
    }

    public void generateStudents(int numStudents) {

        List<Characteristics> characteristics = characteristicsRepository.findAll();

        List<Discipline> disciplines = disciplineRepository.findAll();

        for (int i = 1; i <= numStudents; i++) {
            Collections.shuffle(disciplines);
            Student student = new Student(null, faker.name().fullName(), faker.number().numberBetween(18, 30),
                    faker.internet().emailAddress(), faker.address().fullAddress(),
                    new ArrayList<>(disciplines.subList(0, faker.number().numberBetween(5, 20))), null);

            studentRepository.save(student);
            Set<StudentCharacteristics> studentCharacteristics = new HashSet<>();
            for (Characteristics c : characteristics) {
                studentCharacteristics.add(new StudentCharacteristics(null, student, c, faker.number().numberBetween(0, 10)));
            }
            studentCharacteristicsRepository.saveAllAndFlush(studentCharacteristics);
        }
    }

}
