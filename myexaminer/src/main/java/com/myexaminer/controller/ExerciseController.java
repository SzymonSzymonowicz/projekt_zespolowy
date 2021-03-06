package com.myexaminer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myexaminer.dto.ExerciseDTO;
import com.myexaminer.entity.Exercise;
import com.myexaminer.exerciseTypes.OpenExercise;
import com.myexaminer.exerciseTypes.ReceivedExercise;
import com.myexaminer.service.ExerciseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log4j2
@RestController
@RequestMapping(path = "/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public Exercise getExercise(@RequestBody Long exerciseId) {
        return exerciseService.getExerciseById(exerciseId);
    }

    @PostMapping
    public void addExercise(@RequestBody Exercise exercise) {
        exerciseService.createExercise(exercise);
    }

    @GetMapping("/{id}")
    public Iterable<ExerciseDTO> getAllExercisesById(@PathVariable Long id, HttpServletRequest request) {
        return exerciseService.getExerciseDTOList(id, request);
    }

    @PostMapping("/save")
    public void saveExercises(@RequestBody List<ReceivedExercise> receivedExerciseList) {
        exerciseService.saveExercises(receivedExerciseList);
    }

    @PostMapping("/exam/{id}")
    @PreAuthorize("hasRole('ROLE_LECTURER')")
    public void createExercise(@RequestBody OpenExercise exercise,
                               @PathVariable Long id) throws JsonProcessingException {
        exerciseService.createExercise(exercise, id);
    }

    @DeleteMapping("/{exerciseId}")
    @PreAuthorize("hasRole('ROLE_LECTURER')")
    public void deleteExercise(@PathVariable Long exerciseId) throws JsonProcessingException {
        exerciseService.deleteExercise(exerciseId);
    }
}
