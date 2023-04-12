package org.example.recomsystem.repositories;

import org.example.recomsystem.entities.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
}
