package org.example.recomsystem.services;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.recomsystem.entities.Discipline;
import org.example.recomsystem.entities.Student;
import org.example.recomsystem.repositories.DisciplineRepository;
import org.example.recomsystem.repositories.StudentRepository;
import org.example.recomsystem.services.functiner.CalcInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    private final double coefficientLimit = 0.7;


    public Map<Long, Double> calculateDisciplinesRateForStudent(long studentId) throws NotFoundException {
        List<Long> commonStudentIds = findStudentIdsWithCommonDisciplines(studentId);
        Map<Long, Double> studentCoefficients = calculatePearsonCoefficients(studentId, commonStudentIds);
        Map<Long, Double> disciplinesRate = disciplineRepository.findAll().stream()
                .map(d -> Map.entry(d.getId(), 0.0))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<Student> commonStudents = studentRepository.findAllById(commonStudentIds);
        for (Student st : commonStudents) {
            for (Discipline d : st.getDisciplines()) {
                disciplinesRate.put(
                        d.getId(),
                        disciplinesRate.get(d.getId()) + studentCoefficients.get(st.getId()));
            }
        }
        System.out.println(disciplinesRate);
        return disciplinesRate;
    }

    public List<Long> findStudentIdsWithCommonDisciplines(long studentId) throws NotFoundException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Not found student with id " + studentId));

        System.out.println(student);
        List<Discipline> studentDisciplines = student.getDisciplines();

        Map<Long, Integer> otherStudsInters = studentRepository.findAllByIdNot(studentId).stream()
                .map(new CalcInter(studentDisciplines))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(otherStudsInters);
        List<Long> res = normalizeList(otherStudsInters).stream()
                .filter(entry -> entry.getValue() >= coefficientLimit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println(res);
        return res;
    }

    public List<Map.Entry<Long, Double>> normalizeList(Map<Long, Integer> list) {
        int min = list.values().stream().mapToInt(Integer::intValue).min().orElse(0);
        int max = list.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        int range = max - min;
        return list.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), (entry.getValue() - min) * 1.0 / range))
                .collect(Collectors.toList());
    }

    public static double findCorrelation(List<Integer> xs, List<Integer> ys) {
        //TODO: check here that arrays are not null, of the same length etc

        double sx = 0.0;
        double sy = 0.0;
        double sxx = 0.0;
        double syy = 0.0;
        double sxy = 0.0;

        int n = xs.size();

        for (int i = 0; i < n; ++i) {
            double x = xs.get(i);
            double y = ys.get(i);

            sx += x;
            sy += y;
            sxx += x * x;
            syy += y * y;
            sxy += x * y;
        }

        // covariation
        double cov = sxy / n - sx * sy / n / n;
        // standard error of x
        double sigmax = Math.sqrt(sxx / n - sx * sx / n / n);
        // standard error of y
        double sigmay = Math.sqrt(syy / n - sy * sy / n / n);

        // correlation is just a normalized covariation
        return cov / sigmax / sigmay;
    }

    public Map<Long, Double> calculatePearsonCoefficients(long studentId, List<Long> commonIds) throws
            NotFoundException {
        // Retrieve the student's characteristics and ratings
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Not found student with id " + studentId));

        Map<String, Integer> ratings = student.getCharacteristicRatings();

        // Retrieve all the other students' characteristics and ratings
        List<Student> otherStudents = studentRepository.findAllById(commonIds);
        Map<Long, Map<String, Integer>> otherRatings = otherStudents.stream()
                .collect(Collectors.toMap(Student::getId, Student::getCharacteristicRatings));

        // Calculate the Pearson correlation coefficients for each characteristic
        Map<Long, Double> correlations = new HashMap<>();

        List<Integer> targetStRatings = new ArrayList<>(student.getCharacteristicRatings().values());
        for (Long stId : otherRatings.keySet()) {
            List<Integer> stRatings = new ArrayList<>(student.getCharacteristicRatings().values());
            correlations.put(stId, findCorrelation(targetStRatings, stRatings));
        }


        return correlations;
    }
}
