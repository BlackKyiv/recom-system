package org.example.recomsystem.repositories;

import org.example.recomsystem.entities.StudentCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCharacteristicsRepository extends JpaRepository<StudentCharacteristics, Long> {
}
