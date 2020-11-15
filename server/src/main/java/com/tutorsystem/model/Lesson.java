package com.tutorsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lesson")
public class Lesson implements Comparable<Lesson>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JsonIgnoreProperties({"tutorLessons", "paymentsSent", "paymentsReceived", "students"})
    private User tutor;

    @ManyToOne
    @JsonIgnoreProperties({"studentLessons", "tutor", "paymentsSent", "paymentsReceived"})
    private User student;

    private int rate;

    private Date start, end;

    @Transient
    private int week;

    @Transient
    private int hours;

    @Transient
    private boolean paid;

    public Lesson() {
    }

    public int getWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.start);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public int getHours() {
        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        return (int) (this.end.getTime() - this.start.getTime()) / MILLI_TO_HOUR;
    }

    public boolean isPaid(){
        int balance = this.student.getTotalPaid();
        List<Lesson> lessons = this.student.getStudentLessons();
        Collections.sort(lessons);
        for(Lesson l : lessons){
            balance -= l.getRate()*l.getHours();
            if(l.equals(this)) return balance >= 0;
        }
        return false;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public int compareTo(Lesson lesson) {
        return this.getStart().compareTo(lesson.getStart());
    }
}
