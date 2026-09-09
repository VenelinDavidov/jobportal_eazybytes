package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository <JobApplication, Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    void deleteByUserIdAndJobId(Long userId, Long jobId);

    List <JobApplication> findByUserIdOrderByAppliedAtDesc(Long userId);
}