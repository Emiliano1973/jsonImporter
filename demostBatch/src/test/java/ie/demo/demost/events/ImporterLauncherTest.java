package ie.demo.demost.events;

import ie.demo.demost.entities.Event;
import ie.demo.demost.services.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.batch.runtime.BatchStatus;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImporterLauncherTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job eventJob;


    @MockBean
    private EventService eventService;
    @Value("${event.app.json.toWork.path}")
    private String sourceDirName;

    @Value("${event.app.json.worked.path}")
    private String workedFilesDirName;


    @Before
    public void setUp() throws Exception {
        File dest = new File(workedFilesDirName);
        File[] files = dest.listFiles();
        if (files.length > 0) {
            for (File file : files) {
                String soourceFile = sourceDirName + File.separator + file.getName();
                Files.move(Paths.get(file.toURI()), Paths.get(soourceFile));
            }
        }

    }

    @Test
    public void launchJob() throws Exception {
        doNothing().when(eventService).save(any(Event.class));
        spy(eventService).save(any(Event.class));
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJob(eventJob);
        //testing a job
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(newExecution());

        //Testing a individual step
        //JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1");

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus().getBatchStatus());

    }

    private JobParameters newExecution() {
        Map<String, JobParameter> parameters = new HashMap<>();

        JobParameter parameter = new JobParameter(new Date());
        parameters.put("currentTime", parameter);

        return new JobParameters(parameters);
    }

}
