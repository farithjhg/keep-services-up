package com.wolfsoft.app;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTasks {
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	//@Qualifier("publishJob")
	Job job;

	/**The pattern is a list of six single space-separated fields: 
	 * representing second, minute, hour, day, month, weekday. 
	 * Month and weekday names can be given as the first three letters of the English names.
	 * Example patterns:

	    "0 0 * * * *" = the top of every hour of every day.*
	    "*\/10 * * * * *" = every ten seconds. Remove 2nd character, it is escape
	    "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
	    "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
	    "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
	    "0 0 0 25 12 ?" = every Christmas Day at midnight

	 */
	@Scheduled(cron = "*/240 * * * * MON-FRI")
	public void runJob() throws Exception {
		JobParametersBuilder param = new JobParametersBuilder().addLong("time", System.currentTimeMillis());
		jobLauncher.run(job, param.toJobParameters());
	}
}
