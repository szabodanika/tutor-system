package com.tutorsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName, lastName, email, phone;
    private Date registered;
    private int rate, tutorCode;
    private boolean activated, disabled, admin;

    @JsonIgnore
    private String password;

    @ManyToOne
    @JsonIgnoreProperties({"students", "tutorLessons", "paymentsReceived"})
    private User tutor;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("tutor")
    private List<Lesson> tutorLessons;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("student")
    private List<Lesson> studentLessons;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"tutor", "studentLessons", "paymentsSent", "paymentsReceived"})
    private List<User> students;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("student")
    private List<Payment> paymentsSent;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("tutor")
    private List<Payment> paymentsReceived;

    private boolean isTutor = false;

    @Transient
    private int totalPaid;

    @Transient
    private int totalDebt;

    private int activationCode;


    public User() {
    }

    private void computeBalance() {
        this.totalPaid = 0;
        this.totalDebt = 0;
        if (this.studentLessons != null) {
            for (Lesson lesson : this.studentLessons) {
                totalDebt += lesson.getRate() * lesson.getHours();
            }
        }

        if (this.paymentsSent != null) {
            for (Payment payment : this.paymentsSent) {
                totalPaid += payment.getAmount();
            }
        }
    }

    public int getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(int activationCode) {
        this.activationCode = activationCode;
    }

    public int getTutorCode() {
        return tutorCode;
    }

    public void setTutorCode(int tutorCode) {
        this.tutorCode = tutorCode;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getTotalPaid() {
        computeBalance();
        return totalPaid;
    }

    public void setTotalPaid(int totalPaid) {
        this.totalPaid = totalPaid;
    }

    public int getTotalDebt() {
        computeBalance();
        return totalDebt;
    }

    public void setTotalDebt(int totalDebt) {
        this.totalDebt = totalDebt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public boolean isTutor() {
        return !this.students.isEmpty();
    }

    public void setTutor(boolean tutor) {
        isTutor = tutor;
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public List<Lesson> getTutorLessons() {
        return tutorLessons;
    }

    public void setTutorLessons(List<Lesson> tutorLessons) {
        this.tutorLessons = tutorLessons;
        computeBalance();
    }

    public List<Lesson> getStudentLessons() {
        return studentLessons;
    }

    public void setStudentLessons(List<Lesson> studentLessons) {
        this.studentLessons = studentLessons;
        computeBalance();
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<Payment> getPaymentsSent() {
        return paymentsSent;
    }

    public void setPaymentsSent(List<Payment> paymentsSent) {
        this.paymentsSent = paymentsSent;
        computeBalance();
    }

    public List<Payment> getPaymentsReceived() {
        return paymentsReceived;
    }

    public void setPaymentsReceived(List<Payment> paymentsReceived) {
        this.paymentsReceived = paymentsReceived;
        computeBalance();
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
