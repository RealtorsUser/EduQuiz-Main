package com.eduquiz.repository;

import com.eduquiz.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("SELECT r FROM Result r ORDER BY r.marks DESC")
    List<Result> findTopPerformers();

    List<Result> findByUserName(String username);
}

