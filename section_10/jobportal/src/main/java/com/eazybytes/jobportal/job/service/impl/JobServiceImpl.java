package com.eazybytes.jobportal.job.service.impl;

import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.entity.Job;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.job.service.IJobService;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.repository.JobRepository;
import com.eazybytes.jobportal.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
