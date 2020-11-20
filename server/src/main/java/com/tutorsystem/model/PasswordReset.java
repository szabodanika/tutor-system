package com.tutorsystem.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private User user;

    private Date date;

    private int resetCode;

    @Transient
    private boolean valid;

    public PasswordReset() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getResetCode() {
        return resetCode;
    }

    public void setResetCode(int resetCode) {
        this.resetCode = resetCode;
    }

    public boolean isValid() {
        int HOUR_IN_MILLIS = 3600000;
        long diff = new Date().getTime() - this.date.getTime();
        return diff < HOUR_IN_MILLIS;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
