package com.example.attendance.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.attendance.entity.Employee;
import com.example.attendance.repository.EmployeeRepository;

@Service
public class AuthService {
    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;

    public AuthService(EmployeeRepository employeeRepository, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.jwtService = jwtService;
    }

    public Map<String, Object> login(String email, String password) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không đúng"));

        if (!Objects.equals(password, employee.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không đúng");
        }

        String token = jwtService.generateToken(employee.getId(), employee.getEmail(), employee.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("access_token", token);
        data.put("token_type", "bearer");
        return data;
    }

    public Map<String, Object> getCurrentEmployeeProfile(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ");
        }

        Map<String, Object> claims = jwtService.parseToken(authorizationHeader.substring(7));
        if (claims == null || claims.get("user_id") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ");
        }

        Long userId = ((Number) claims.get("user_id")).longValue();
        Employee employee = employeeRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy nhân viên"));

        Map<String, Object> data = new HashMap<>();
        data.put("id", employee.getId());
        data.put("full_name", employee.getFullName());
        data.put("email", employee.getEmail());
        data.put("role", employee.getRole());
        data.put("department_id", null);
        return data;
    }
}
