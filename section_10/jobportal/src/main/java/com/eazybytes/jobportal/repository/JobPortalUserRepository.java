package com.eazybytes.jobportal.repository;

import com.eazybytes.jobportal.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPortalUserRepository extends JpaRepository <JobPortalUser, Long> {


    Optional <JobPortalUser> readUserByMobileNumber(String mobileNumber);

    Optional <JobPortalUser> readUserByEmail(String email);
}