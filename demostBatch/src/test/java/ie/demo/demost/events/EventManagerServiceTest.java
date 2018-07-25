package ie.demo.demost.events;

import ie.demo.demost.entities.Event;
import ie.demo.demost.services.EventManagerService;
import ie.demo.demost.services.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventManagerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(EventManagerServiceTest.class);

    @Autowired
    private EventManagerService eventManagerService;

    @MockBean
    private EventService eventService;

    @Value("${event.app.json.toWork.path}")
    private String sourceDirName;

    @Value("${event.app.json.worked.path}")
    private String workedFilesDirName;

    //move the files from the destinations if aothet test are executed to source where the test read
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
    public void storeEventsTest() throws Exception {
        logger.info("Init storeEventsTest");
        doNothing().when(eventService).save(any(Event.class));
        spy(eventService).save(any(Event.class));
        eventManagerService.storeEvents(sourceDirName + File.separator + "testIn.json");

        logger.info("storeEventsTest finished");

    }


}


