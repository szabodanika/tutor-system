package com.tutorsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private Date date;

    @ManyToOne
    @JsonIgnoreProperties({"students", "tutorLessons", "studentLessons", "paymentsReceived", "paymentsSent"})
    private User user;

    private String content;

    public Announcement() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
