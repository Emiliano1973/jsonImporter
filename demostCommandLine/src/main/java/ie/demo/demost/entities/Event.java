package ie.demo.demost.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 */
@Entity
@Table(name = "EVENTS")
public class Event implements Serializable {


    @Id
    @Column(name = "EVENT_ID")
    private String id;

    @Column(name = "EVENT_DURATION", nullable = false)
    private long duration;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "HOST")
    private String host;

    @Column(name = "ALERT", nullable = false)
    private boolean alert;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }
}
