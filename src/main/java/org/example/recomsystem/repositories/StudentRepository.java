package org.example.recomsystem.repositories;

import org.example.recomsystem.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByIdNot(long studentId);
}
