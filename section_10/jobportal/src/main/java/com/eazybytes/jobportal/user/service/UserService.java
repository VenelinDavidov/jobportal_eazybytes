package com.eazybytes.jobportal.user.service;


import com.eazybytes.jobportal.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {


    UserDto assignCompanyToEmployer(Long userId, Long companyId);

    Optional<UserDto> searchUserByEmail(String email);

    UserDto elevateToEmployer(Long userId);

    ProfileDto createOrUpdateProfile(String userEmail, String profileJson, MultipartFile profilePicture, MultipartFile resume) throws JsonProcessingException;

    ProfileDto getProfile(String userEmail);

    ProfileDto getProfilePicture(String userEmail);

    ProfileDto getResume(String userEmail);

    JobDto saveJob(String userEmail, Long jobId);

    void unsaveJob(String userEmail, Long jobId);

    List<JobDto> getSavedJobs(String userEmail);

    JobApplicationDto applyForJob(String userEmail, ApplyJobRequestDto applyJobRequestDto);

    void withdrawApplication(String userEmail, Long jobId);

    List<JobApplicationDto> getJobSeekerApplications(String userEmail);
}
