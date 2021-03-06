package com.myexaminer.controller;

import com.myexaminer.dto.RegisterDTO;
import com.myexaminer.entity.Account;
import com.myexaminer.entity.Role;
import com.myexaminer.security.jwt.JwtUtils;
import com.myexaminer.security.payload.JwtResponse;
import com.myexaminer.security.service.AccountDetails;
import com.myexaminer.service.AccountService;
import com.myexaminer.service.RegistrationService;
import com.myexaminer.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final RegistrationService registrationService;
    private final StudentService studentService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // TODO Validation for registerDTO
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody RegisterDTO registerDTO) {
        if (accountService.accountExistsByEmail(registerDTO.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Error: This email is already used!");
        }

        if (studentService.studentExistsByIndex(registerDTO.getIndex())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Error: This student index is already used!");
        }

        registrationService.registerNewStudentToDatabase(registerDTO);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        List<String> roles = accountDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(JwtResponse.builder()
                .token(jwt)
                .id(accountDetails.getId())
                .email(accountDetails.getUsername())
                .roles(roles)
                .build());
    }

    @GetMapping("/role")
    public Set<Role> role(HttpServletRequest request) {
        return accountService.getAccountByEmail(request.getUserPrincipal().getName()).getRoles();
    }

    @GetMapping(value = "/unique", params = "index")
    public boolean checkIfIndexIsUnique(@RequestParam String index) {
        return !studentService.studentExistsByIndex(index);
    }

    @GetMapping(value = "/unique", params = "email")
    public boolean checkIfEmailIsUnique(@RequestParam String email) {
        return !accountService.accountExistsByEmail(email);
    }
}
