package org.example.recomsystem.services.functiner;

import lombok.AllArgsConstructor;
import org.example.recomsystem.entities.Discipline;
import org.example.recomsystem.entities.Student;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class CalcInter implements Function<Student, Map.Entry<Long, Integer>> {

    private final List<Discipline> disciplineList;


    @Override
    public Map.Entry<Long, Integer> apply(Student st) {

        int inter = (int) disciplineList.stream().filter(st.getDisciplines()::contains).count();
        return Map.entry(st.getId(), inter);
    }
}
