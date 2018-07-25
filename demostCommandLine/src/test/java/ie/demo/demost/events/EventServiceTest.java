package ie.demo.demost.events;


import ie.demo.demost.entities.Event;
import ie.demo.demost.services.EventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventServiceTest {

    private static final Logger logger=LoggerFactory.getLogger(EventServiceTest.class);

    @Autowired
    private EventService eventService;


    @Test
    public void saveTest() throws Exception {
        logger.info("Init saveTest");
        Event event = new Event();
        event.setId("test1");
        event.setDuration(5);
        event.setAlert(true);
        eventService.save(event);
        Optional<Event> event1 = eventService.findById("test1");
        assertTrue("Event test1 not inserted in db", event1.isPresent());
        logger.info("saveTest finished");

    }


    @Test
    public void findByIdTest() throws Exception {
        logger.info("Init saveTest");

        Optional<Event> event1 = eventService.findById("test0");
        assertTrue("Event test0 is not present in db", event1.isPresent());

        logger.info("findByTest finished");

    }
}
