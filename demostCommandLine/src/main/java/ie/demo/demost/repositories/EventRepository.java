package ie.demo.demost.repositories;

import ie.demo.demost.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
}
