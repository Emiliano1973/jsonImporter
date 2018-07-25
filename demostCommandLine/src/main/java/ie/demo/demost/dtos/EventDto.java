package ie.demo.demost.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ie.demo.demost.entities.enums.State;
import ie.demo.demost.utils.converters.JSONStateDeserializer;

import java.io.Serializable;
import java.util.Objects;

/**
 * this object memorize the data from json object
 */
public class EventDto implements Serializable {

    private String id;

    private long timestamp;

    @JsonDeserialize(using = JSONStateDeserializer.class)
    private State state;

    private String type;

    private String host;

    public EventDto() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (o.getClass() != this.getClass()) return false;
        EventDto eventDto = (EventDto) o;
        return timestamp == eventDto.timestamp &&
                Objects.equals(id, eventDto.id) &&
                state == eventDto.state &&
                Objects.equals(type, eventDto.type) &&
                Objects.equals(host, eventDto.host);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, timestamp, state, type, host);
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", state=" + state +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
