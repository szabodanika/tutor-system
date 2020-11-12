package com.tutorsystem.session;

import com.tutorsystem.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSessionBean {

    private static final int MAX_AGE_LIMIT = 60;

    private User user;
    private Date sessionStart = new Date();

    public String validate(Long user) {
        if(this.user == null) return "not_logged_in";
        if(user != this.user.getId()) return "access_denied";
        return this.validate();
    }

    public String validate() {
        if(user == null) return "not_logged_in";
        if(!user.isActivated()) return "account_not_activated";
        if(user.isDisabled()) return "account_disabled";
        if(getAgeMinutes() > MAX_AGE_LIMIT) return "session_expired";
        return "ok";
    }

    public int getAgeMinutes() {
        return (int) ((new Date().getTime() - sessionStart.getTime()) / 60000);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.sessionStart = new Date();
    }


}
