package com.employee.employee_management.security;

import com.employee.employee_management.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "143006";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            String token = jwtUtil.generateToken(username + ":ADMIN");
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", "ADMIN");
            return ResponseEntity.ok(response);
        }

        if (userService.validateUser(username, password)) {
            Optional<User> user = userService.findByUsername(username);
            String token = jwtUtil.generateToken(username + ":EMPLOYEE:" + user.get().getEmployeeId());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", "EMPLOYEE");
            response.put("employeeId", String.valueOf(user.get().getEmployeeId()));
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Invalid credentials!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        Long employeeId = Long.parseLong(request.get("employeeId"));
        userService.registerUser(username, password, employeeId);
        return ResponseEntity.ok("Employee registered successfully!");
    }
}