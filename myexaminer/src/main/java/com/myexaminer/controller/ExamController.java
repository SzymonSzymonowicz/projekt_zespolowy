package com.myexaminer.controller;

import com.myexaminer.model.Exam;
import com.myexaminer.model.Exercise;
import com.myexaminer.model.Student;
import com.myexaminer.modelDTO.ExamDTO;
import com.myexaminer.service.ExamService;
import com.myexaminer.service.ExerciseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping(path="/exam")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addNewExam (@RequestBody Exam exam) {
        if(examService.examExistsById(exam.getIdExam())){
            log.info("Exam with given ID -> {} <- ALREADY EXISTS", exam.getIdExam());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        examService.examSave(exam);
        log.info("Exam with ID -> {} <- has been ADDED", exam.getIdExam());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public @ResponseBody Exam getExam (@RequestBody Map<String, Integer> map_idExam) {
        Integer idExam = map_idExam.get("idExam");
        if(!examService.examExistsById(idExam)){
            log.info("Exam with given ID -> {} <- DOES NOT EXIST", idExam);
            return null;
        }

        Exam returnedExam = examService.returnExamById(idExam);

        log.info("Exam with ID -> {} <- HAS BEEN RETURNED", returnedExam.getIdExam());

        return returnedExam;
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<ExamDTO> getAllExams() {
        return examService.returnAllExamsDTO();
    }
}
