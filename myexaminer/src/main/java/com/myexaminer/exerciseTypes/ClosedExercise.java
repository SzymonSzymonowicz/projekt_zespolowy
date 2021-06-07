package com.myexaminer.exerciseTypes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClosedExercise extends OpenExercise {
    private List<String> answers;

    public ClosedExercise(String type, String instruction, Long points, List<String> answers) {
        super(type, instruction, points);
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "ClosedExercise{" +
                "answers=" + answers +
                ", type='" + type + '\'' +
                ", instruction='" + instruction + '\'' +
                ", points=" + points +
                '}';
    }
}
