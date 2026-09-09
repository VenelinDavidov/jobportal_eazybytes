package com.eazybytes.jobportal.user.service.impl;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.dto.*;
import com.eazybytes.jobportal.entity.*;
import com.eazybytes.jobportal.repository.*;
import com.eazybytes.jobportal.user.service.UserService;
import com.eazybytes.jobportal.util.ApplicationUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final JobPortalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final ProfileRepository profileRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;



    @Override
    public UserDto assignCompanyToEmployer(Long userId, Long companyId) {

        JobPortalUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Verify user is an employer
        if (!ApplicationConstants.ROLE_EMPLOYER.equals(user.getRole().getName())) {
            throw new RuntimeException("User must be an employer to be assigned to a company");
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

        user.setCompany(company);
        // JobPortalUser updatedUser = userRepository.save(user);

        return mapToUserDto(user);
    }

    @Override
    public Optional <UserDto> searchUserByEmail(String email) {
        return userRepository.findJobPortalUserByEmail(email)
                                               .map(this::mapToUserDto);
    }

    @Override
    public UserDto elevateToEmployer(Long userId) {

        JobPortalUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Check if user is already an employer
        if (ApplicationConstants.ROLE_EMPLOYER.equals(user.getRole().getName())) {
            return mapToUserDto(user);
        }
        // Check if user is already an admin
        if (ApplicationConstants.ROLE_ADMIN.equals(user.getRole().getName())) {
            throw new RuntimeException("Cannot elevate admin user to employer role");
        }

        // Find ROLE_EMPLOYER
        Role employerRole = roleRepository.findRoleByName(ApplicationConstants.ROLE_EMPLOYER)
                .orElseThrow(() -> new RuntimeException("ROLE_EMPLOYER not found"));

        user.setRole(employerRole);
        // JobPortalUser updatedUser = userRepository.save(user);
        /**
         * Why it’s unnecessary
         * findById() returns a managed entity
         * You modify it inside a transaction
         * Dirty checking automatically updates it
         */
        return mapToUserDto(user);
    }



    @Transactional
    @Override
    public ProfileDto createOrUpdateProfile(String userEmail, String profileJson, MultipartFile profilePicture, MultipartFile resume)
                                                                                                             throws JsonProcessingException {

        JobPortalUser user = userRepository.findJobPortalUserByEmail (userEmail)
                .orElseThrow (() -> new RuntimeException ("User not found with email: " + userEmail));

        Profile profile = user.getProfile();
        if (null == profile){
            profile = new Profile();
            profile.setUser(user);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        // Parse JSON string to ProfileDto
        ProfileDto profileDto = objectMapper.readValue (profileJson, ProfileDto.class);
        Profile savedProfile = profileRepository.save(mapToProfile(profile, profileDto, profilePicture, resume));
        return mapToProfileDto(savedProfile, false);
    }

    @Override
    public ProfileDto getProfile(String userEmail) {

        JobPortalUser user = userRepository.findJobPortalUserByEmail (userEmail)
                .orElseThrow (() -> new RuntimeException ("User not found with email: " + userEmail));

        if (user.getProfile ()== null) {
            return null;
        }

        return mapToProfileDto(user.getProfile(), false);
    }

    @Override
    public ProfileDto getProfilePicture(String userEmail) {

        JobPortalUser user = userRepository.findJobPortalUserByEmail (userEmail)
                .orElseThrow (() -> new RuntimeException ("User not found with email: " + userEmail));
        if (user.getProfile () == null ){
            return null;
        }
        return mapToProfileDto(user.getProfile(), true);
    }

    @Override
    public ProfileDto getResume(String userEmail) {

        JobPortalUser user = userRepository.findJobPortalUserByEmail (userEmail)
                .orElseThrow (() -> new RuntimeException ("User not found with email: " + userEmail));

        if (user.getProfile () == null ){
            return null;
        }
        return mapToProfileDto(user.getProfile(), true);
    }

    @Transactional
    @Override
    public JobDto saveJob(String userEmail, Long jobId) {

      // Validate user exist
        JobPortalUser user = userRepository.findJobPortalUserByEmail (userEmail)
                .orElseThrow (() -> new RuntimeException ("User not found with email: " + userEmail));
      // Validate job exist
        Job job = jobRepository.findById (jobId)
                .orElseThrow (() -> new RuntimeException ("Job not found with ID: " + jobId));

        user.getSavedJobs().add(job);
       // userRepository.save (user);
        return ApplicationUtility.transformJobToDto (job);
    }

    @Transactional
    @Override
    public void unsaveJob(String userEmail, Long jobId) {

        // Validate if user exists
        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        // Validate job exists
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        user.getSavedJobs().remove(job);
    }

    @Override
    public List <JobDto> getSavedJobs(String userEmail) {

        // Validate if user exists
        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));


        return user.getSavedJobs ()
                .stream ()
                .map (job -> ApplicationUtility.transformJobToDto (job))
                .collect(Collectors.toList());
    }
    @Transactional
    @Override
    public JobApplicationDto applyForJob(String userEmail, ApplyJobRequestDto applyJobRequestDto) {

        // Validate if user exists
        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        Long jobId = applyJobRequestDto.jobId ();
        if (jobApplicationRepository.existsByUserIdAndJobId (user.getId (), jobId)){
            throw new RuntimeException ("User has already applied for this job");
        }
       // Validate job exists
        Job job = jobRepository.findById (jobId)
                .orElseThrow (() -> new RuntimeException ("Job not found with ID: " + jobId));

        // Create job application
        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser (user);
        jobApplication.setJob (job);
        jobApplication.setAppliedAt (java.time.Instant.now ());
        jobApplication.setStatus (ApplicationConstants.PENDING_STATUS);
        jobApplication.setCoverLetter (applyJobRequestDto.coverLetter ());

        JobApplication saved = jobApplicationRepository.save (jobApplication);

        // Increment applications count
        job.setApplicationsCount (job.getApplicationsCount () != null
                ? job.getApplicationsCount () + 1
                : 1);
       // jobRepository.save(job); - Optional
        return mapToJobApplicationDto(saved);
    }

    @Override
    public void withdrawApplication(String userEmail, Long jobId) {

        // Validate if user exists
        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        if (!jobApplicationRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            throw new RuntimeException("You have not applied for this job");
        }

        jobApplicationRepository.deleteByUserIdAndJobId(user.getId(), jobId);

        // Get the job to update the count
        Job job = jobRepository.findById(jobId)
                               .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        // Decrement applications count (ensure it doesn't go below 0)
        if (job.getApplicationsCount () != null && job.getApplicationsCount () > 0){
            job.setApplicationsCount (job.getApplicationsCount () - 1);
            // jobRepository.save(job); - Optional
        }
    }

    @Override
    public List <JobApplicationDto> getJobSeekerApplications(String userEmail) {

        // Validate if user exists
        JobPortalUser user = userRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        return user.getJobApplications ()
                     .stream ()
                     .map (this::mapToJobApplicationDto)
                     .collect(Collectors.toList());
    }

    private JobApplicationDto mapToJobApplicationDto(JobApplication application) {

        // Map profile if exists
        ProfileDto profileDto = null;
        Profile profile = application.getUser ().getProfile ();

        if (profile != null){
            profileDto = new ProfileDto (
                    profile.getId(),
                    profile.getUser().getId(),
                    profile.getJobTitle(),
                    profile.getLocation(),
                    profile.getExperienceLevel(),
                    profile.getProfessionalBio(),
                    profile.getPortfolioWebsite(),
                    profile.getProfilePicture(),
                    profile.getProfilePictureName(),
                    profile.getProfilePictureType(),
                    profile.getResume(),
                    profile.getResumeName(),
                    profile.getResumeType(),
                    profile.getCreatedAt(),
                    profile.getUpdatedAt());
        }

        return new JobApplicationDto(
                application.getId(),
                application.getUser().getId(),
                application.getUser().getName(),
                application.getUser().getEmail(),
                application.getUser().getMobileNumber(),
                profileDto,
                ApplicationUtility.transformJobToDto(application.getJob()),
                application.getAppliedAt(),
                application.getStatus(),
                application.getCoverLetter(),
                application.getNotes()
        );
    }


    private ProfileDto mapToProfileDto(Profile profile, boolean includeBinaryData) {

        ProfileDto dto;
        if (includeBinaryData) {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), profile.getProfilePicture(),
                    profile.getProfilePictureName(), profile.getProfilePictureType(), profile.getResume(),
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt()
            );
        } else {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), null,
                    profile.getProfilePictureName(), profile.getProfilePictureType(), null,
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt());
        }
        return dto;
    }

    private Profile mapToProfile(Profile profile, ProfileDto profileDto, MultipartFile profilePicture, MultipartFile resume) {

            // Update text fields
            profile.setJobTitle(profileDto.jobTitle());
            profile.setLocation(profileDto.location());
            profile.setExperienceLevel(profileDto.experienceLevel());
            profile.setProfessionalBio(profileDto.professionalBio());
            profile.setPortfolioWebsite(profileDto.portfolioWebsite());
            // Handle profile picture upload
            if (profilePicture != null && !profilePicture.isEmpty()) {
                try {
                    profile.setProfilePicture(profilePicture.getBytes());
                    profile.setProfilePictureName(profilePicture.getOriginalFilename());
                    profile.setProfilePictureType(profilePicture.getContentType());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload profile picture", e);
                }
            }
            // Handle resume upload
            if (resume != null && !resume.isEmpty()) {
                try {
                    profile.setResume(resume.getBytes());
                    profile.setResumeName(resume.getOriginalFilename());
                    profile.setResumeType(resume.getContentType());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload resume", e);
                }
            }
            return profile;
    }


    private UserDto mapToUserDto(JobPortalUser user) {

        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        dto.setUserId(user.getId());
        dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
        dto.setCompanyId(user.getCompany() != null ? user.getCompany().getId() : null);
        dto.setCompanyName(user.getCompany() != null ? user.getCompany().getName() : null);
        return dto;
    }
}
