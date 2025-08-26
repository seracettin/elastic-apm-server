package com.example.apmtest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Random;

/**
 * Service class providing various operations for APM testing.
 * Includes fast operations, slow operations, database simulations, and external API calls.
 */
@Service
public class TestService {

    private final WebClient webClient;
    private final Random random = new Random();

    public TestService() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    /**
     * Fast operation that completes quickly.
     * Simulates minimal processing for baseline performance.
     */
    public String fastOperation() {
        // Simulate very light processing
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append("fast-").append(i).append("-");
        }
        return sb.toString();
    }

    /**
     * Slow operation that introduces delays to simulate heavy processing.
     * 
     * @param delayMs The delay in milliseconds
     * @param simulateError Whether to simulate an error condition
     * @throws RuntimeException if simulateError is true
     */
    public String slowOperation(int delayMs, boolean simulateError) throws InterruptedException {
        // Simulate database query delay
        Thread.sleep(delayMs / 3);
        simulateDatabaseQuery();
        
        // Simulate processing delay
        Thread.sleep(delayMs / 3);
        simulateComplexProcessing();
        
        // Simulate external API call delay
        Thread.sleep(delayMs / 3);
        
        if (simulateError && random.nextDouble() < 0.3) {
            throw new RuntimeException("Simulated error in slow operation");
        }
        
        return "Slow operation completed after " + delayMs + "ms";
    }

    /**
     * Simulates database operations with varying complexity.
     */
    public String databaseOperation(String data) throws InterruptedException {
        // Simulate database connection time
        Thread.sleep(50 + random.nextInt(100));
        
        // Simulate query execution
        simulateDatabaseQuery();
        
        // Simulate data processing
        if (data != null && !data.isEmpty()) {
            // Process the data
            String processedData = data.toUpperCase().replaceAll("\\s+", "_");
            return "Processed: " + processedData;
        }
        
        return "Database operation completed";
    }

    /**
     * Simulates an external API call.
     */
    public String externalApiCall() {
        try {
            // Simulate calling an external service (using a public API for testing)
            String response = webClient.get()
                    .uri("https://jsonplaceholder.typicode.com/posts/1")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            
            return "External API response received: " + (response != null ? response.substring(0, Math.min(100, response.length())) + "..." : "null");
            
        } catch (Exception e) {
            // Return a simulated response in case of network issues
            return "Simulated external API response (network call failed): {\"id\": 1, \"title\": \"Sample Post\"}";
        }
    }

    /**
     * Simulates complex processing operations.
     */
    private void simulateComplexProcessing() throws InterruptedException {
        // Simulate CPU-intensive work
        long sum = 0;
        for (int i = 0; i < 100000; i++) {
            sum += Math.sqrt(i) * Math.sin(i);
        }
        
        // Add a small delay to make it visible in APM
        Thread.sleep(50 + random.nextInt(200));
    }

    /**
     * Simulates database query operations.
     */
    private void simulateDatabaseQuery() throws InterruptedException {
        // Simulate different types of database queries
        String[] queryTypes = {"SELECT", "INSERT", "UPDATE", "DELETE"};
        String queryType = queryTypes[random.nextInt(queryTypes.length)];
        
        // Different delays for different query types
        switch (queryType) {
            case "SELECT":
                Thread.sleep(30 + random.nextInt(100));
                break;
            case "INSERT":
                Thread.sleep(50 + random.nextInt(150));
                break;
            case "UPDATE":
                Thread.sleep(40 + random.nextInt(120));
                break;
            case "DELETE":
                Thread.sleep(60 + random.nextInt(140));
                break;
        }
    }
}
