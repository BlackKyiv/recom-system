package org.example.recomsystem.services;

import org.example.recomsystem.entities.Student;
import org.example.recomsystem.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CorrelationService {

    @Autowired
    private StudentRepository studentRepository;

    public Map<Long, Double> calculatePearsonCoefficients(long studentId) {
        // Retrieve the student's characteristics and ratings
        Student student = studentRepository.findById(studentId).orElse(null);
        Map<String, Integer> ratings = student.getCharacteristicRatings();

        // Retrieve all the other students' characteristics and ratings
        List<Student> otherStudents = studentRepository.findAllByIdNot(studentId);
        Map<Long, Map<String, Integer>> otherRatings = otherStudents.stream()
                .collect(Collectors.toMap(Student::getId, Student::getCharacteristicRatings));

        // Calculate the Pearson correlation coefficients for each characteristic
        Map<Long, Double> correlations = new HashMap<>();
        for (String characteristic : ratings.keySet()) {
            double sumX = 0;
            double sumY = 0;
            double sumXY = 0;
            double sumX2 = 0;
            double sumY2 = 0;
            int n = otherRatings.size();

            for (Map<String, Integer> otherRating : otherRatings.values()) {
                Integer ratingX = ratings.get(characteristic);
                Integer ratingY = otherRating.get(characteristic);

                if (ratingX != null && ratingY != null) {
                    sumX += ratingX;
                    sumY += ratingY;
                    sumXY += ratingX * ratingY;
                    sumX2 += ratingX * ratingX;
                    sumY2 += ratingY * ratingY;
                }
            }

            double numerator = (n * sumXY) - (sumX * sumY);
            double denominatorX = Math.sqrt((n * sumX2) - (sumX * sumX));
            double denominatorY = Math.sqrt((n * sumY2) - (sumY * sumY));
            double denominator = denominatorX * denominatorY;

            double correlation = denominator != 0 ? numerator / denominator : 0;
            correlations.put(studentId, correlation);
        }

        return correlations;
    }
}

