package com.example.apmtest.repository;

import com.example.apmtest.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for TestEntity operations.
 * Provides methods for testing database interactions with APM monitoring.
 */
@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

    /**
     * Find entities by name containing the given string.
     */
    List<TestEntity> findByNameContaining(String name);

    /**
     * Find entities created after a specific date.
     */
    List<TestEntity> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find entities with value greater than specified amount.
     */
    List<TestEntity> findByValueGreaterThan(Double value);

    /**
     * Custom query to find entities by name and value range.
     */
    @Query("SELECT t FROM TestEntity t WHERE t.name LIKE %:name% AND t.value BETWEEN :minValue AND :maxValue")
    List<TestEntity> findByNameAndValueRange(@Param("name") String name, 
                                           @Param("minValue") Double minValue, 
                                           @Param("maxValue") Double maxValue);

    /**
     * Count entities created today.
     */
    @Query("SELECT COUNT(t) FROM TestEntity t WHERE CAST(t.createdAt AS DATE) = CURRENT_DATE")
    Long countTodayRecords();
}
