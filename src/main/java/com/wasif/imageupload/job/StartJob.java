package com.wasif.imageupload.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartJob {
    private static final Logger log = LoggerFactory.getLogger(StartJob.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job filesBatchJob;

    public void startFilesJob(long reqId) throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        // Set job parameters
        Map<String, JobParameter> paramsMap = new HashMap<>();
        paramsMap.put("TIME", new JobParameter(new Date()));
        paramsMap.put("BY_USER", new JobParameter("John Doe"));
        paramsMap.put("REQ_ID", new JobParameter(reqId));

        JobParameters params = new JobParameters(paramsMap);

        // Start batch job execution
        JobExecution jobExecution = jobLauncher.run(filesBatchJob, params);
        log.info("Job execution status {}", jobExecution.getStatus());
    }

}
