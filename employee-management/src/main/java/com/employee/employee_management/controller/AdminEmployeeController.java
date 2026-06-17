package com.employee.employee_management.controller;

import com.employee.employee_management.model.Attendance;
import com.employee.employee_management.model.Employee;
import com.employee.employee_management.model.LeaveRequest;
import com.employee.employee_management.repository.AttendanceRepository;
import com.employee.employee_management.repository.EmployeeRepository;
import com.employee.employee_management.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminEmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    // Get All Employees
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Add Employee
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // Delete Employee
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.ok("Deleted!");
    }

    // Update Employee
    @PutMapping("/employees/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmp) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setName(updatedEmp.getName());
            emp.setDepartment(updatedEmp.getDepartment());
            emp.setDesignation(updatedEmp.getDesignation());
            emp.setEmail(updatedEmp.getEmail());
            emp.setMobileNumber(updatedEmp.getMobileNumber());
            emp.setSalary(updatedEmp.getSalary());
            employeeRepository.save(emp);
            return ResponseEntity.ok("Updated!");
        }).orElse(ResponseEntity.status(404).body("Not found!"));
    }

    // Search by Department
    @GetMapping("/employees/department/{dept}")
    public List<Employee> getByDepartment(@PathVariable String dept) {
        return employeeRepository.findByDepartment(dept);
    }

    // Filter by Salary
    @GetMapping("/employees/salary/{salary}")
    public List<Employee> getBySalary(@PathVariable Double salary) {
        return employeeRepository.findBySalaryGreaterThan(salary);
    }

    // Pending Leaves
    @GetMapping("/leaves/pending")
    public List<LeaveRequest> getPendingLeaves() {
        return leaveRequestRepository.findByStatus("PENDING");
    }

    // Approve Leave
    @PutMapping("/leave/{id}/approve")
    public ResponseEntity<?> approveLeave(@PathVariable Long id) {
        return leaveRequestRepository.findById(id).map(leave -> {
            leave.setStatus("APPROVED");
            leaveRequestRepository.save(leave);
            return ResponseEntity.ok("Approved!");
        }).orElse(ResponseEntity.status(404).body("Not found!"));
    }

    // Reject Leave
    @PutMapping("/leave/{id}/reject")
    public ResponseEntity<?> rejectLeave(@PathVariable Long id) {
        return leaveRequestRepository.findById(id).map(leave -> {
            leave.setStatus("REJECTED");
            leaveRequestRepository.save(leave);
            return ResponseEntity.ok("Rejected!");
        }).orElse(ResponseEntity.status(404).body("Not found!"));
    }

    // Today's Attendance Count
    @GetMapping("/attendance/today")
    public ResponseEntity<?> getTodayAttendance() {
        List<Attendance> list = attendanceRepository.findByDateAndStatus(LocalDate.now(), "PRESENT");
        return ResponseEntity.ok(Map.of("presentCount", list.size()));
    }
}