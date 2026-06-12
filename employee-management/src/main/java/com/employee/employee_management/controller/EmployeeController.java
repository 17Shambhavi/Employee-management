package com.employee.employee_management.security;

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
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    // Profile dekhna
    @GetMapping("/profile/{employeeId}")
    public ResponseEntity<?> getProfile(@PathVariable Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        }
        return ResponseEntity.status(404).body("Employee not found!");
    }

    // Salary dekhna
    @GetMapping("/salary/{employeeId}")
    public ResponseEntity<?> getSalary(@PathVariable Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "name", employee.get().getName(),
                    "salary", employee.get().getSalary()
            ));
        }
        return ResponseEntity.status(404).body("Employee not found!");
    }

    // Attendance mark karna
    @PostMapping("/attendance")
    public ResponseEntity<?> markAttendance(@RequestBody Map<String, String> request) {
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(Long.parseLong(request.get("employeeId")));
        attendance.setDate(LocalDate.now());
        attendance.setStatus(request.get("status"));
        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Attendance marked successfully!");
    }

    // Apni attendance dekhna
    @GetMapping("/attendance/{employeeId}")
    public ResponseEntity<?> getAttendance(@PathVariable Long employeeId) {
        List<Attendance> list = attendanceRepository.findByEmployeeId(employeeId);
        return ResponseEntity.ok(list);
    }

    // Leave request dalna
    @PostMapping("/leave")
    public ResponseEntity<?> applyLeave(@RequestBody Map<String, String> request) {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(Long.parseLong(request.get("employeeId")));
        leave.setFromDate(LocalDate.parse(request.get("fromDate")));
        leave.setToDate(LocalDate.parse(request.get("toDate")));
        leave.setReason(request.get("reason"));
        leave.setStatus("PENDING");
        leaveRequestRepository.save(leave);
        return ResponseEntity.ok("Leave request submitted!");
    }

    // Apni leaves dekhna
    @GetMapping("/leave/{employeeId}")
    public ResponseEntity<?> getLeaves(@PathVariable Long employeeId) {
        List<LeaveRequest> list = leaveRequestRepository.findByEmployeeId(employeeId);
        return ResponseEntity.ok(list);
    }
}