package com.myexaminer.controller;

import com.myexaminer.dto.ExamDTO;
import com.myexaminer.dto.GenericOneValue;
import com.myexaminer.dto.GenericTwoValues;
import com.myexaminer.entity.Exam;
import com.myexaminer.enums.State;
import com.myexaminer.service.ExamService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping(path = "/exam")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public Exam getExam(@RequestBody Map<String, Long> map_id) {
        return examService.getExam(map_id);
    }

    @PreAuthorize("hasRole('ROLE_LECTURER')")
    @PostMapping
    public Long addExam(@RequestBody ExamDTO examDTO) {
        return examService.createExam(examDTO);
    }

    @GetMapping("/{groupId}")
    public Iterable<ExamDTO> getAllExamsByGroupId(@PathVariable Long groupId) {
        return examService.getExamDTOSByIdGroup(groupId);
    }

    @GetMapping("/status")
    public State getExamStatus(@RequestBody GenericOneValue id) {
        return examService.getState(id);
    }

    @PutMapping("/status")
    public void changeExamStatus(@RequestBody GenericTwoValues genericTwoValues) {
        examService.updateExamStatus(genericTwoValues);
    }
}
