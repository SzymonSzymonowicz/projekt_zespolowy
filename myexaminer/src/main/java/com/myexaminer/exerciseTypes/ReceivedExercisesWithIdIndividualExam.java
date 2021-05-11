package com.myexaminer.exerciseTypes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReceivedExercisesWithIdIndividualExam {

    private List<ReceivedExercise> receivedExercises;

    private Long idIndividualExam;

    private Long idExam;
}
