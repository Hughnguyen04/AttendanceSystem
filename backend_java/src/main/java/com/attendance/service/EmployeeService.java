package com.attendance.service;

import com.attendance.model.Employee;
import com.attendance.dto.EmployeeDTO;
import com.attendance.repository.EmployeeRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findByIsActiveTrueOrderByIdDesc()
                .stream()
                .map(e -> modelMapper.map(e, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Integer id) {
        Employee employee = employeeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        Employee employee = modelMapper.map(dto, Employee.class);
        employee.setIsActive(true);
        Employee saved = employeeRepository.save(employee);
        return modelMapper.map(saved, EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployee(Integer id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        
        if (dto.getFullName() != null) employee.setFullName(dto.getFullName());
        if (dto.getAge() != null) employee.setAge(dto.getAge());
        if (dto.getDob() != null) employee.setDob(dto.getDob());
        if (dto.getSalary() != null) employee.setSalary(dto.getSalary());
        if (dto.getShiftId() != null) employee.setShiftId(dto.getShiftId());
        if (dto.getRole() != null) employee.setRole(Enum.valueOf(com.attendance.model.UserRole.class, dto.getRole()));
        if (dto.getIsActive() != null) employee.setIsActive(dto.getIsActive());
        
        Employee updated = employeeRepository.save(employee);
        return modelMapper.map(updated, EmployeeDTO.class);
    }

    public void deleteEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setIsActive(false);
        employeeRepository.save(employee);
    }

    public EmployeeDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        return modelMapper.map(employee, EmployeeDTO.class);
    }
}
