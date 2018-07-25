package ie.demo.demost.events;

import ie.demo.demost.entities.Event;
import ie.demo.demost.services.EventManagerService;
import ie.demo.demost.services.EventService;
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

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventManagerServiceTest {

    private static final Logger logger=LoggerFactory.getLogger(EventManagerServiceTest.class);

    @Autowired
    private EventManagerService eventManagerService;

    @MockBean
    private EventService eventService;


    private static final String SOURCE_DIR_NAME ="./jsonManageTest";


    @Test
    public void storeEventsTest() throws Exception{
        logger.info("Init storeEventsTest");
        doNothing().when(eventService).save(any(Event.class));
        spy(eventService).save(any(Event.class));
        eventManagerService.storeEvents(SOURCE_DIR_NAME+File.separator+"testIn.json");

        logger.info("storeEventsTest finished");

    }



}


