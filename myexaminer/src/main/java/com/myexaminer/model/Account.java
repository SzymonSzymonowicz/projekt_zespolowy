package com.myexaminer.model;

import com.myexaminer.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Account {
    // TODO rename column in sql
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private boolean isVerified;

    private String recoveryQuestion;

    private String recoveryAnswer;

    private boolean isLecturer;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "fk_account_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Account(String email, String password, String recoveryQuestion, String recoveryAnswer) {
        this.email = email;
        this.password = password;
        this.isVerified = false;
        this.recoveryQuestion = recoveryQuestion;
        this.recoveryAnswer = recoveryAnswer;
        this.isLecturer = false;
    }

    public void addToRoles(Role role){
        roles.add(role);
    }

    public boolean hasRole(RoleEnum roleEnum) {
        List<RoleEnum> roleEnums = roles.stream().map(role -> role.getName()).collect(Collectors.toList());

        return roleEnums.contains(roleEnum);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", is_verificated=" + isVerified +
                ", recovery_question='" + recoveryQuestion + '\'' +
                ", recovery_answer='" + recoveryAnswer + '\'' +
                ", is_lecturer=" + isLecturer +
                '}';
    }
}
