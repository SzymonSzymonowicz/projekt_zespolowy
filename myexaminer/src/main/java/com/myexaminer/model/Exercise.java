package com.myexaminer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private int idExercise;

    @Column(name = "exercise_body")
    private String exerciseBody;

    @ManyToOne
    @JoinColumn(name="fk_exam_id", nullable=false)
    private Exam exam;

    @OneToMany(mappedBy="exercise")
    private List<ArchiveExercise> archiveExercises;

    public Exercise(String exerciseBody, Exam exam, List<ArchiveExercise> archiveExercises) {
        this.exerciseBody = exerciseBody;
        this.exam = exam;
        this.archiveExercises = archiveExercises;
    }
}
