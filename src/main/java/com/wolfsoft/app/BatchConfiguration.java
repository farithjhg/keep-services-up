package com.wolfsoft.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.validation.BindException;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    
    @Bean
    public ItemReader<String>  reader() throws Throwable {
    	FlatFileItemReader<String> reader = new FlatFileItemReader<String>();
    	String service1 ="https://myoauth2-server.herokuapp.com/services/health/";
		reader.setResource(new UrlResource(service1));
        reader.setLineMapper(new DefaultLineMapper<String>() {{
            setLineTokenizer(new DelimitedLineTokenizer());
            setFieldSetMapper(new FieldSetMapper<String>() {
				@Override
				public String mapFieldSet(FieldSet fieldSet) throws BindException {
					return fieldSet.readString(0);
				}
			});
        }});
        return reader;
    }

    @Bean
    public MyItemProcessor processor() {
        return new MyItemProcessor();
    }

    @Bean
    public ItemWriter<String> writer() {
    	log.debug("Writer");
    	
        return new ItemWriter<String>() {
				@Override
				public void write(List<? extends String> items) throws Exception {
					log.info("write "+items.size()+" ["+items.get(0)+"]");
				}
		};
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobExecutionListener listener) throws Throwable {
        //JobExecutionListener new ;
		return jobBuilderFactory.get("publishJob")
        		.incrementer(new RunIdIncrementer())
        		.listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() throws Throwable {
        return stepBuilderFactory.get("step1")
        		.<String, String>chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    // end::jobstep[]
}
