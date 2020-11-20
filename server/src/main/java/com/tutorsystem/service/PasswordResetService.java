package com.tutorsystem.service;

import com.tutorsystem.model.Announcement;
import com.tutorsystem.model.PasswordReset;
import com.tutorsystem.model.User;
import com.tutorsystem.repository.AnnouncementRepository;
import com.tutorsystem.repository.PasswordResetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetRepository repository;

    public List<PasswordReset> getAll(){
        return this.repository.findAll();
    }

    public PasswordReset findByUser(User user){
        return this.repository.findByUser(user);
    }

    public void save(PasswordReset passwordReset){
        this.repository.save(passwordReset);
    }

    public int generateResetCode() {
        return new Random().nextInt(10000000);
    }

    public PasswordReset findByCode(int resetCode) {
        return this.repository.findByResetCode(resetCode);
    }

    public void remove(PasswordReset passwordReset){
        this.repository.delete(passwordReset);
    }
}
