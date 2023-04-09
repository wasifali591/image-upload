package com.wasif.imageupload.configuration;

import com.wasif.imageupload.job.JobListner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import com.wasif.imageupload.job.JobProcessor;
import com.wasif.imageupload.job.JobReader;
import com.wasif.imageupload.job.JobWriter;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobReader jobReader;
    @Autowired
    private JobProcessor jobProcessor;
    @Autowired
    private JobWriter jobWriter;
    @Autowired
    private JobListner jobListner;

    @Bean
    public Step step() {
        return stepBuilderFactory.get("setp1")
                .<MultipartFile, MultipartFile>chunk(2)
                .reader(jobReader)
                .processor(jobProcessor)
                .writer(jobWriter)
                .build();
    }

    @Bean
    public Job runBatchJob() {
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .listener(jobListner)
                .flow(step())
                .end()
                .build();
    }
}
