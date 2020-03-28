package unioeste.smch.alerts.entities;

import unioeste.smch.core.entities.User;
import unioeste.smch.core.models.Topic;

import javax.persistence.*;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double value;

    private Long timestamp;

    private Topic topic;

    @ManyToOne
    private User user;

    private boolean verified;

    public Alert() {}

    public Alert(User user, Long timestamp, String value, Topic topic) {
        this.user = user;
        this.timestamp = timestamp;
        this.value =  Double.valueOf(value);
        this.topic = topic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Topic getTopic() {
        return topic;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
