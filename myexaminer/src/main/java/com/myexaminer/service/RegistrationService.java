package com.myexaminer.service;

import com.myexaminer.dto.RegisterDTO;
import com.myexaminer.entity.Account;
import com.myexaminer.entity.Notebook;
import com.myexaminer.entity.Role;
import com.myexaminer.entity.Student;
import com.myexaminer.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RoleService roleService;
    private final AccountService accountService;
    private final StudentService studentService;
    private final NotebookService notebookService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewStudentToDatabase(RegisterDTO registerDTO) {

        Account newAccount = new Account(
                registerDTO.getEmail(),
                passwordEncoder.encode(registerDTO.getPassword()),
                registerDTO.getRecoveryQuestion(),
                registerDTO.getRecoveryAnswer()
        );
        Role studentRole = roleService.getRoleByName(RoleEnum.ROLE_STUDENT);
        newAccount.addToRoles(studentRole);
        accountService.accountSave(newAccount);
        log.info("Account with ID -> {} <- has been CREATED and SAVED to database", newAccount.getId());

        Student newStudent = new Student(
                newAccount.getId(),
                registerDTO.getFirstName(),
                registerDTO.getLastName(),
                registerDTO.getIndex(),
                registerDTO.getFaculty(),
                registerDTO.getFieldOfStudy()
        );
        studentService.studentSave(newStudent);
        log.info("Student with ID -> {} <- has been CREATED and SAVED to database", newStudent.getId());

        Notebook newNotebok = new Notebook("Miłej nauki !", newAccount);
        notebookService.notebookSave(newNotebok);

        log.info("Notebook with ID -> {} <- has been CREATED and SAVED to database", newNotebok.getId());
    }
}