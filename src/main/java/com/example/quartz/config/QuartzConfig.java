package com.example.quartz.config;

import java.util.Date;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.example.quartz.jobs.MySampleJob;

@Configuration
public class QuartzConfig {

  @Autowired
  private ApplicationContext applicationContext;

  @Bean
  public JobDetailFactoryBean jobDetail() {
    JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
    jobDetailFactory.setJobClass(MySampleJob.class);
    jobDetailFactory.setDescription("Invoke Sample Job service...");
    jobDetailFactory.setDurability(true);
    return jobDetailFactory;
  }

  @Bean
  public SimpleTriggerFactoryBean trigger(JobDetail job) {
    SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
    trigger.setJobDetail(job);
    trigger.setRepeatInterval(30000); // Set interval to 5 minutes (300,000 milliseconds)
    trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    trigger.setStartTime(new Date(System.currentTimeMillis() + 5000)); // Start time set to current
                                                                       // date plus 5
    // seconds
    return trigger;
  }

  @Bean
  public SchedulerFactoryBean scheduler(Trigger trigger, JobDetail job) {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
    schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties")); // Ensure the
                                                                                    // correct path
                                                                                    // to
    // Quartz.properties

    schedulerFactory.setJobFactory(springBeanJobFactory());
    schedulerFactory.setJobDetails(job);
    schedulerFactory.setTriggers(trigger);

    // Set Quartz properties programmatically
    Properties quartzProperties = new Properties();
    quartzProperties.setProperty("org.quartz.scheduler.instanceName", "QuartzScheduler");
    quartzProperties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
    quartzProperties.setProperty("org.quartz.threadPool.class",
        "org.quartz.simpl.SimpleThreadPool");
    quartzProperties.setProperty("org.quartz.threadPool.threadCount", "2");
    quartzProperties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
    schedulerFactory.setQuartzProperties(quartzProperties);

    return schedulerFactory;
  }

  @Bean
  public SpringBeanJobFactory springBeanJobFactory() {
    SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

}