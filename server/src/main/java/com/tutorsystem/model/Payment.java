package com.tutorsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JsonIgnoreProperties({"paymentsSent", "tutor", "studentLessons"})
    private User student;

    @ManyToOne
    @JsonIgnoreProperties({"students", "paymentsReceived", "tutorLessons"})
    private User tutor;

    private Date date;

    private int amount;

    private String comment;

    public Payment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String transactionNumber) {
        this.comment = transactionNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

