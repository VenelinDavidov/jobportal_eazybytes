package com.eazybytes.jobportal.job.service.impl;

import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.entity.Job;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.job.service.IJobService;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.repository.JobRepository;
import com.eazybytes.jobportal.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements IJobService {

    private final JobRepository jobRepository;
    private final JobPortalUserRepository userRepository;



    @Override
    public List <JobDto> getEmployerJobs(String employerEmail) {

        JobPortalUser employer = userRepository.findJobPortalUserByEmail (employerEmail)
                .orElseThrow (() -> new RuntimeException ("Employer not found"));

        if (employer.getCompany() == null){
         throw new RuntimeException ("Employer doesn't have a company assigned");
        }

        List <Job> jobs = employer.getCompany ().getJobs ();

        return jobs.stream()
                .map (job -> ApplicationUtility.transformJobToDto(job))
                .collect (Collectors.toList ());
    }



    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {

       // Validate employer and get their company
        JobPortalUser employer = userRepository.findJobPortalUserByEmail (employerEmail)
                .orElseThrow (() -> new RuntimeException ("Employer not found"));

        if (employer.getCompany () == null){
            throw new RuntimeException ("Employer does not have a company assigned. Please contact admin.");
        }

        Job job = tranformDtoToEntity(jobDto);
        job.setCompany (employer.getCompany());
        job.setPostedDate (Instant.now());
        job.setApplicationsCount (0);
        job.setStatus ("DRAFT");
        Job saveJob = jobRepository.save (job);

        return ApplicationUtility.transformJobToDto (saveJob);
    }

    @Override
    public JobDto updateJobStatus(Long jobId, String status, String employerEmail) {

        if(!status.equals ("ACTIVE") && !status.equals ("CLOSED") && !status.equals ("DRAFT")) {
            throw new IllegalArgumentException("Invalid status. Must be ACTIVE, CLOSED, or DRAFT");
        }

        JobPortalUser employer = userRepository.findJobPortalUserByEmail (employerEmail)
                .orElseThrow (() -> new RuntimeException ("Employer not found"));

        if (employer.getCompany () == null){
            throw new RuntimeException ("Employer doesn't have a company assigned");
        }

        Job job = employer.getCompany ()
                .getJobs ()
                .stream ()
                .filter (j -> j.getId ().equals (jobId))
                .findFirst ()
                .orElseThrow (() -> new RuntimeException ("Job not found"));

        job.setStatus (status);

        return ApplicationUtility.transformJobToDto (job);
    }

    //Method
    private Job tranformDtoToEntity(JobDto jobDto) {
        Job job = new Job ();
        BeanUtils.copyProperties (jobDto, job);
        return job;
    }
}
