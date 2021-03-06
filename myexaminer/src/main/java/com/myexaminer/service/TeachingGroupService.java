package com.myexaminer.service;

import com.myexaminer.dto.GroupNameAndId;
import com.myexaminer.entity.Account;
import com.myexaminer.entity.Lecturer;
import com.myexaminer.entity.Student;
import com.myexaminer.entity.TeachingGroup;
import com.myexaminer.enums.RoleEnum;
import com.myexaminer.repository.TeachingGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TeachingGroupService {

    private final TeachingGroupRepository teachingGroupRepository;
    private final StudentService studentService;
    private final LecturerService lecturerService;
    private final AccountService accountService;

    public void saveTeachingGroup(TeachingGroup teachingGroup) {
        teachingGroupRepository.save(teachingGroup);
    }

    public boolean teachingGroupExistsById(Long id) {
        Optional<TeachingGroup> teachingGroupById = teachingGroupRepository.findById(id);

        return teachingGroupById.isPresent();
    }

    public List<TeachingGroup> findAllGroups() {
        return teachingGroupRepository.findAll();
    }

    public TeachingGroup getTeachingGroupById(Long id) {
        Optional<TeachingGroup> teachingGroup = teachingGroupRepository.findById(id);

        return teachingGroup.orElseThrow(() -> new EntityNotFoundException("Group with id " + id + " does not exist!"));
    }

    public TeachingGroup getTeachingGroupByStudentId(Long studentId) {
        Student student = studentService.getStudentById(studentId);

        return teachingGroupRepository
                .findByStudentsContaining(student)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no group containing student with id " + studentId + "."));
    }

    public boolean teachingGroupExistsByName(String teachingGroupName) {
        Optional<TeachingGroup> teachingGroupExistsByName = teachingGroupRepository.findByName(teachingGroupName);

        return teachingGroupExistsByName.isPresent();
    }

    public void addStudentToTeachingGroup(Long groupId, Long studentId) {
        TeachingGroup teachingGroup = getTeachingGroupById(groupId);

        Student student = studentService.getStudentById(studentId);

        teachingGroup.addStudent(student);

        saveTeachingGroup(teachingGroup);

        student.addToTeachingGroups(teachingGroup);

        studentService.studentSave(student);
    }

    public List<TeachingGroup> returnTeachingGroupsByLecturerEmail(String email) {
        return teachingGroupRepository.findByLecturerAccountEmail(email);
    }

    public List<TeachingGroup> getTeachingGroupsByLecturerId(Long id) {
        return teachingGroupRepository.findByLecturerId(id);
    }

    public void createTeachingGroup(String groupName, Authentication authentication) {
        if (teachingGroupExistsByName(groupName)) {
            log.info("Group with given name -> {} <- ALREADY EXISTS", groupName);
            throw new EntityExistsException("Group with given name ->" + groupName + "<- ALREADY EXISTS");
        }

        Lecturer lecturer = lecturerService.findLecturerByEmail(authentication.getName());

        TeachingGroup teachingGroup = TeachingGroup.builder()
                .name(groupName)
                .startingDate(LocalDateTime.now())
                .accessCode(RandomStringUtils.randomAlphanumeric(8))
                .lecturer(lecturer)
                .build();

        saveTeachingGroup(teachingGroup);
        log.info("Group with ID -> {} <- has been ADDED", teachingGroup.getId());
    }

    public void addStudentToGroup(Long groupId, Long studentId) {
        if (!teachingGroupExistsById(groupId)) {
            log.info("Group with given ID -> {} <- DOES NOT EXIST", groupId);
            return;
        }

        if (!studentService.studentExistsById(studentId)) {
            log.info("Student with given ID -> {} <- DOES NOT EXIST", studentId);
            return;
        }

        addStudentToTeachingGroup(groupId, studentId);
        log.info("Student with ID -> {} <- has been ADDED to TeachingGroup with ID -> {} <-", studentId, groupId);
    }

    public void removeStudentFromGroup(Long groupId, Long studentId) {
        TeachingGroup group = getTeachingGroupById(groupId);
        group.removeStudent(studentService.getStudentById(studentId));
        teachingGroupRepository.save(group);
    }

    public void deleteGroup(Long groupId) {
        teachingGroupRepository.deleteById(groupId);
        log.info("TeachingGroup with ID -> {} <- has been deleted", groupId);
    }

    public TeachingGroup findTeachingGroupByAccessCode(String code) {
        return teachingGroupRepository.findByAccessCode(code)
                .orElseThrow(() -> new EntityNotFoundException("There is no teaching group with given code -> " + code));
    }

    public ResponseEntity addStudentToGroupByCode(String accessCode, Authentication authentication) {
        TeachingGroup teachingGroup = findTeachingGroupByAccessCode(accessCode);
        Student student = studentService.findStudentByEmail(authentication.getName());

        teachingGroup.addStudent(student);
        teachingGroupRepository.save(teachingGroup);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Student " + authentication.getName() + " has been added to teaching group");
    }

    public List<TeachingGroup> getTeachingGroupByAccountId(Long accountId) {
        Account account = accountService.getAccountById(accountId);

        if (account.hasRole(RoleEnum.ROLE_STUDENT)) {
            Student student = studentService.getStudentByAccountId(account.getId());

            return teachingGroupRepository.findAllByStudents(student);
        } else if (account.hasRole(RoleEnum.ROLE_LECTURER)) {
            Lecturer lecturer = lecturerService.getLecturerByAccountId(account.getId());

            return teachingGroupRepository.findAllByLecturer(lecturer);
        }
        //TODO find better exception for this
        throw new IllegalArgumentException("Your account doesn't posses required roles. Contact administrators of the application.");
    }

    public void editGroup(Long groupId, TeachingGroup teachingGroup) {
        TeachingGroup dbGroup = getTeachingGroupById(groupId);

        dbGroup.setName(teachingGroup.getName());
        dbGroup.setStartingDate(teachingGroup.getStartingDate());

        teachingGroupRepository.save(dbGroup);
    }

    public List<GroupNameAndId> getTeachingGroupNameAndIdByAccountId(Long accountId) {
        Account account = accountService.getAccountById(accountId);

        if (account.hasRole(RoleEnum.ROLE_STUDENT)) {
            Student student = studentService.getStudentByAccountId(account.getId());

            return teachingGroupRepository.getAllGroupsNameAndIdByStudent(student);
        } else if (account.hasRole(RoleEnum.ROLE_LECTURER)) {
            Lecturer lecturer = lecturerService.getLecturerByAccountId(account.getId());

            return teachingGroupRepository.getAllGroupsNameAndIdByLecturer(lecturer);
        }
        //TODO find better exception for this
        throw new IllegalArgumentException("Your account doesn't posses required roles. Contact administrators of the application.");
    }
}
