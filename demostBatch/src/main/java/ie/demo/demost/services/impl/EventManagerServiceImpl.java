package ie.demo.demost.services.impl;

import ie.demo.demost.dtos.EventDto;
import ie.demo.demost.entities.Event;
import ie.demo.demost.entities.enums.State;
import ie.demo.demost.readers.Reader;
import ie.demo.demost.readers.ReaderException;
import ie.demo.demost.readers.impl.EventDtoReader;
import ie.demo.demost.services.EventManagerService;
import ie.demo.demost.services.EventManagerServiceException;
import ie.demo.demost.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EventManagerServiceImpl implements EventManagerService {

    private static final Logger logger = LoggerFactory.getLogger(EventManagerServiceImpl.class);
    private static final boolean IS_DEBUG = logger.isDebugEnabled();
    @Autowired
    private EventService eventService;

    @Override
    public void storeEvents(String filename) throws EventManagerServiceException {
        if (IS_DEBUG) logger.debug("starting storing events");

        try (Reader<EventDto> eventDtoReader = new EventDtoReader(filename)) {

            Map<String, EventDto> eventsMapCache = new HashMap<>();
            while (eventDtoReader.moveNext()) {
                EventDto eventDto = eventDtoReader.read();
                manageEvent(eventDto, eventsMapCache);
            }

            if (!eventsMapCache.isEmpty()) {
                eventsMapCache.forEach((k, v) -> {
                    String message = "Event " + k + "is still open, the status:" + v.getState().getValue();
                    logger.warn(message);
                });
            }
        } catch (ReaderException e) {
            throw new EventManagerServiceException("Error in data reading :" + e.getMessage(), e);
        } catch (IOException e) {
            throw new EventManagerServiceException("Error in data reading :" + e.getMessage(), e);

        }
        if (IS_DEBUG) logger.debug("Ending storing events");
    }


    /**
     * This method insert the data in db when found the two object that have the same id
     *
     * @param eventDto
     * @param eventDtoMapCache
     */
    private void manageEvent(EventDto eventDto, Map<String, EventDto> eventDtoMapCache) {
        //I am checking if the event it's present in the cache;
        if (!eventDtoMapCache.containsKey(eventDto.getId())) {
            //if not present I add it int the cache by event id
            eventDtoMapCache.put(eventDto.getId(), eventDto);
        } else {
            //otherwise
            EventDto eventDto1 = eventDtoMapCache.get(eventDto.getId());
            long startTime = (eventDto.getState() == State.STARTED) ? eventDto.getTimestamp() : eventDto1.getTimestamp();
            long endTime = (eventDto.getState() == State.FINISHED) ? eventDto.getTimestamp() : eventDto1.getTimestamp();
            long period = endTime - startTime;
            boolean alert = period > 4l;
            Event event = new Event();
            event.setId(eventDto.getId());
            event.setType(getMergedValue(eventDto.getType(), eventDto1.getType()));
            event.setHost(getMergedValue(eventDto.getHost(), eventDto1.getHost()));
            event.setDuration(period);
            event.setAlert(alert);
            if (IS_DEBUG) logger.debug("Insert Event with id :" + event.getId() + "");
            this.eventService.save(event);
            eventDtoMapCache.remove(eventDto.getId());
            if (IS_DEBUG) logger.debug("Event with id :" + event.getId() + " inserted");
        }

    }

    private String getMergedValue(String string1, String string2) {
        if (
                ((string1 == null) || (string1.trim().equals("")))
                        &&
                        ((string2 == null) || (string2.trim().equals("")))
                ) {
            return null;
        }
        if ((string1 != null) && (!string1.trim().equals(""))) {
            return string1;
        }
        if ((string2 != null) && (!string2.trim().equals(""))) {
            return string2;
        }


        return null;

    }


}
