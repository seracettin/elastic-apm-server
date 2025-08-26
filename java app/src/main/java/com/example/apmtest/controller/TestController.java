package com.example.apmtest.controller;

import com.example.apmtest.service.TestService;
import com.example.apmtest.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller providing test endpoints for APM monitoring.
 * Includes both fast and slow endpoints to test different performance scenarios.
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private DatabaseService databaseService;

    /**
     * Fast endpoint that responds quickly with minimal processing.
     * Useful for baseline performance testing.
     */
    @GetMapping("/fast")
    public ResponseEntity<Map<String, Object>> fastEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("endpoint", "fast");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "This is a fast response!");
        response.put("executionTime", "< 50ms");
        
        // Simulate minimal processing
        testService.fastOperation();
        
        return ResponseEntity.ok(response);
    }

    /**
     * Slow endpoint that simulates slow operations with delays.
     * Useful for testing performance monitoring and alerts.
     */
    @GetMapping("/slow")
    public ResponseEntity<Map<String, Object>> slowEndpoint(
            @RequestParam(defaultValue = "2000") int delayMs,
            @RequestParam(defaultValue = "false") boolean simulateError) {
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Simulate slow processing
            testService.slowOperation(delayMs, simulateError);
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("endpoint", "slow");
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "This is a slow response after processing!");
            response.put("requestedDelay", delayMs + "ms");
            response.put("actualExecutionTime", executionTime + "ms");
            response.put("databaseQueries", 3);
            response.put("externalApiCalls", 1);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("endpoint", "slow");
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("error", "Simulated error occurred");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("executionTime", (System.currentTimeMillis() - startTime) + "ms");
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Endpoint to test database operations for APM monitoring.
     */
    @PostMapping("/database")
    public ResponseEntity<Map<String, Object>> databaseEndpoint(@RequestBody(required = false) Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Initialize sample data if needed
            databaseService.createSampleRecords();
            
            // Perform complex database operations
            String searchTerm = (request != null) ? request.get("searchTerm") : null;
            String queryResult = databaseService.performComplexQuery(searchTerm);
            String updateResult = databaseService.updateRecords();
            
            response.put("endpoint", "database");
            response.put("timestamp", LocalDateTime.now());
            response.put("operation", "CREATE/READ/UPDATE/DELETE");
            response.put("queryResult", queryResult);
            response.put("updateResult", updateResult);
            response.put("searchTerm", searchTerm);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", "Database operation failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Simple GET endpoint for database testing.
     */
    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> getDatabaseEndpoint(@RequestParam(required = false) String search) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String result = databaseService.performComplexQuery(search);
            
            response.put("endpoint", "database");
            response.put("timestamp", LocalDateTime.now());
            response.put("operation", "READ");
            response.put("result", result);
            response.put("searchTerm", search);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", "Database operation failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Endpoint to simulate external API calls.
     */
    @GetMapping("/external")
    public ResponseEntity<Map<String, Object>> externalApiEndpoint() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String apiResult = testService.externalApiCall();
            
            response.put("endpoint", "external");
            response.put("timestamp", LocalDateTime.now());
            response.put("externalApiResponse", apiResult);
            response.put("responseTime", "1200ms");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", "External API call failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("service", "apm-test-app");
        return ResponseEntity.ok(response);
    }
}
