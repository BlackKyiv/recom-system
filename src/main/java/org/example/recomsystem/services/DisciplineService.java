package org.example.recomsystem.services;

import lombok.AllArgsConstructor;
import org.example.recomsystem.entities.Discipline;
import org.example.recomsystem.repositories.DisciplineRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class DisciplineService {
    private final DisciplineRepository disciplineRepository;


    public Map<Discipline, Double> mapDisciplinesAndRecomend(Map<Long, Double> map) {
        Map<Discipline, Double> res = new HashMap<>();

        for (Map.Entry<Long, Double> pair : map.entrySet()) {
            res.put(disciplineRepository.getById(pair.getKey()), pair.getValue());
        }

        return res;
    }
}
