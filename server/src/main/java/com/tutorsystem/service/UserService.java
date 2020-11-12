package com.tutorsystem.service;

import com.tutorsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tutorsystem.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService implements CustomService<User, Long>{

    @Autowired
    private UserRepository repository;

    @Override
    public List<User> findAll() {
        return this.repository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> optionalUser = this.repository.findById(id);
        return optionalUser.orElse(null);

    }

    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public void save(User user) {
        this.repository.save(user);
    }

    public User findByEmail(String email){
        Optional<User> optionalUser = this.repository.findByEmail(email);
        return optionalUser.orElse(null);
    }

    public User findByEmailAndPassword(String email, String password) {
        Optional<User> optionalUser = repository.findByEmailAndPassword(email,password);
        return optionalUser.orElse(null);
    }

    public User findByTutorCode(int code){
        Optional<User> optionalUser = this.repository.findByTutorCode(code);
        return optionalUser.orElse(null);
    }

    public int generateTutorCode(){
        int code = new Random().nextInt(1000000);
        while(findByTutorCode(code) != null) {
            code = new Random().nextInt(1000000);
        }
        return code;
    }

    public User findByActivationCode(int activationCode) {
        Optional<User> optionalUser = this.repository.findByActivationCode(activationCode);
        return optionalUser.orElse(null);
    }
}
