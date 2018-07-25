package ie.demo.demost.services.impl;

import ie.demo.demost.services.BatchExecuterManagerService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * To use in batch
 */
@Component("eventTasklet")
public class EventTaskletImpl implements Tasklet {

    @Autowired
    private BatchExecuterManagerService batchExecuterManagerService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        batchExecuterManagerService.execute();
        return RepeatStatus.FINISHED;
    }
}
