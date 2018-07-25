package ie.demo.demost.events;

import ie.demo.demost.dtos.EventDto;
import ie.demo.demost.readers.Reader;
import ie.demo.demost.readers.impl.EventDtoReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/*
Test the eventReader
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EventDtoReaderTest {


    private static final Logger logger = LoggerFactory.getLogger(EventDtoReaderTest.class);

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
    public void moveNextTest() throws Exception {
        logger.info("Init moveNextTest running");

        Reader<EventDto> eventDtoReader = new EventDtoReader(sourceDirName + File.separator + "testIn.json");

        assertTrue("not json object find out", eventDtoReader.moveNext());
        logger.info("moveNextTest finished");

    }


    @Test
    public void readTest() throws Exception {
        logger.info("Init readTest running");

        Reader<EventDto> eventDtoReader = new EventDtoReader(sourceDirName + File.separator + "testIn.json");

        assertTrue("not json object find out", eventDtoReader.moveNext());
        assertTrue("The eventDto is null", eventDtoReader.moveNext());
        logger.info("readTest finished");

    }


}
