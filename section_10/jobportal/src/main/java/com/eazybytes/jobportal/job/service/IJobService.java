package com.eazybytes.jobportal.job.service;

import com.eazybytes.jobportal.dto.JobDto;

import java.util.List;

public interface IJobService {


    List<JobDto> getEmployerJobs(String employerEmail);
}
