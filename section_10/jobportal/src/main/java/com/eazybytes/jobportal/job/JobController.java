package com.eazybytes.jobportal.job;

import com.eazybytes.jobportal.dto.JobApplicationDto;
import com.eazybytes.jobportal.dto.JobDto;
import com.eazybytes.jobportal.dto.UpdateJobApplicationDto;
import com.eazybytes.jobportal.job.service.IJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final IJobService jobService;

    @GetMapping(path = "/employer", version = "1.0")
    public ResponseEntity<List<JobDto>> getEmployersJobs(Authentication authentication) {

        String employerEmail = authentication.getName ();
        List <JobDto> jobs = jobService.getEmployerJobs(employerEmail);

        return ResponseEntity.ok(jobs);
    }


    @PostMapping(path = "/employer", version = "1.0")
    public ResponseEntity<JobDto> createJob(@RequestBody @Valid JobDto jobDto, Authentication authentication) {

        String employerEmail = authentication.getName();
        JobDto createdJob = jobService.createJob(jobDto,employerEmail);

        return ResponseEntity.status (HttpStatus.CREATED)
                             .body (createdJob);
    }

    @PatchMapping(path = "/{jobId}/status/employer")
    public ResponseEntity<?> updateJobStatus(@PathVariable Long jobId,
                                             @RequestBody Map <String, String> requestBody,
                                             Authentication authentication){

        String employerEmail = authentication.getName ();
        String status = requestBody.get ("status");

        if (status == null || status.trim().isEmpty ()) {
            return ResponseEntity.status (HttpStatus.BAD_REQUEST)
                    .body (Map.of ("message", "Status is required"));

        }
        JobDto updatedJob = jobService.updateJobStatus(jobId, status.toUpperCase (), employerEmail);

        return ResponseEntity.ok()
                             .body(updatedJob);
    }

    @GetMapping("/applications/{jobId}/employer")
    public ResponseEntity <List<JobApplicationDto>>getApplicationsByJobForEmployer(@PathVariable Long jobId) {

        List <JobApplicationDto> jobApplications = jobService.getApplicationsByJobForEmployer(jobId);
        return ResponseEntity.ok(jobApplications);
    }

    @PatchMapping("/applications/employer")
    public ResponseEntity<String> updateJobApplication(@RequestBody @Valid UpdateJobApplicationDto updateJobApplicationDto){

       boolean isUpdated = jobService.updateJobApplication(updateJobApplicationDto);

       if (!isUpdated){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update application");
       }
        return ResponseEntity.ok("Application updated successfully");
    }
}
