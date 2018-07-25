package ie.demo.demost.services.impl;

import ie.demo.demost.entities.Event;
import ie.demo.demost.repositories.EventRepository;
import ie.demo.demost.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void save(Event event) {
        this.eventRepository.save(event);
    }

    @Override
    public Optional<Event> findById(String eventId) {
        return this.eventRepository.findById(eventId);
    }
}
