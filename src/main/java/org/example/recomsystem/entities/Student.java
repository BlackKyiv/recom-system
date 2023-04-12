package org.example.recomsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    private String email;

    private String address;

    @ManyToMany
    private List<Discipline> disciplines;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    List<StudentCharacteristics> characteristics;

    public Map<String, Integer> getCharacteristicRatings() {
        Map<String, Integer> studentRatings = new HashMap<>();
        characteristics.forEach(ch -> studentRatings.put(ch.getCharacteristics().getName(), ch.getRating()));
        return studentRatings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());

        return result;
    }


}