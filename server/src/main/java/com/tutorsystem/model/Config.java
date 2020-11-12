package com.tutorsystem.model;

import javax.persistence.*;

@Entity
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // safety features
    private String masterPassword;
    private boolean loginOpen = true,
            signupOpen = true,
            restOpen = true,
            allowTutorSignup = true;
    private int registrationsPerMinute = 6,
            loginsPerMinute = 12,
            requestsPerMinute = 600,
            activationCodeMaxValue = 100000000;

    public Config() {
    }

    public int getActivationCodeMaxValue() {
        return activationCodeMaxValue;
    }

    public void setActivationCodeMaxValue(int activationCodeMaxValue) {
        this.activationCodeMaxValue = activationCodeMaxValue;
    }

    public boolean isAllowTutorSignup() {
        return allowTutorSignup;
    }

    public void setAllowTutorSignup(boolean allowTutorSignup) {
        this.allowTutorSignup = allowTutorSignup;
    }

    public long getId() {
        return id;
    }

    public int getRegistrationsPerMinute() {
        return registrationsPerMinute;
    }

    public void setRegistrationsPerMinute(int registrationsPerMinute) {
        this.registrationsPerMinute = registrationsPerMinute;
    }

    public int getLoginsPerMinute() {
        return loginsPerMinute;
    }

    public void setLoginsPerMinute(int loginsPerMinute) {
        this.loginsPerMinute = loginsPerMinute;
    }

    public int getRequestsPerMinute() {
        return requestsPerMinute;
    }

    public void setRequestsPerMinute(int requestsPerMinute) {
        this.requestsPerMinute = requestsPerMinute;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isRestOpen() {
        return restOpen;
    }

    public void setRestOpen(boolean restOpen) {
        this.restOpen = restOpen;
    }

    public boolean isLoginOpen() {
        return loginOpen;
    }

    public void setLoginOpen(boolean loginOpen) {
        this.loginOpen = loginOpen;
    }

    public boolean isSignupOpen() {
        return signupOpen;
    }

    public void setSignupOpen(boolean signupOpen) {
        this.signupOpen = signupOpen;
    }
}
