package com.employee.employee_management.controller;

import com.employee.employee_management.model.Employee;
import com.employee.employee_management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HrController {

    private final EmployeeService employeeService;

    // Sab employees dekho
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>>
    getAllEmployees() {
        return ResponseEntity.ok(
                employeeService.getAllEmployees());
    }

    // Ek employee dekho
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee>
    getEmployee(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                employeeService.getEmployeeById(id));
    }

    // Employee update karo
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee>
    updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee emp) {
        return ResponseEntity.ok(
                employeeService.updateEmployee(id, emp));
    }
}