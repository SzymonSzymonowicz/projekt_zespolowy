package com.myexaminer.modelDTO;

import com.myexaminer.model.Exam;
import com.myexaminer.model.Exercise;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExamDTO {

    private int idExam;

    private String examName;

    private String examDescription;

    private Date examAvailableDate;

    private List<ExerciseDTO> exercises;

    public int getIdExam() {
        return idExam;
    }

    private void setIdExam(int idExam) {
        this.idExam = idExam;
    }

    public String getExamName() {
        return examName;
    }

    private void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamDescription() {
        return examDescription;
    }

    private void setExamDescription(String examDescription) {
        this.examDescription = examDescription;
    }

    public Date getExamAvailableDate() {
        return examAvailableDate;
    }

    private void setExamAvailableDate(Date examAvailableDate) {
        this.examAvailableDate = examAvailableDate;
    }

    public List<ExerciseDTO> getExercises() {
        return exercises;
    }

    private List<ExerciseDTO>  setExercises(List<Exercise> exercises) {
        return exercises.stream().map(exercise -> new ExerciseDTO(exercise)).collect(Collectors.toList());
    }

    public ExamDTO(Exam exam) {
        this.idExam = exam.getIdExam();
        this.examName = exam.getExamName();
        this.examDescription = exam.getExamDescription();
        this.examAvailableDate = exam.getExamAvailableDate();
        this.exercises = setExercises(exam.getExercises());
    }

}
