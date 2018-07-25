package ie.demo.demost.events;

import ie.demo.demost.dtos.EventDto;
import ie.demo.demost.readers.Reader;
import ie.demo.demost.readers.impl.EventDtoReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventDtoReaderTest {


    private static final Logger logger= LoggerFactory.getLogger(EventDtoReaderTest.class);

    /*
 boolean moveNext() throws ReaderException;

    T read() throws ReaderException;

 */


    private static final String SOURCE_DIR_NAME ="./jsonManageTest";


    @Test
    public void moveNextTest() throws Exception{
        logger.info("Init moveNextTest running");

        Reader<EventDto> eventDtoReader=new EventDtoReader(SOURCE_DIR_NAME + File.separator+"testIn.json");

        assertTrue("not json object find out", eventDtoReader.moveNext());
        logger.info("moveNextTest finished");

    }


    @Test
    public void readTest() throws Exception{
        logger.info("Init readTest running");

        Reader<EventDto> eventDtoReader=new EventDtoReader(SOURCE_DIR_NAME + File.separator+"testIn.json");

        assertTrue("not json object find out", eventDtoReader.moveNext());
        assertTrue("The eventDto is null", eventDtoReader.moveNext());
        logger.info("readTest finished");

    }



}
