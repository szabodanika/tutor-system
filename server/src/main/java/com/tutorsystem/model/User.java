package model;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName, lastName, email, phone, password;
    private Date registered;
    private boolean isTutor;

    @OneToOne
    private User tutor;

    @OneToMany(mappedBy = "lesson",  cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL)
    private List<User> students;

    @OneToMany(mappedBy = "payment",  cascade = CascadeType.ALL)
    private List<Lesson> payments;

    public User() {
    }

    public User(long id, String firstName, String lastName, String email, String phone, String password, Date registered, boolean isTutor, User tutor, List<Lesson> lessons, List<User> students, List<Lesson> payments) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.registered = registered;
        this.isTutor = isTutor;
        this.tutor = tutor;
        this.lessons = lessons;
        this.students = students;
        this.payments = payments;
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
        return isTutor;
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

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<Lesson> getPayments() {
        return payments;
    }

    public void setPayments(List<Lesson> payments) {
        this.payments = payments;
    }
}
