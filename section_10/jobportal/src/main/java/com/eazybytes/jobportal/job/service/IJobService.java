package com.eazybytes.jobportal.job.service;

import com.eazybytes.jobportal.dto.JobDto;
import jakarta.validation.Valid;

import java.util.List;

public interface IJobService {


    List<JobDto> getEmployerJobs(String employerEmail);

    JobDto createJob( JobDto jobDto, String employerEmail);

    JobDto updateJobStatus(Long jobId, String upperCase, String employerEmail);

}
