package com.example.apmtest.service;

import com.example.apmtest.entity.TestEntity;
import com.example.apmtest.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service for database operations with APM monitoring.
 * Provides realistic database interactions for testing.
 */
@Service
@Transactional
public class DatabaseService {

    @Autowired
    private TestRepository testRepository;

    private final Random random = new Random();

    /**
     * Create sample test records.
     */
    public List<TestEntity> createSampleRecords() {
        String[] names = {"Order Processing", "User Management", "Payment Gateway", "Inventory Check", "Email Service"};
        String[] descriptions = {
                "Processing customer order",
                "Managing user authentication",
                "Handling payment transactions",
                "Checking inventory levels",
                "Sending notification emails"
        };

        for (int i = 0; i < 5; i++) {
            TestEntity entity = new TestEntity(
                    names[i],
                    descriptions[i],
                    random.nextDouble() * 1000
            );
            testRepository.save(entity);
        }

        return testRepository.findAll();
    }

    /**
     * Perform complex database query operations.
     */
    public String performComplexQuery(String searchTerm) {
        // Multiple database operations to generate APM traces
        List<TestEntity> allRecords = testRepository.findAll();
        
        List<TestEntity> filteredByName = testRepository.findByNameContaining(searchTerm != null ? searchTerm : "Order");
        
        List<TestEntity> recentRecords = testRepository.findByCreatedAtAfter(LocalDateTime.now().minusHours(1));
        
        List<TestEntity> highValueRecords = testRepository.findByValueGreaterThan(500.0);
        
        Long todayCount = testRepository.countTodayRecords();

        return String.format("Query results - Total: %d, By name: %d, Recent: %d, High value: %d, Today: %d",
                allRecords.size(), filteredByName.size(), recentRecords.size(), highValueRecords.size(), todayCount);
    }

    /**
     * Update existing records.
     */
    public String updateRecords() {
        List<TestEntity> records = testRepository.findAll();
        int updated = 0;
        
        for (TestEntity record : records) {
            if (random.nextDouble() < 0.5) { // Update 50% of records
                record.setValue(record.getValue() * 1.1); // Increase value by 10%
                record.setDescription(record.getDescription() + " (Updated)");
                testRepository.save(record);
                updated++;
            }
        }

        return String.format("Updated %d out of %d records", updated, records.size());
    }

    /**
     * Delete old records.
     */
    public String cleanupOldRecords() {
        List<TestEntity> oldRecords = testRepository.findByCreatedAtAfter(LocalDateTime.now().minusMinutes(5));
        int deleted = 0;
        
        for (TestEntity record : oldRecords) {
            if (record.getValue() < 100) { // Delete low value records
                testRepository.delete(record);
                deleted++;
            }
        }

        return String.format("Deleted %d old records", deleted);
    }

    /**
     * Get record by ID with error simulation.
     */
    public Optional<TestEntity> getRecordById(Long id, boolean simulateError) {
        if (simulateError && random.nextDouble() < 0.2) {
            throw new RuntimeException("Database connection timeout simulated");
        }
        
        return testRepository.findById(id);
    }
}
