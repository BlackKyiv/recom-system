package org.example.recomsystem.entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public class StudentCorrelationDTO {
    private Student student;
    private double correlation;
}
