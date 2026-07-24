package com.example.jpetstore.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final JdbcTemplate jdbcTemplate;

    public HelloController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello World! JPetStore Partial API is running.");
        
        try {
            String dbVersion = jdbcTemplate.queryForObject("SELECT version()", String.class);
            response.put("databaseStatus", "Connected to PostgreSQL");
            response.put("databaseVersion", dbVersion);
        } catch (Exception e) {
            response.put("databaseStatus", "Disconnected / Initializing: " + e.getMessage());
        }

        return response;
    }
}
