package org.example.recomsystem.services;

import lombok.RequiredArgsConstructor;
import org.example.recomsystem.entities.Characteristics;
import org.example.recomsystem.entities.Student;
import org.example.recomsystem.repositories.CharacteristicsRepository;
import org.example.recomsystem.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    private final CharacteristicsRepository characteristicRepository;

    public List<Characteristics> getAllCharacteristics() {
        return characteristicRepository.findAll();
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id).orElse(null);
        if (existingStudent != null) {
            existingStudent.setName(student.getName());
            existingStudent.setAge(student.getAge());
            existingStudent.setAddress(student.getAddress());
            existingStudent.setCharacteristics(student.getCharacteristics());
            existingStudent.setDisciplines(student.getDisciplines());
            return studentRepository.save(existingStudent);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}

