package ie.demo.demost.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.net.MalformedURLException;

/**
 * Created by Echinofora1973 on 22/05/2017.
 */
@Configuration
public class BatchConfig {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Value("classpath:org/springframework/batch/core/schema-drop-hsqldb.sql")
    private org.springframework.core.io.Resource dropReopsitoryTables;

    @Value("classpath:org/springframework/batch/core/schema-hsqldb.sql")
    private org.springframework.core.io.Resource dataReopsitorySchema;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean(name = "eventJob")
    public Job job(@Qualifier("step1") Step step1) {
        return jobs.get("eventJob")
                .start(step1).build();
    }


    @Bean
    protected Step step1(@Qualifier("eventTasklet") Tasklet tasklet) {
        return steps.get("eventStep")
                .tasklet(tasklet)
                .build();
    }


    @Bean("jobRepository")
    public JobRepository jobRepository(DataSource dataSource,
                                       @Qualifier("internalTransactionManager") PlatformTransactionManager internalTransactionManager) {
        logger.info("--------------Init batch repository");
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(internalTransactionManager);
        jobRepositoryFactoryBean.setDatabaseType("hgsqldb");


        try {
            jobRepositoryFactoryBean.afterPropertiesSet();
            logger.info("--------------End Init batch repository");

            return (JobRepository) jobRepositoryFactoryBean.getObject();
        } catch (Exception e) {
            logger.error("error in config :" + e.getMessage(), e);
            throw new RuntimeException("error in config :" + e.getMessage(), e);
        }
    }


    @Bean("internalTransactionManager")
    protected PlatformTransactionManager internalTransactionManager() {

        return new ResourcelessTransactionManager();
    }


    @Bean("jobLauncher")
    public JobLauncher getJobLauncher(@Qualifier("jobRepository") JobRepository jobRepository) {
        logger.info("--------------Init Job launcher");

        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        try {
            jobLauncher.afterPropertiesSet();
            logger.info("--------------End Init Job launcher");

        } catch (Exception e) {
            logger.error("error in config :" + e.getMessage(), e);
            throw new RuntimeException("error in config :" + e.getMessage(), e);
        }
        return jobLauncher;
    }


    @Bean
    public DataSourceInitializer dataSourceInitializer(@Qualifier("dataSource") DataSource dataSource) throws MalformedURLException {
        logger.info("--------------Init Batch dataSourceInitializer");
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();

        databasePopulator.addScript(dropReopsitoryTables);
        databasePopulator.addScript(dataReopsitorySchema);
        databasePopulator.setIgnoreFailedDrops(true);

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);
        logger.info("--------------Init Batch dataSourceInitializer finished");

        return initializer;
    }
}
