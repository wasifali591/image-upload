package com.wasif.imageupload.configuration;

import com.wasif.imageupload.job.ImageJobListner;
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

import com.wasif.imageupload.job.ImageJobProcessor;
import com.wasif.imageupload.job.ImageJobReader;
import com.wasif.imageupload.job.ImageJobWriter;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ImageJobReader imageJobReader;
    @Autowired
    private ImageJobProcessor imageJobProcessor;
    @Autowired
    private ImageJobWriter imageJobWriter;
    @Autowired
    private ImageJobListner imageJobListner;

    @Bean
    public Step step() {
        return stepBuilderFactory.get("setp1")
                .<MultipartFile, MultipartFile>chunk(2)
                .reader(imageJobReader)
                .processor(imageJobProcessor)
                .writer(imageJobWriter)
                .build();
    }

    @Bean
    public Job runBatchJob() {
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .listener(imageJobListner)
                .flow(step())
                .end()
                .build();
    }
}
