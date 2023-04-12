package org.example.recomsystem.repositories;

import org.example.recomsystem.entities.Characteristics;
import org.example.recomsystem.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CharacteristicsRepository extends JpaRepository<Characteristics, Long> {
}
