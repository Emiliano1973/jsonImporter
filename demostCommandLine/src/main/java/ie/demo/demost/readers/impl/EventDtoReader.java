package ie.demo.demost.readers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.demo.demost.dtos.EventDto;
import ie.demo.demost.readers.Reader;
import ie.demo.demost.readers.ReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/*
    This class reads a jsonlines file and
 */
public class EventDtoReader implements Reader<EventDto> {

    private static final Logger logger=LoggerFactory.getLogger(EventDtoReader.class);
    private static final boolean IS_DEBUG=logger.isDebugEnabled();
    private Scanner scanner;
    private ObjectMapper objectMapper = null;
    private EventDto eventDto = null;

    /**
     * Istsance a EventDtoReader
     * @param fileName
     * @throws ReaderException
     */
    public EventDtoReader(String fileName) throws ReaderException {
        try {

            logger.info("Json line file to import :"+fileName);
            this.scanner = new Scanner(new FileInputStream(new File(fileName)));

            this.objectMapper = new ObjectMapper();
        } catch (FileNotFoundException e) {
            throw new ReaderException("File " + fileName + " not found :" + e.getMessage(), e);
        }
    }

    @Override
    public boolean moveNext() throws ReaderException {
        //I am reading the record from file
        try {
            this.eventDto = null;
            if (!this.scanner.hasNextLine())
                return false;
            String record = this.scanner.nextLine();
            if ((record == null) || (record.trim().equals("")))
                return false;
            if (IS_DEBUG)logger.debug("Json line format :"+record);
            this.eventDto = this.objectMapper.readValue(record, EventDto.class);
            if (IS_DEBUG)logger.debug("Json line format converted id java object :"+this.eventDto);
            return true;
        } catch (IOException e) {
            throw new ReaderException("Impossible to read the record from file :" + e.getMessage(), e);

        }
    }

    @Override
    public EventDto read() {
        return this.eventDto;
    }

    @Override
    public void close() throws IOException {
        eventDto = null;
    if (IS_DEBUG)logger.debug("Closing file....");
        if (this.scanner != null)
            this.scanner.close();
        if (IS_DEBUG)logger.debug("File closed.");
    }

}
