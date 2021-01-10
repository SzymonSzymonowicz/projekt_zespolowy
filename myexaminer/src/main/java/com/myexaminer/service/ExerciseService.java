package com.myexaminer.service;

import com.google.gson.Gson;
import com.myexaminer.model.Exercise;
import com.myexaminer.repository.ExerciseRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository){this.exerciseRepository = exerciseRepository;}

    public void exerciseSave(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public boolean exerciseExistsById(int idExercise){
        Optional<Exercise> exerciseById = exerciseRepository.findByIdExercise(idExercise);

        return exerciseById.isPresent();
    }

    public Exercise returnExerciseById(int idExercise){
        Optional<Exercise> exerciseById = exerciseRepository.findByIdExercise(idExercise);

        return exerciseById.get();
    }

    public Iterable<Exercise> returnAllExercises(){
        return exerciseRepository.findAll();
    }

    public String getExerciseType(int idExercise){
        JSONObject obj = new JSONObject(
                exerciseRepository.findByIdExercise(idExercise).get().getExerciseBody());
        return obj.getString("type");
    }
}
