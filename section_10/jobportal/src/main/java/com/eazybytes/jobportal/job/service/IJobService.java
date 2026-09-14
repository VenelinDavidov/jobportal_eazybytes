package com.eazybytes.jobportal.job.service;

import com.eazybytes.jobportal.dto.JobApplicationDto;
import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.dto.UpdateJobApplicationDto;
import jakarta.validation.Valid;

import java.util.List;

public interface IJobService {


    List<JobDto> getEmployerJobs(String employerEmail);

    JobDto createJob( JobDto jobDto, String employerEmail);

    JobDto updateJobStatus(Long jobId, String upperCase, String employerEmail);

    List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId);

    boolean updateJobApplication(@Valid UpdateJobApplicationDto updateJobApplicationDto);
}
