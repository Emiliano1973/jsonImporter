package ie.demo.demost.services;

import ie.demo.demost.entities.Event;

import java.util.Optional;

public interface EventService {

    void save(Event event);

    Optional<Event> findById(String eventId);
}
