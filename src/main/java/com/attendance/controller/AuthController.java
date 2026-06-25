package com.attendance.controller;

import com.attendance.dto.LoginRequest;
import com.attendance.dto.LoginResponse;
import com.attendance.model.Employee;
import com.attendance.repository.EmployeeRepository;
import com.attendance.security.JwtTokenProvider;
import com.attendance.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + loginRequest.getEmail()));

        System.out.println("role:" + employee.getRole().name());

        System.out.println("Input: " + loginRequest.getPassword());
        System.out.println("DB: " + employee.getPasswordHash());

        if (!loginRequest.getPassword().equals(employee.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }


        String token = jwtTokenProvider.generateToken(employee.getEmail(), employee.getRole().name());

        LoginResponse response = new LoginResponse(
                token,
                employee.getId(),
                employee.getEmail(),
                employee.getFullName(),
                employee.getRole().toString()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        boolean isValid = jwtTokenProvider.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("Profile endpoint");
    }
}
