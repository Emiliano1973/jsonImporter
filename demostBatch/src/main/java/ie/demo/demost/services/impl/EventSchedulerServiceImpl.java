package ie.demo.demost.services.impl;

import ie.demo.demost.services.EventSchedulerService;
import ie.demo.demost.services.ImporterLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EventSchedulerServiceImpl implements EventSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(EventSchedulerServiceImpl.class);


    @Autowired
    private ImporterLauncher importerLauncher;

    @Override
    @Scheduled(cron = "0 0/15 * * * ?")
    @Async
    public void executeScheduling() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        logger.info("Import events running");
        importerLauncher.runImport();
        logger.info("Import events finished");
    }
}
