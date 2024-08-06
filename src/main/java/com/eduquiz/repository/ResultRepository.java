package com.eduquiz.repository;

import com.eduquiz.model.Performer;
import com.eduquiz.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByUsername(String username);

    @Query("SELECT new com.eduquiz.model.Performer(r.quizId, r.name, r.schoolName, r.marks, r.coupon) " +
            "FROM Result r WHERE r.coupon IS NOT NULL " +
            "ORDER BY r.marks DESC")
    List<Performer> findTopPerformers();
}

