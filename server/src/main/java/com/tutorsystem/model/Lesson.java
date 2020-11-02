package model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User tutor;

    @ManyToOne
    private User student;
    private int rate;
    private Date start, end;

    @ManyToOne
    private Payment payment;

    public Lesson() {
    }

    public Lesson(long id, User tutor, User student, int rate, Date start, Date end, Payment payment) {
        this.id = id;
        this.tutor = tutor;
        this.student = student;
        this.rate = rate;
        this.start = start;
        this.end = end;
        this.payment = payment;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
